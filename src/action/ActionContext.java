package action;

import java.util.HashMap;

public class ActionContext {
	private final HashMap<String, Object> variables;
	private final ActionContext parent;

	public ActionContext() {
		this(null);
	}

	public ActionContext(ActionContext parent) {
		this.parent = parent;
		this.variables = new HashMap<>();
	}

	// Declare a variable in THIS context.
	public void declare(String name, Object value) {
		variables.put(name, value);
	}

	// Find the context where this variable was declared.
	private ActionContext findContext(String name) {
		if (variables.containsKey(name)) {
			return this;
		}

		if (parent != null) {
			return parent.findContext(name);
		}

		return null;
	}

	// Get a variable from this context or any parent.
	public Object get(String name) {
		ActionContext context = findContext(name);

		if (context == null) {
			throw new IllegalArgumentException(
				"[JScratch ActionContext] Variable not declared: " + name
			);
		}

		return context.variables.get(name);
	}

	// Change an existing variable.
	public void set(String name, Object value) {
		ActionContext context = findContext(name);

		if (context == null) {
			throw new IllegalArgumentException(
				"[JScratch ActionContext] Variable not declared: " + name
			);
		}

		context.variables.put(name, value);
	}

	public boolean has(String name) {
		return findContext(name) != null;
	}
	
	public ActionContext snapshot() {
		ActionContext snapshot = new ActionContext();
		copyVariables(snapshot);
		return snapshot;
	}

	private void copyVariables(ActionContext target) {
		if (parent != null) { parent.copyVariables(target); }

		for (var entry : variables.entrySet()) {
			target.declare(entry.getKey(), entry.getValue());
		}
	}
}