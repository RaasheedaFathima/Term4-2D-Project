package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;

import sat.env.*;
import sat.formula.*;



public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    public static String numberVariables;

    public void startSATSolver(Formula f) throws IOException {
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
            writeFile(e);
        }
    }

    public static void writeFile(Environment env) throws IOException{
        FileWriter writer = new FileWriter("BoolAssignment.txt");
        for(Integer n = 1; n<=Integer.parseInt(numberVariables); n++){
            Variable vars = new Variable(n.toString());
            writer.write(n.toString()+":"+env.get(vars).toString()+"\n");
        }
        writer.close();
    }

    public static Formula readFile(String filename) { //method to read the file
        Scanner scans = null; //creates scanner object with initial value null
        boolean existsProblem = false; //boolean variable to indicate whether there is a problem to solve or not
        Clause[] listofclauses = null; //stores an array of clauses
        int pointerClause = 0; //pointer for a clause
        try {
            scans = new Scanner(new File(filename));
            String inputstring;
            while (scans.hasNextLine()) { //while the scanner object is still able to scan the next line
                inputstring = scans.nextLine(); //create a string from the nextLine scanned by the scanner
                if (inputstring.isEmpty() || inputstring.charAt(0) == 'c') { //if the line is empty or the line is a comment line then skip it
                    continue;
                }
                if (inputstring.charAt(0) == 'p') { //if the first character of the line/string is p means its a problem
                    String[] listofstring = inputstring.split(" "); //convert the line into a list of strings divided based on spaces
                    Integer numberofclauses = Integer.parseInt(listofstring[listofstring.length - 1]);
                    numberVariables = listofstring[2];
                    listofclauses = new Clause[numberofclauses];
                    pointerClause = numberofclauses - 1; //create a pointer which will be used to insert clauses at indexes in list of clauses
                    existsProblem = true;
                    break;
                }
            }
            if (existsProblem == true) { //condtion if there exists a problem
                scans.useDelimiter(" 0"); //to distinguish lines based on the last string " 0"
                while (scans.hasNext()) {
                    String nextstring;
                    nextstring = scans.next();
                    String[] listofvalues = nextstring.trim().split(" ");
                    Literal[] listofliterals = new Literal[listofvalues.length];
                    for (int k = 0; k < listofliterals.length; k++) {
                        String listofstring = listofvalues[k].trim();
                        if (listofstring.length() > 0) {
                            listofliterals[k] = (listofstring.charAt(0) == '-') ? NegLiteral.make(listofstring.substring(1)) : PosLiteral.make(listofstring);
                        }
                    }
                    if (listofliterals[0] != null) { //if the first element of the list of literals is not null
                        listofclauses[pointerClause] = makeCl(listofliterals); //make a new entry in the listofclauses with index pointerClause
                        if (listofclauses[pointerClause] == null) //if the clause at the position marked by pointerClause is null
                            throw new IOException("not satisfiable");
                    }
                    pointerClause--; //move pointer down by 1 index position
                }
            } else { //condition if there exists no problem
                throw new IOException("no P character or problem found");
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
            String filename = args[0]; //the filename supplied will be the first entry of the args array
            SATSolverTest testings = new SATSolverTest(); //create new instance of the SATSolverTest
            Formula formulatest = readFile(filename); //creates formula called formulatest by utilizing the readFile method on file specified by filename
            testings.startSATSolver(formulatest); //runs startSATSolver with formulatest as its argument
        }else{
            throw new IOException("Please supply the location of the file");
        }
    }
}
