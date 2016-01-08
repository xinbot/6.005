package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import calculator.Lexer.Token;
import calculator.Lexer.TokenMismatchException;

public class LexerTest {
	
	@Test
	public void basicOperatorTest() throws TokenMismatchException{
		Lexer lex = new Lexer("+-*/");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.PLUS);
		assertEquals(tok.text, "+");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MINUS);
		assertEquals(tok.text, "-");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MULTIPLY);
		assertEquals(tok.text, "*");
		
		tok = lex.next();
		assertEquals(tok.type, Type.DIVIDE);
		assertEquals(tok.text, "/");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void basicNumberTest() throws TokenMismatchException{
		Lexer lex = new Lexer("102");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "102");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
        
        lex = new Lexer("1.732");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "1.732");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void basicUnitTest() throws TokenMismatchException{
		Lexer lex = new Lexer("pt");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
        
        lex = new Lexer("in");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void basicLeft_parnTest() throws TokenMismatchException{
		Lexer lex = new Lexer("(");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void basicRight_parnTest() throws TokenMismatchException{
		Lexer lex = new Lexer(")");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestOne() throws TokenMismatchException{
		Lexer lex = new Lexer("7.14+286pt");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "7.14");
		
		tok = lex.next();
		assertEquals(tok.type, Type.PLUS);
		assertEquals(tok.text, "+");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "286");
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestTwo() throws TokenMismatchException{
		Lexer lex = new Lexer("8.33in + 915");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "8.33");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
		assertEquals(tok.type, Type.PLUS);
		assertEquals(tok.text, "+");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "915");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);		
	}
	
	@Test
	public void caseTestThree() throws TokenMismatchException{
		Lexer lex = new Lexer("478.66-368pt");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "478.66");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MINUS);
		assertEquals(tok.text, "-");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "368");
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestFour() throws TokenMismatchException{
		Lexer lex = new Lexer("2.33in - 165");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "2.33");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MINUS);
		assertEquals(tok.text, "-");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "165");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestFive() throws TokenMismatchException{
		Lexer lex = new Lexer("256.88*602pt");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "256.88");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MULTIPLY);
		assertEquals(tok.text, "*");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "602");
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestSix() throws TokenMismatchException{
		Lexer lex = new Lexer("5.24in * 255");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "5.24");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MULTIPLY);
		assertEquals(tok.text, "*");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "255");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestSeven() throws TokenMismatchException {
		Lexer lex = new Lexer("667.20pt/105.6");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "667.20");

		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
		assertEquals(tok.type, Type.DIVIDE);
		assertEquals(tok.text, "/");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "105.6");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestEight() throws TokenMismatchException {
		Lexer lex = new Lexer("388/102.6in");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "388");
		
		tok = lex.next();
		assertEquals(tok.type, Type.DIVIDE);
		assertEquals(tok.text, "/");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "102.6");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestNine() throws TokenMismatchException {
		Lexer lex = new Lexer("4.14pt+(3in*2.4)");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "4.14");
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
		assertEquals(tok.type, Type.PLUS);
		assertEquals(tok.text, "+");
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "3");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MULTIPLY);
		assertEquals(tok.text, "*");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "2.4");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestTen() throws TokenMismatchException {
		Lexer lex = new Lexer("(((8.88pt*2.99)))-100");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "8.88");

		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MULTIPLY);
		assertEquals(tok.text, "*");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "2.99");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MINUS);
		assertEquals(tok.text, "-");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "100");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestEleven() throws TokenMismatchException{
		Lexer lex = new Lexer("(3 + 2.4) in");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "3");
		
		tok = lex.next();
		assertEquals(tok.type, Type.PLUS);
		assertEquals(tok.text, "+");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "2.4");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
	
	@Test
	public void caseTestTwelve() throws TokenMismatchException{
		Lexer lex = new Lexer("(8.16in + (3.33pt * 4.64)) pt");
		Token tok;
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "8.16");
		
		tok = lex.next();
		assertEquals(tok.type, Type.INCH);
		assertEquals(tok.text, "in");
		
		tok = lex.next();
		assertEquals(tok.type, Type.PLUS);
		assertEquals(tok.text, "+");
		
		tok = lex.next();
		assertEquals(tok.type, Type.LEFT_PAREN);
		assertEquals(tok.text, "(");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "3.33");
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
		assertEquals(tok.type, Type.MULTIPLY);
		assertEquals(tok.text, "*");
		
		tok = lex.next();
		assertEquals(tok.type, Type.NUMBER);
		assertEquals(tok.text, "4.64");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
		assertEquals(tok.type, Type.RIGHT_PAREN);
		assertEquals(tok.text, ")");
		
		tok = lex.next();
		assertEquals(tok.type, Type.POINT);
		assertEquals(tok.text, "pt");
		
		tok = lex.next();
        assertEquals(tok.type, Type.EOF);
	}
}
