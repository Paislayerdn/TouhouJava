package jbml.ast;

public final class JBMLSet implements JBMLCommand {
	public final String target;
	public final String property;
	public final Object value;

	public JBMLSet(
		String target,
		String property,
		Object value
	) {
		this.target = target;
		this.property = property;
		this.value = value;
	}
}