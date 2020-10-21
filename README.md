# Term4-2D-Project


Notes:
the codes may need extra imports like:
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.Iterator;

To read the file, we first pass the address of the file in the arguments configuration of android studio to a String variable called filename, then we generate a formula “formulatest” which takes value using the readFile(filename) method, where inside this function, the return value will be a call of the method makeFm() using a list of clauses as the argument, which will return formula f as its output. 

Our approach/solution to this problem, first in the SATSolver.java file we import packages from the “sat” and “immutable” libraries to obtain certain functionalities from them. In the SATSolver class, we first create a method named public static Environment solve(Formula formula), which takes in an object of the Formula class as its argument, creates an instance of Environment represented by the variable “answer”, and calls a different implementation of solve with 2 arguments, where the first argument is the formula.getClauses() method to extract the clauses from the input formula as an Immutable List, and the second argument is the environment we create. 
In the implementation of solve(ImList<Clause> clauses, Environment env),  we first check if the list of clauses is empty and returns the environment “env” if it is. Next we declare a variable for the smallest/minimum Clause and set it to null. We use a for loop to iterate through every clause (we call it “A”) in the list of clauses and for every instance of a Clause, we check whether it is empty or not, if it is empty it will return null. If the smallest is currently null or the size of the smallest clause is larger than the clause “A” we are currently iterating on, then A will be the new “smallest”. The next line checks if smallest is an unit clause with size == 1 using .isUnit() method, and if it is then it will break out of the iteration, keeping smallest as its last assigned value, otherwise it will just continue with the iteration until it finds a suitable “smallest” clause. Next we declare a literal “Random_L”, using the .chooseLiteral() method on the smallest clause which will arbitrary pick a literal from the clause, and we also declare a Variable “V_RandomL” using the getVariable method on the literal we just declared. 
We then check if the “smallest” clause is an unit clause or not, if it is meaning that the Boolean value of the clause is solely determined by its single literal, then it checks whether the Random_L is equal to a Positive Literal that is generated using the make() method with “V_RandomL” as its argument. If it is, then the environment env will bind the variable V_RandomL to the Boolean value of true, otherwise it will bind V_RandomL to the Boolean value false. Then when we use

But if the smallest clause is not an unit clause, has size larger than 1, then we assign an arbit


We also have a method called substitute(ImList<Clause> clauses, Literal l) which takes in a list of clauses (called “clauses”) and a literal l as the input. It utilizes an Iterator<Clauses> called “Citer” to iterate through each clause in the list of clauses and eliminate/reduce the clauses as a result of setting literal l to true, where the new clauses after reduction will be stored to a new Immutable List of Clauses we declare called “new_clauses”. How this works is that when one of the literals in a Clause is true, since it is a disjunction, the value of the Clause will automatically be true analogous to an OR function, that is why we can reduce the clause to make the problem simpler. We also declare some kind of temporary storage of Clause called “tmp_clause”. Using a while loop, we use the Citer.hasNext() method as the condition to check if the iterator still has another clause in the sequence to evaluate through, if it does then it will set the next clause in line to “tmp_clause” using Citer.next() and subsequently runs the reduce() method of the Clause class using literal l as its argument, and if the reduced clause is not null it will be added to the new list of clauses “new_clauses” using the add() method. In the end of this method, we return the new Immutable List of clauses “new_clauses”. 

Comp struct repo part 1 done 

