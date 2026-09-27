package jbml;

public final class JBMLToken {
	public final JBMLTokenType type;
	public final String text;
	public final int line;

	public JBMLToken(JBMLTokenType type, String text, int line) {
		this.type = type;
		this.text = text;
		this.line = line;
	}

	@Override
	public String toString() {
		return type + "('" + text + "') at line " + line;
	}
}