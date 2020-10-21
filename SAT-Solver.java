package sat;

import java.util.Iterator;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) throws RuntimeException{
        Environment answer = new Environment();
        return solve(formula.getClauses(),answer);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        try{if(clauses.isEmpty()) {
            return env;
        }
        Clause smallest = null;
        for (Clause A:clauses) {
            if (A.isEmpty()) {
                return null;
            }
            if (smallest == null || smallest.size() > A.size()) smallest = A;
            if (smallest.isUnit()) break;
        }

        Literal Random_L = smallest.chooseLiteral();
        Variable V_RandomL = Random_L.getVariable();
        if (smallest.isUnit()){
            if (Random_L.equals(PosLiteral.make(V_RandomL)))env = env.putTrue(V_RandomL);
            else env = env.putFalse(V_RandomL);
            return solve(substitute(clauses,Random_L),env);
        }
        else {
            if (Random_L.equals(NegLiteral.make(V_RandomL))) Random_L = Random_L.getNegation();
            Environment envtrue = solve(substitute(clauses,Random_L),env.putTrue(V_RandomL));
            //System.out.println(smallest.toString());
            if (envtrue == null) return solve(substitute(clauses,Random_L.getNegation()),env.putFalse(V_RandomL));
            return envtrue;
        }}catch(NullPointerException eksepsi){
            return null;
        }catch(RuntimeException eksepsi){
            return null;
    }
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) throws RuntimeException{
        Iterator<Clause> Citer = clauses.iterator();
        ImList<Clause> new_clauses = new EmptyImList<Clause>();
        Clause tmp_clause;
        while(Citer.hasNext()){
            tmp_clause = Citer.next();
            tmp_clause = tmp_clause.reduce(l);
            if (tmp_clause!=null)
                new_clauses = new_clauses.add(tmp_clause);
        }
        return new_clauses;
    }

}
