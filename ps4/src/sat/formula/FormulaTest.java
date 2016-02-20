package sat.formula;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class FormulaTest {
	Literal a = PosLiteral.make("a");
	Literal b = PosLiteral.make("b");
	Literal c = PosLiteral.make("c");
	Literal d = PosLiteral.make("d");
	Literal na = a.getNegation();
	Literal nb = b.getNegation();
	Literal nc = c.getNegation();
	Literal nd = d.getNegation();

	// make sure assertions are turned on!
	// we don't want to run test cases without assertions too.
	// see the handout to find out how to turn them on.
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void FormulaBasicTest() {
		// Empty Clause Constructor
		Formula f = new Formula();
		assertEquals(f.getSize(), 0);
		assertTrue(f.getClauses().isEmpty());

		// Single Literal Constructor
		Literal l = PosLiteral.make("");
		f = new Formula(new Clause(l));
		assertEquals(f.getSize(), 1);
		assertTrue(f.getClauses().first().isUnit());
		assertEquals(f.getClauses().first().chooseLiteral(), l);
		assertTrue(f.getClauses().rest().isEmpty());

		l = PosLiteral.make("e");
		f = new Formula(new Clause(l));
		assertEquals(f.getSize(), 1);
		assertTrue(f.getClauses().first().isUnit());
		assertEquals(f.getClauses().first().chooseLiteral(), l);
		assertTrue(f.getClauses().rest().isEmpty());

		// multiValue Clause
		Literal l1 = PosLiteral.make("l1");
		Literal l2 = PosLiteral.make("l2");
		Clause c = make(l1, l2);
		f = new Formula(c);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c);
		assertTrue(f.getClauses().rest().isEmpty());
	}

	@Test
	public void FormulaAddTest() {
		// Case one
		Formula f = new Formula();
		Clause c = make(); // an empty clause
		assertEquals(f.getSize(), 0);
		assertTrue(f.getClauses().isEmpty());

		f = f.addClause(c);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c);
		assertTrue(f.getClauses().rest().isEmpty());

		// Case two
		f = new Formula();
		Literal l1 = PosLiteral.make("l1");
		Literal l2 = PosLiteral.make("l2");
		c = make(l1, l2); // a non-empty clause
		assertEquals(f.getSize(), 0);
		assertTrue(f.getClauses().isEmpty());

		f = f.addClause(c);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c);
		assertFalse(f.getClauses().first().isEmpty());
		assertTrue(f.getClauses().rest().isEmpty());

		// Case three
		f = new Formula(c);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c);
		assertFalse(f.getClauses().first().isEmpty());
		assertTrue(f.getClauses().rest().isEmpty());

		Clause ec = make();
		f = f.addClause(ec); // a two-clause formula
		assertEquals(f.getSize(), 2);
		assertEquals(f.getClauses().first(), ec);
		assertEquals(f.getClauses().rest().first(), c);
		assertTrue(f.getClauses().rest().rest().isEmpty());

		// Case Four
		f = new Formula(c);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c);
		assertTrue(f.getClauses().rest().isEmpty());

		Literal l3 = PosLiteral.make("l3");
		Literal l4 = PosLiteral.make("l4");
		ec = make(l3, l4);
		f = f.addClause(ec);
		assertEquals(f.getSize(), 2);
		assertEquals(f.getClauses().first(), ec);
		assertEquals(f.getClauses().rest().first(), c);
		assertTrue(f.getClauses().rest().rest().isEmpty());

	}

	@Test
	public void FormulaGetTest() {
		// Case one
		Formula f = new Formula();
		assertEquals(f.getSize(), 0);
		assertTrue(f.getClauses().isEmpty());

		// Case two
		Literal l = PosLiteral.make("l");
		Clause c = make(l);
		f = new Formula(c);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c);
		assertTrue(f.getClauses().rest().isEmpty());

		// Case three
		Literal l1 = PosLiteral.make("l1");
		Literal l2 = PosLiteral.make("l2");
		Clause c1 = make(l1, l2);
		f = new Formula(c1);
		assertEquals(f.getSize(), 1);
		assertEquals(f.getClauses().first(), c1);
		assertTrue(f.getClauses().rest().isEmpty());

		Literal l3 = PosLiteral.make("l3");
		Literal l4 = PosLiteral.make("l4");
		Clause c2 = make(l3, l4);
		f = f.addClause(c2);
		assertEquals(f.getSize(), 2);
		assertEquals(f.getClauses().first(), c2);
		assertEquals(f.getClauses().rest().first(), c1);
		assertTrue(f.getClauses().rest().rest().isEmpty());
	}
	
	@Test
	public void FormulaIteratorTest() {
		//Case one
		Formula f = new Formula();
    	Iterator<Clause> iter = f.iterator();
    	assertFalse(iter.hasNext());
    	
    	//Case two
    	Literal l = PosLiteral.make("l");
    	Clause c = make(l);
    	f = new Formula(c);
    	iter = f.iterator();
    	assertTrue(iter.hasNext());
    	assertEquals(iter.next(), c);
    	assertFalse(iter.hasNext());
    	
    	//Case three
    	Literal l1 = PosLiteral.make("l1");
    	Literal l2 = PosLiteral.make("l2");
    	Literal l3 = PosLiteral.make("l3");
    	Literal l4 = PosLiteral.make("l4");
    	Clause c1 = make(l1, l2);
    	Clause c2 = make(l3, l4);
    	f = makeFormula(c1, c2);
    	
    	iter = f.iterator();
    	assertTrue(iter.hasNext());
    	assertEquals(iter.next(), c2);
    	assertTrue(iter.hasNext());
    	assertEquals(iter.next(), c1);
    	assertFalse(iter.hasNext());
	}
	
	@Test
	public void FormulaAndTest() {
		//Case one
		Formula f1 = new Formula();
    	Formula f2 = new Formula();
    	Formula f1Andf2 = f1.and(f2);
    	assertEquals(f1Andf2.getSize(), 0);
    	assertTrue(f1Andf2.getClauses().isEmpty());
    	
    	//Case two
    	f1 = new Formula();
    	Clause c1 = make(a, b);
    	f2 = new Formula(c1);
    	f1Andf2 = f1.and(f2);
    	assertEquals(f1Andf2.getSize(), 1);
    	assertEquals(f1Andf2.getClauses().first(), c1);
    	assertTrue(f1Andf2.getClauses().rest().isEmpty());
    	
    	//Case three
    	Formula f2Andf1 = f2.and(f1);
    	assertEquals(f2Andf1.getSize(), 1);
    	assertEquals(f2Andf1.getClauses().first(), c1);
    	assertTrue(f2Andf1.getClauses().rest().isEmpty());
    	
    	//Case Four
    	Clause c2 = make(c,d);
    	f1 = new Formula(c1);
    	f2 = new Formula(c2);
    	f1Andf2 = f1.and(f2);
    	assertEquals(f1Andf2.getSize(), 2);
    	assertEquals(f1Andf2.getClauses().first(), c2);
    	assertEquals(f1Andf2.getClauses().rest().first(), c1);
    	assertTrue(f1Andf2.getClauses().rest().rest().isEmpty());
	}
	
	@Test
	public void FormulaOrTest() {
		//Case one
		Formula f1 = new Formula();
    	Formula f2 = new Formula();
    	Formula f1Orf2 = f1.or(f2);
    	assertEquals(f1Orf2.getSize(), 0);
    	assertTrue(f1Orf2.getClauses().isEmpty());
    	
    	//Case two
    	f1 = new Formula();
    	Clause c1 = make(a, b);
    	f2 = new Formula(c1);
    	f1Orf2 = f1.or(f2);
    	assertEquals(f1Orf2.getSize(), 1);
    	assertEquals(f1Orf2.getClauses().first(), c1);
    	assertTrue(f1Orf2.getClauses().rest().isEmpty());
    	
    	//Case three
    	Formula f2Orf1 = f2.or(f1);
    	assertEquals(f2Orf1.getSize(), 1);
    	assertEquals(f2Orf1.getClauses().first(), c1);
    	assertTrue(f2Orf1.getClauses().rest().isEmpty());
    	
    	//Case Four
    	Clause ca = make(a);
    	Clause cb = make(b);
    	Clause cc = make(c);
    	Clause cd = make(d);
    	f1 = makeFormula(ca, cb);
    	f2 = makeFormula(cc, cd);
    	f1Orf2 = f1.or(f2);
    	
    	// (a & b) .or (c & d) == (b | c) & (a | c) & (b | d) & (a | d)
    	assertEquals(f1Orf2.getSize(), 4);
    	Clause firstClause = f1Orf2.getClauses().first();
    	Clause secondClause = f1Orf2.getClauses().rest().first();
    	Clause thirdClause = f1Orf2.getClauses().rest().rest().first();
    	Clause lastClause = f1Orf2.getClauses().rest().rest().rest().first();
    	assertEquals(firstClause, make(a, d));
    	assertEquals(secondClause, make(a, c));
    	assertEquals(thirdClause, make(b, d));
    	assertEquals(lastClause, make(b, c));
    	assertTrue(f1Orf2.getClauses().rest().rest().rest().rest().isEmpty());
	}
	
	@Test
	public void FormulaNotTest() {
		Clause aOrb = make(a, b);
    	Clause cc = make(c);
    	Formula f = makeFormula(aOrb, cc);	// f = ((a | b) & c)
    	
        // !((a | b) & c) 
        // => (!b & !a) | !c            (moving negation down to the literals)
        // => (!b | !c) & (!a | !c)     (conjunctive normal form)
    	Formula nf = f.not();
    	Clause ec1 = make(na, nc);
    	Clause ec2 = make(nb, nc);
    	assertEquals(nf.getSize(), 2);
    	assertEquals(nf.getClauses().first(), ec1);
    	assertEquals(nf.getClauses().rest().first(), ec2);
    	assertTrue(nf.getClauses().rest().rest().isEmpty());
    	
    	Clause cOrd = make(c, d);
    	f = makeFormula(aOrb, cOrd);
    	nf = f.not();
    	
    	Clause c1 = make(na, nc);
    	Clause c2 = make(na, nd);
    	Clause c3 = make(nb, nc);
    	Clause c4 = make(nb, nd);
    	
    	assertEquals(nf.getSize(), 4);
    	assertEquals(nf.getClauses().first(), c2);
    	assertEquals(nf.getClauses().rest().first(), c4);
    	assertEquals(nf.getClauses().rest().rest().first(), c1);
    	assertEquals(nf.getClauses().rest().rest().rest().first(), c3);
    	assertTrue(nf.getClauses().rest().rest().rest().rest().isEmpty());
    	
    	
	}
	
	@Test
	public void FormulaGetSizeTest() {
		//Case one
		Formula f = new Formula();
    	assertEquals(f.getSize(), 0);
    	assertTrue(f.getClauses().isEmpty());
    	
    	//Case two
    	Clause nec1 = make(a, b);
    	Clause nec2 = make(c, d);
    	Formula f1 = new Formula(nec1);
    	Formula f2 = new Formula(nec2);
    	Formula f1Andf2 = f1.and(f2);
    	assertEquals(f1Andf2.getSize(), 2);
    	assertEquals(f1Andf2.getClauses().first(), nec2);
    	assertEquals(f1Andf2.getClauses().rest().first(), nec1);
    	assertTrue(f1Andf2.getClauses().rest().rest().isEmpty());
	}
	
	@Test
	public void FormulatoStringTest() {
		//Case one
		Formula f = new Formula();		// an empty formula
    	String excepted = "Problem[]";
    	assertEquals(excepted, f.toString());
    	
    	//Case two
    	Literal l = PosLiteral.make("");// a literal
    	Clause c = new Clause(l);
    	f = new Formula(c);		// a non-empty formula	
    	excepted = "Problem[" + "\n" + c.toString() + "]";
    	assertEquals(excepted, f.toString());
    	
    	//Case three
    	Literal l1 = PosLiteral.make("l1");
    	Literal l2 = PosLiteral.make("l2");
    	c = make(l1, l2);
    	f = new Formula(c);
    	excepted = "Problem[" + "\n" + c.toString() + "]";
    	assertEquals(excepted, f.toString());
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
	
	// Helper function for constructing a clause. Takes
	// a variable number of arguments, e.g.
	// clause(a, b, c) will make the clause (a or b or c)
	// @param e,... literals in the clause
	// @return clause containing e,...
	private Clause make(Literal... e) {
		Clause c = new Clause();
		for (int i = 0; i < e.length; ++i) {
			c = c.add(e[i]);
		}
		return c;
	}
}