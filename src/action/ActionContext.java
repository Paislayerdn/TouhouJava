package action;

import main.Debug;

import java.util.Map;
import java.util.HashMap;

public final class ActionContext {
	private final Map<String, Object> variables;
	private final ActionContext parent;

	public ActionContext() {
		this(null);
	}
	public ActionContext(ActionContext parent) {
		this.parent = parent;
		this.variables = new HashMap<>();
	}

	// Find the context where this variable was declared.
	private ActionContext findContext(String name) {
		if (variables.containsKey(name)) { return this; }

		if (parent != null) { return parent.findContext(name); }
		return null;
	}
	
	// Declare a variable in THIS context, without conflicting the upper contexts.
	public void declare(String name, Object value) {
		if (findContext(name) != null) {
			throw Debug.terminate("JScratch", this, "Variable already declared: " + name);
		}

		variables.put(name, value);
	}
	// Get a variable from this context or parent upwards.
	public Object get(String name) {
		ActionContext context = findContext(name);

		if (context == null) {
			throw Debug.terminate("JScratch", this, "Getting an undeclared variable: " + name);
		}

		return context.variables.get(name);
	}
	// Change an existing variable.
	public void set(String name, Object value) {
		ActionContext context = findContext(name);

		if (context == null) {
			throw Debug.terminate("JScratch", this, "Setting an undeclared variable: " + name);
		}

		context.variables.put(name, value);
	}

	public boolean has(String name) {
		return findContext(name) != null;
	}
}