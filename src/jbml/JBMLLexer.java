package jbml;

import java.util.ArrayList;
import java.util.List;

public final class JBMLLexer {
	private final String source;
	private int index;
	private int line;

	public JBMLLexer(String source) {
		this.source = source;
		this.index = 0;
		this.line = 1;
	}

	public List<JBMLToken> tokenize() {
		List<JBMLToken> tokens = new ArrayList<>();

		while (!isAtEnd()) {
			char c = peek();

			if (c == '\n') {
				advance();
				line++;
				continue;
			}

			if (Character.isWhitespace(c)) {
				advance();
				continue;
			}

			if (isNumberStart()) {
				tokens.add(readNumber());
				continue;
			}
			
			if (c == '#' || c == '/' || c == '-') {
				skipComment();
				continue;
			}

			if (c == '"') {
				tokens.add(readString());
				continue;
			}

			if (Character.isLetter(c)) {
				tokens.add(readWord());
				continue;
			}

			error();
		}

		tokens.add(new JBMLToken(JBMLTokenType.EOF, "", line));
		return tokens;
	}

	private JBMLToken readWord() {
		int start = index;
		int tokenLine = line;

		while (!isAtEnd() && Character.isLetterOrDigit(peek())) {
			advance();
		}

		String text = source.substring(start, index);
		JBMLTokenType type = keyword(text);

		return new JBMLToken(type, text, tokenLine);
	}

	private JBMLToken readString() {
		int tokenLine = line;
		advance(); // Opening quote.

		StringBuilder text = new StringBuilder();

		while (!isAtEnd()) {
			char c = peek();

			if (c == '"') {
				advance(); // Closing quote.
				return new JBMLToken(
					JBMLTokenType.STRING,
					text.toString(),
					tokenLine
				);
			}

			if (c == '\n') {
				error();
			}

			text.append(c);
			advance();
		}

		error();
		return null; // Unreachable.
	}

	private JBMLToken readNumber() {
		int start = index;
		int tokenLine = line;

		if (peek() == '-') {
			advance();
		}

		while (!isAtEnd() && Character.isDigit(peek())) {
			advance();
		}

		if (!isAtEnd() && peek() == '.') {
			advance();

			while (!isAtEnd() && Character.isDigit(peek())) {
				advance();
			}
		}

		String text = source.substring(start, index);

		return new JBMLToken(
			JBMLTokenType.NUMBER,
			text,
			tokenLine
		);
	}

	private boolean isNumberStart() {
		if (Character.isDigit(peek())) {
			return true;
		}

		return peek() == '-'
			&& index + 1 < source.length()
			&& Character.isDigit(source.charAt(index + 1));
	}

	private void skipComment() {
		while (!isAtEnd() && peek() != '\n') {
			advance();
		}
	}

	private JBMLTokenType keyword(String text) {
		return switch (text.toUpperCase()) {
			case "SCENE" -> JBMLTokenType.SCENE;
			case "WORLD" -> JBMLTokenType.WORLD;
			case "SKY" -> JBMLTokenType.SKY;
			case "EVENT" -> JBMLTokenType.EVENT;
			case "END" -> JBMLTokenType.END;

			case "CAMERA" -> JBMLTokenType.CAMERA;
			case "OBJECT" -> JBMLTokenType.OBJECT;
			case "PLANE" -> JBMLTokenType.PLANE;

			case "SET" -> JBMLTokenType.SET;
			case "CHANGE" -> JBMLTokenType.CHANGE;
			case "TWEEN" -> JBMLTokenType.TWEEN;
			case "CALL" -> JBMLTokenType.CALL;
			case "FOREVER" -> JBMLTokenType.FOREVER;
			case "WAIT" -> JBMLTokenType.WAIT;

			case "TRUE" -> JBMLTokenType.TRUE;
			case "FALSE" -> JBMLTokenType.FALSE;

			default -> JBMLTokenType.IDENTIFIER;
		};
	}

	private char peek() {
		return source.charAt(index);
	}

	private void advance() {
		index++;
	}

	private boolean isAtEnd() {
		return index >= source.length();
	}

	private void error() {
		throw new RuntimeException(
			"JBML error on line " + line + ". Fuck you."
		);
	}
}