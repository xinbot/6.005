package sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sat.env.Bool;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void SATTest() {
    	//(a | !b) & (a | b)
    	Clause c1 = makeClause(a, nb);
    	Clause c2 = makeClause(a, b);
    	Formula f = makeFormula(c1, c2);
    	
    	Environment env = SATSolver.solve(f);
    	assertEquals(env.get(a.getVariable()), Bool.TRUE);
    	
    	//(a & b) & (a & !b)
    	Formula f1 = makeFormula(new Clause(a), new Clause(b));
    	Formula f2 = makeFormula(new Clause(a), new Clause(nb));
    	env = SATSolver.solve(f1.and(f2));
    	assertNull(env);
    	
    	//(a & b) & (!b | c)
    	Clause c3 = makeClause(nb, c);
    	f = makeFormula(new Clause(a), new Clause(b), c3);
    	env = SATSolver.solve(f);
    	assertEquals(env.get(a.getVariable()), Bool.TRUE);
    	assertEquals(env.get(b.getVariable()), Bool.TRUE);
    	assertEquals(env.get(c.getVariable()), Bool.TRUE);
    	
    	//(a & !a)
    	f = makeFormula(new Clause(a), new Clause(na));
    	env = SATSolver.solve(f);
    	assertNull(env);
    	
    	//(a | !b) & (a | c)
    	c1 = makeClause(a, nb);
    	c2 = makeClause(a, c);
    	f = makeFormula(c1, c2);
    	env = SATSolver.solve(f);
    	assertEquals(env.get(a.getVariable()), Bool.TRUE);
    	assertEquals(env.get(b.getVariable()), Bool.TRUE);
    	assertEquals(env.get(c.getVariable()), Bool.TRUE);
    }
    
    // Helper function for constructing a formula.  Takes
    // a variable number of arguments, e.g.
    // makeFormula(a, b, c) will make the formula (a and b and c)
    // @param e,...   clause in the formula
    // @return formula containing e,...
    private Formula makeFormula(Clause... e) {
        Formula f = new Formula();
        for (int i = 0; i < e.length; ++i) {
            f = f.addClause(e[i]);
        }
        return f;
    }
    
    // Helper function for constructing a clause.  Takes
    // a variable number of arguments, e.g.
    // make(a, b, c) will make the clause (a or b or c)
    // @param e,...   literals in the clause
    // @return clause containing e,...
    private Clause makeClause(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }
}