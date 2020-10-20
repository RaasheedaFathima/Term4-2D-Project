# Term4-2D-Project

Notes:
the codes may need extra imports like:
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.Iterator;

We also have a method called substitute(ImList<Clause> clauses, Literal l) which takes in a list of clauses (called “clauses”) and a literal l as the input. It utilizes an Iterator<Clauses> called “Citer” to iterate through each clause in the list of clauses and eliminate/reduce the clauses as a result of setting literal l to true, where the new clauses after reduction will be stored to a new Immutable List of Clauses we declare called “new_clauses”. How this works is that when one of the literals in a Clause is true, since it is a disjunction, the value of the Clause will automatically be true analogous to an OR function, that is why we can reduce the clause to make the problem simpler. We also declare some kind of temporary storage of Clause called “tmp_clause”. Using a while loop, we use the Citer.hasNext() method as the condition to check if the iterator still has another clause in the sequence to evaluate through, if it does then it will set the next clause in line to “tmp_clause” using Citer.next() and subsequently runs the reduce() method of the Clause class using literal l as its argument, and if the reduced clause is not null it will be added to the new list of clauses “new_clauses” using the add() method. In the end of this method, we return the new Immutable List of clauses “new_clauses”. 
