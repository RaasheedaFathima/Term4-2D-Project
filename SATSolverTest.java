package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import sat.env.*;
import sat.formula.*;



public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    public static boolean a2SATproblem = true;

    public static boolean isa2SATproblem() {
        return a2SATproblem;
    }

    public void startSATSolver(Formula f) {
        System.out.println("SAT solver starts!!!");
        long startTime = System.nanoTime();
        Environment e = SATSolver.solve(f);
        long endTime = System.nanoTime();
        long durationTime = endTime - startTime;
        System.out.println("Time:" + durationTime / 1_000_000.0 + "ms");
        if (e == null) {
            System.out.println("not satisfiable");
        } else{
            System.out.println("satisfiable");
            System.out.println(e);}
    }


    public static Formula readFile(String filename) throws IOException { //very important method to read the file
        Scanner scans = null; //creates scanner object with initial value null
        boolean existsProblem = false; //boolean variable to indicate whether there is a problem to solve or not
        Clause[] listofclauses = null; //to store an array of clauses
        int pointerClause = 0;
        try {
            scans = new Scanner(new File(filename)); //have to handle file not found exception
            String inputstring = null;
            while (scans.hasNextLine()) { //while the scanner object is still able to scan the next line
                inputstring = scans.nextLine(); //create a string from the nextLine scanned by the scanner
                if (inputstring.isEmpty() || inputstring.charAt(0) == 'c') { //if the line is empty or the line is a comment line then skip it
                    continue;
                }
                if (inputstring.charAt(0) == 'p') { //if the first character of the line/string is p means its a problem
                    String[] listofstring = inputstring.split(" "); //convert the line into a list of strings divided based on spaces
                    Integer indextoplace = Integer.parseInt(listofstring[listofstring.length - 1]);
                    listofclauses = new Clause[indextoplace];
                    pointerClause = indextoplace - 1;
                    existsProblem = true; //then there exists a problem
                    break;
                }
            }
            if (existsProblem == true) { //if there exists a problem
                scans.useDelimiter(" 0");
                while (scans.hasNext()) { //while the scanner still can scan the next string
                    String nextstring;
                    nextstring = scans.next();
                    String[] listofvalues = nextstring.trim().split(" ");
                    if (listofvalues.length > 2) { //if the clause has more than 2 "values" or in this case literals,
                        a2SATproblem = false;
                        //throw new IOException("More than 2 literals in one clause.");
                    }
                    Literal[] listofliterals = new Literal[listofvalues.length];
                    for (int k = 0; k < listofliterals.length; k++) {
                        String listofstring = listofvalues[k].trim();
                        if (listofstring.length() > 0) {
                            listofliterals[k] = listofstring.charAt(0) == '-' ? NegLiteral.make(listofstring.substring(1)) : PosLiteral.make(listofstring);
                        }
                    }

                    if (listofliterals[0] != null) { //if the first element of the list of literals is not null
                        listofclauses[pointerClause] = makeCl(listofliterals); //make a new entry in the listofclauses with index pointerClause
                        if (listofclauses[pointerClause] == null)
                            throw new IOException("unsatisfiable");
                    }
                    pointerClause--;
                }
            } else {
                throw new IOException("no problem found");
            }
        } catch (FileNotFoundException eksepsi) {
            System.out.println("File not found exception");
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException eksepsi) {
            System.out.println("CNF file contains more clause than expected");
            System.exit(0);
        } catch (IOException eksepsi) {
            System.out.println("I/O Exception");
            System.exit(0);
        }
        finally {
            scans.close(); //closes the scanner object
        }
        return makeFm(listofclauses);
    }


    public void testSATSolver1() {
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a, b)));
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/
    }


    public void testSATSolver2() {
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/
    }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }

    public static void main(String args[]) throws IOException {
        if (args.length > 0) {
            String filename = args[0]; //the filename supplied will be the first element of the args array
            SATSolverTest testings = new SATSolverTest(); //create new instance of the SATSolverTest class
            Formula formulatest = readFile(filename); //creates formula called formulatest by utilizing the readFile method on file specified by filename
            if (isa2SATproblem()) { //if the problem is a 2 literal SAT problem
                testings.startSATSolver(formulatest);
            } else if(isa2SATproblem()==false) { //if the problem is not a 2 literal SAT problem
                System.out.println("More than 2 literals");
                testings.startSATSolver(formulatest);
            }
            else{
                System.out.println("Supply the location of the file");
                System.exit(0);
            }
        }
    }
}
