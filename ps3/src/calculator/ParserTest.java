package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import calculator.Lexer.TokenMismatchException;
import calculator.Parser.Value;
import calculator.Parser.ValueType;

public class ParserTest {
	
	@Test
	public void caseTestOne() throws TokenMismatchException{
		//3+2.4 = 5.4
		Parser p = new Parser(new Lexer("3+2.4"));
		Value value;
		value = p.evaluate();
		
		assertEquals(value.type, ValueType.SCALAR);
		assertEquals(value.value+"", "5.4");
		
		//1-2.4 = -1.4
		p = new Parser(new Lexer("1-2.4"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.SCALAR);
		assertEquals(value.value+"", "-1.4");
		
		//4*2.3 = 9.2
		p = new Parser(new Lexer("4*2.3"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.SCALAR);
		assertEquals(value.value+"", "9.2");
		
		//6/3=2
		p = new Parser(new Lexer("6/3"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.SCALAR);
		assertEquals(value.value+"", "2.0");
		
		//(3 + 4)*2.4 = 16.8
		p = new Parser(new Lexer("(3 + 4)*2.4"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.SCALAR);
		assertEquals(value.value+"", "16.8");
		
		//(((3 + 4))) * 2.4pt = 16.8pt
		p = new Parser(new Lexer("(((3 + 4))) * 2.4pt"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.POINTS);
		assertEquals(value.value+"", "16.8");
		
	} 
	
	
	@Test
	public void caseTestTwo() throws TokenMismatchException{
		//3 + 2.4in = 5.4in
		Parser p = new Parser(new Lexer("3 + 2.4in"));
		Value value;
		value = p.evaluate();
		
		assertEquals(value.type, ValueType.INCHES);
		assertEquals(value.value+"", "5.4");
		
		//3pt * 2.4in = 518.4pt
		p = new Parser(new Lexer("3pt * 2.4in"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.POINTS);
		assertEquals(value.value+"", "518.4");
		
		//3in * 2.4 = 7.2in
		p = new Parser(new Lexer("3in * 2.4"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.INCHES);
		assertEquals(value.value+"", "7.2");
	}
	
	@Test
	public void caseTestThree() throws TokenMismatchException{
		//4pt+(3in*2.4) = 522.4pt
		Parser p = new Parser(new Lexer("4pt+(3in*2.4)"));
		Value value;
		value = p.evaluate();
		
		assertEquals(value.type, ValueType.POINTS);
		assertEquals(value.value+"", "522.4");
		//4pt+((3in*2.4)) = 522.4pt
		p = new Parser(new Lexer("4pt+((3in*2.4))"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.POINTS);
		assertEquals(value.value+"", "522.4");
		
		//(3 + 2.4) in = 5.4in
		p = new Parser(new Lexer("(3 + 2.4) in"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.INCHES);
		assertEquals(value.value+"", "5.4");
		
		//(3in * 2.4) pt = 518.4pt
		p = new Parser(new Lexer("(3in * 2.4) pt"));
		value = p.evaluate();
		assertEquals(value.type, ValueType.POINTS);
		assertEquals(value.value+"", "518.4");
	}
}
