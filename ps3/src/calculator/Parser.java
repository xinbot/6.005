package calculator;

import java.util.Stack;

import calculator.Lexer.Token;
import calculator.Lexer.TokenMismatchException;

/*
 * Expression  ::= (primitive | combination) EOF
 * Primitive   ::= number (unit | )
 * Combination ::= arithmetic | unit_conversion | left_paren expression right_paren
 * Unit_Conversion   ::= left_paren expression right_paren unit
 * Arithmetic  ::= expression operator expression
 * Operator    ::= [+|-|*|/]
 * Number      ::= [0-9]+[[.[0-9]+]|[]]
 * Unit		   ::= [pt|in]
 * Left_Paren  ::= [(]
 * Right_Paren ::= [)]
 */


/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {

	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
		ParserException(String msg) {
			super(msg);
		}
	}

	/**
	 * Type of values.
	 */
	public enum ValueType {
		POINTS, INCHES, SCALAR
	};

	/**
	 * Internal value is always in points.
	 */
	public class Value {
		final double value;
		final ValueType type;

		Value(double value, ValueType type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
			case INCHES:
				return value / PT_PER_IN + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
	}

	private static final double PT_PER_IN = 72;
	private final Lexer lexer;

	/**
	 * Generate a parser
	 * 
	 * @param a lexer use to generate tokenized symbol
	 * */
	Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	/**
	 * Evaluates the calculator production of the grammar. Modifies lexer by
	 * consuming all the remaining tokens.
	 * 
	 * @return evaluated result value
	 * @throws TokenMismatchException
	 */
	public Value evaluate() throws TokenMismatchException {
		// infix to postfix
		Stack<Value> num = new Stack<Value>();
		Stack<Token> op = new Stack<Token>();

		while (true) {
			Token tok = lexer.next();

			switch (tok.type) {
			case NUMBER:
				num.push(new Value(Double.parseDouble(tok.text),
						ValueType.SCALAR));
				break;

			case INCH: // regard inch as an special operator
				Value v_inch = num.pop();
				if (v_inch.type == ValueType.SCALAR) {
					num.push(new Value(v_inch.value, ValueType.INCHES));
				} else if (v_inch.type == ValueType.POINTS) {
					double newValue = v_inch.value / PT_PER_IN;
					num.push(new Value(newValue, ValueType.INCHES));
				}
				break;

			case POINT:// regard point as an special operator
				Value v_point = num.pop();
				if (v_point.type == ValueType.SCALAR) {
					num.push(new Value(v_point.value, ValueType.POINTS));
				} else if (v_point.type == ValueType.INCHES) {
					double newValue = v_point.value * PT_PER_IN;
					num.push(new Value(newValue, ValueType.POINTS));
				}
				break;

			case PLUS:
				op.push(tok);
				break;

			case MINUS:
				op.push(tok);
				break;

			case MULTIPLY:
				op.push(tok);
				break;

			case DIVIDE:
				op.push(tok);
				break;

			case LEFT_PAREN:
				op.push(tok);
				break;

			case RIGHT_PAREN:
				Token t1 = op.pop();
                while (t1.type != Type.LEFT_PAREN) {
                	Value value2 = num.pop();
    				Value value1 = num.pop();
    				Value result = operate(value1, value2, t1.type);
    				num.push(result);
    				t1 = op.pop();
                }
				break;

			case EOF:
				while (!op.empty()) {
                    Token t2 = op.pop();
                    if (t2.type != Type.LEFT_PAREN && t2.type != Type.RIGHT_PAREN) {
                    	Value value2 = num.pop();
        				Value value1 = num.pop();
        				Value result = operate(value1, value2, t2.type);
        				num.push(result);
                    }
                }
				return num.pop();

			default:
				throw new ParserException("Token (" + tok.text + " " + tok.type
						+ ") can not be recognized.");
			}// end switch
		}// end while

	}
	
	/*
	 * inches/scalar = inches
	 * inches*scalar = inches
	 * inches/inches = scalar
	 * inches/points = scalar
	 * scalar/inches = inches
	 * inches*inches = inches
	 * scalar+inches = inches
	 * inches+points = inches (use units of the first operand)
	 * 
	 **/
	
	/**
	 * operate on value1 and value2 according to operator.
	 * 
	 * @param value1
	 *            : the first operand, value1.value measured in 'pt' or 'in'.
	 * @param value2
	 *            : the second operand, value2.value measured in 'pt' or 'in'.
	 * @param operator
	 *            : the operator type, should be one of +, -, *, /
	 * @return Value representing the result of the operation.
	 */
	private Value operate(Value value1, Value value2, Type operator) {
		Value result;
		ValueType resultType;
		
		if (value1.type == ValueType.SCALAR) { // use units of the first operand
			resultType = value2.type;		   // when one of them is scalar
		} else {
			resultType = value1.type;
		}
		
		switch (operator) {
		case PLUS:
			if (value1.type == ValueType.INCHES
					&& value2.type == ValueType.POINTS) {
				result = new Value(value1.value + (value2.value / PT_PER_IN), ValueType.INCHES);
			} else if (value1.type == ValueType.POINTS
					&& value2.type == ValueType.INCHES) {
				result = new Value(value1.value + (value2.value* PT_PER_IN), ValueType.POINTS);
			}else{
				result = new Value(value1.value + value2.value, resultType);
			}
			break;
			
		case MINUS:
			if (value1.type == ValueType.INCHES
					&& value2.type == ValueType.POINTS) {
				result = new Value(value1.value - (value2.value / PT_PER_IN), ValueType.INCHES);
			} else if (value1.type == ValueType.POINTS
					&& value2.type == ValueType.INCHES) {
				result = new Value(value1.value - (value2.value* PT_PER_IN), ValueType.POINTS);
			}else{
				result = new Value(value1.value - value2.value, resultType);
			}
			break;
			
		case MULTIPLY:
			if (value1.type == ValueType.INCHES
					&& value2.type == ValueType.POINTS) {
				result = new Value(value1.value * (value2.value / PT_PER_IN), ValueType.INCHES);
			} else if (value1.type == ValueType.POINTS
					&& value2.type == ValueType.INCHES) {
				result = new Value(value1.value * (value2.value* PT_PER_IN), ValueType.POINTS);
			}else{
				result = new Value(value1.value * value2.value, resultType);
			}
			break;
		case DIVIDE:
			if (value1.type == ValueType.INCHES
					&& value2.type == ValueType.POINTS) {
				result = new Value(value1.value / (value2.value / PT_PER_IN), ValueType.SCALAR);
			}else if (value1.type == ValueType.POINTS
					&& value2.type == ValueType.INCHES){
				result = new Value(value1.value / (value2.value* PT_PER_IN), ValueType.SCALAR);
			}else if (value1.type == value2.type){
				result = new Value(value1.value / value2.value, ValueType.SCALAR);
			}else{
				result = new Value(value1.value / value2.value, resultType);
			}
			break;
		default:
			throw new ParserException("Operator can not be recognized.");
		}
		return result;
	}

}
