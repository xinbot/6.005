package calculator;

/**
 * Calculator lexical analyzer.
 */
public class Lexer {

	private String s;
	private int i = 0;
	// s is the string of calculator text that we're parsing,
	// and s[i] is the start of the next token to return, or
	// i == s.length means we're at the end of parsing.

	/**
	 * Token in the stream.
	 */
	public static class Token {
		final Type type;
		final String text;

		Token(Type type, String text) {
			this.type = type;
			this.text = text;
		}

		Token(Type type) {
			this(type, null);
		}
	}

	@SuppressWarnings("serial")
	static class TokenMismatchException extends Exception {
		public TokenMismatchException(String message) {
			super(message);
		}
	}

	/**
	 * Generate a Lexer
	 * @param string convert to tokens(require not null)
	 * */
	public Lexer(String input) {
		this.s = input;
	}

	/**
	 * Consuming a token one at a time from the input stream.
	 * @return next token on the stream, or EOF token if there are no more
	 *         tokens in the stream.
	 * @throws TokenMismatchException
	 */
	public Token next() throws TokenMismatchException {
		if (i >= s.length()) {
			return new Token(Type.EOF, "");
		}

		if (Character.isDigit(s.charAt(i))) {
			int start = i;
			while ((i < s.length())
					&& ((Character.isDigit(s.charAt(i))) || (s.charAt(i) == '.'))) {
				i = i + 1;
			}
			int end = i;
			return new Token(Type.NUMBER, s.substring(start, end));
		} else {
			switch (s.charAt(i)) {
			// white space, tab, consume it
			case '\t':
			case ' ':
				i = i + 1;
				return next();

			case 'p':
				if ((i < (s.length() - 1)) && (s.charAt(i + 1) == 't')) {
					i = i + 2;
					return new Token(Type.POINT, "pt");
				} else {
					throw new TokenMismatchException("Unrecognizable token at"
							+ s.substring(i));
				}

			case 'i':
				if ((i < (s.length() - 1)) && (s.charAt(i + 1) == 'n')) {
					i = i + 2;
					return new Token(Type.INCH, "in");
				} else {
					throw new TokenMismatchException("Unrecognizable token at"
							+ s.substring(i));
				}

			case '+':
				i = i + 1;
				return new Token(Type.PLUS, "+");

			case '-':
				i = i + 1;
				return new Token(Type.MINUS, "-");

			case '*':
				i = i + 1;
				return new Token(Type.MULTIPLY, "*");

			case '/':
				i = i + 1;
				return new Token(Type.DIVIDE, "/");

			case '(':
				i = i + 1;
				return new Token(Type.LEFT_PAREN, "(");

			case ')':
				i = i + 1;
				return new Token(Type.RIGHT_PAREN, ")");
			default:
				throw new TokenMismatchException("No token found at "
						+ s.substring(i));
			}// end switch

		}
	}
}
