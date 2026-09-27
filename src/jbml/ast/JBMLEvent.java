package jbml.ast;

import java.util.ArrayList;
import java.util.List;

public final class JBMLEvent {
	public final String name;
	public final List<JBMLCommand> commands = new ArrayList<>();

	public JBMLEvent(String name) {
		this.name = name;
	}
}