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

    public void solvingwithDPLL(Formula f) {
        System.out.println("Solve with DPLL method");
        long startTime = System.nanoTime();
        Environment env = SATSolver.solve(f);
        long endTime = System.nanoTime();
        long durationTime = endTime - startTime;
        System.out.println("Time taken:" + durationTime / 1_000_000.0 + "ms");
        if (env == null) {
            System.out.println("Unsatisfiable");
        } else
            System.out.println("Satisfiable");
            System.out.println(env);
    }


    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static Formula readFile(String filename) throws IOException {
        Scanner scans = null; //creates scanner object with initial value null
        boolean existsProblem = false; //boolean variable to indicate whether there is a problem to solve or not
        Clause[] listofclauses = null; //to store an array of clauses
        int pointerClause = 0;
        try {
            scans = new Scanner(new File(filename)); //have to handle file not found exception
            String inputstring = null;

            while (scans.hasNextLine()) {
                inputstring = scans.nextLine();
                if (inputstring.isEmpty() || inputstring.charAt(0) == 'c') { //if the line is empty or the line is a comment line then skip it
                    continue;
                }
                if (inputstring.charAt(0) == 'p') { //if the first character of the line/string is p means its a problem
                    existsProblem = true;
                    String[] listofstring = inputstring.split(" ");
                    Integer indextoplace = Integer.parseInt(listofstring[listofstring.length - 1]);
                    listofclauses = new Clause[indextoplace];
                    pointerClause = indextoplace - 1;
                    break;
                }

            }

            if (existsProblem == true) {
                scans.useDelimiter(" 0");
                while (scans.hasNext()) {
                    String nextstring = scans.next();
                    String[] listofvalues = nextstring.trim().split(" ");
                    if (listofvalues.length > 2) { //if the clause has more than 2 "values" or in this case literals,
                        a2SATproblem = false;
                        throw new IOException("More than 2 literals in one clause.");
                    }
                    Literal[] listofliterals = new Literal[listofvalues.length];
                    for (int k = 0; k < listofliterals.length; k++) {
                        String listofstring = listofvalues[k].trim();
                        if (listofstring.length() > 0) {
                            listofliterals[k] = listofstring.charAt(0) == '-' ? NegLiteral.make(listofstring.substring(1)) : PosLiteral.make(listofstring);
                        }
                    }

                    if (listofliterals[0] != null) {
                        listofclauses[pointerClause] = makeCl(listofliterals);
                        if (listofclauses[pointerClause] == null)
                            throw new IOException("Unsatisfiable");
                    }
                    pointerClause--;
                }
            } else {
                throw new IOException("no P or problem found");
            }


        } catch (FileNotFoundException eksepsi) {
            eksepsi.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException eksepsi) {
            System.out.println("CNF file contains more clause than expected");
        } catch (IOException eksepsi) {
            eksepsi.printStackTrace();
        } finally {
            scans.close();
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

    //class to read the whole file


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
            String filename = args[0];
            SATSolverTest testings = new SATSolverTest();
            Formula formulatest = readFile(filename);
            if (isa2SATproblem()) {
                testings.testDPLL(formulatest);
            } else if(isa2SATproblem()==false) {
                System.out.println("More than 2 literals");
                System.exit(1);
            }
            else{
                System.out.println("Supply the location of the file. Syntax: solver.jar file");
                System.exit(1);
            }
        }


    }
}

//how to use scanner/this is just for note
/*public static void main(String args[]) throws FileNotFoundException {

    //creating File instance to reference text file in Java
    File text = new File("C:/temp/test.txt");

    //Creating Scanner instnace to read File in Java
    Scanner scnr = new Scanner(text);

    //Reading each line of file using Scanner class
    int lineNumber = 1;
    while(scnr.hasNextLine()){
        String line = scnr.nextLine();
        System.out.println("line " + lineNumber + " :" + line);
        lineNumber++;
    }

}

}
*/
