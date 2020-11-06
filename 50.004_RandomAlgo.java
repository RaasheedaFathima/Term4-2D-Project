import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class RandomAlgo {
    /*
    Create a Hashmap to store the literals and randomly assign Boolean Variable to it.
    0 - False, 1 - True
     */
    HashMap<String, Integer> truthassign = new HashMap<String, Integer>();
    String[][] Clauses;
    int clauseIndex;
    int runCount = 0;
    int numofVar;

    public void setRandomAlgo{
        //set everything to 0
        for(int i = 0; i<=numofVar;i++)
            Clauses[][i] = "0";

    }

    RandomAlgo(String[][] Clauses, int numofVar) {
        this.numofVar = numofVar;
        this.Clauses = Clauses;
        this.clauseIndex = 0;
        randomLiterals(this.numofVar);
    }

    public RandomAlgo() {
    }

    public int randomBool() {
        return ThreadLocalRandom.current().nextInt(0, 2);
    }

    public void randomLiterals(int varNo) {
        for (int i = 1; i <= varNo; i++) {
            this.truthassign.put(String.valueOf(i), randomBool());

        }
    }

    public boolean CheckLit(String Literals) {
        if (Literals.startsWith("-")) {
            return false;
        } else {
            return true;
        }
    }

    //Randomly chooses a Literal in the Clause

    public void ChangeLit(int clauseIndex) {
        int literalValue;
        int num = randomBool();
        String literals = this.Clauses[clauseIndex][num];
        if (CheckLit(literals)) {
            literalValue = this.truthassign.get(literals);
        } else {
            if (truthassign.get(literals.substring(1, literals.length())) == 0) {
                literalValue = 1;
            } else {
                literalValue = 0;
            }
        }
        //flip the value of the literal
        if (literalValue == 1) {
            this.truthassign.put(literals, 0);
        } else {
            this.truthassign.put(literals, 1);
        }

    }

    public void GenSol() {
        generate();
        this.runCount += 1;
        //Average number of iterations is n^2
        int randomwalks = this.numofVar * this.numofVar;
        if (isSAT()) {

            System.out.println("SATISFIABLE!");
            System.out.println(this);
        } else if (this.runCount >= randomwalks) {
            System.out.println("No answer found within " + randomwalks + " iterations.");
            System.out.println("Not satisfiable");
        } else {
            ChangeLit(this.clauseIndex);
            GenSol();
        }
    }

    public boolean isSAT() {
        long started = System.nanoTime();
        if (this.clauseIndex > this.Clauses.length) {
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time:" + timeTaken / 1_000_000.0 + "ms");
            return true;
        } else {
            return false;
        }
    }

    public void generate() {
        int negative;
        this.clauseIndex = 0;
        int clauseNo = this.Clauses.length;
        outerloop:
        for (String[] clause : this.Clauses) {
            negative = 0;
            for (String literal : clause) {
                if (CheckLit(literal)) {
                    if (this.truthassign.get(literal) == 1) {
                        break;
                    } else {
                        if (negative == 0) {
                            negative += 1;
                        } else {
                            break outerloop;
                        }
                    }

                } else {
                    if (this.truthassign.get(literal.substring(1, literal.length())) == 0) {
                        break;
                    } else {
                        if (negative == 0) {
                            negative += 1;
                        } else {
                            break outerloop;
                        }
                    }

                }


            }
            this.clauseIndex += 1;
        }

        if (this.clauseIndex == clauseNo) {
            this.clauseIndex += 1;
        }

    }

    public String toString() {
        String Output = "";
        for (int j = 1; j <= this.numofVar; j++) {
            this.truthassign.get(String.valueOf(j));
            Output = Output + this.truthassign.get(String.valueOf(j)).toString() + " ";
        }
        return Output;
    }

    public static void main(String args[]){
        RandomAlgo a = new RandomAlgo();


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
    } */
