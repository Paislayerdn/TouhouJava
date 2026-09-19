package action;

import java.util.ArrayList;

import entity.Thing;

public final class ActionRunner {
	private ActionContext context;
	private final ArrayList<Action> actions;

	public ActionRunner() {
		this.actions = new ArrayList<>();
		this.context = new ActionContext();
	}
	public ActionRunner(ActionContext context) {
		this.actions = new ArrayList<>();
		this.context = context;
	}	
	public ActionContext getContext() { return context; }

	public void add(Action action) {
		add(action, null);
	}
	public void add(Action action, Thing owner) {
		action.setOwner(owner);
		action.setContext(context);
		actions.add(action);
		
		action.start();
	}

	public void update() {
		ArrayList<Action> snapshot = new ArrayList<>(actions);

		for (Action action : snapshot) {
			if (!action.isFinished()) {
				action.update();
			}
		}

		actions.removeIf(Action::isFinished);
	}

	public void clear() { actions.clear(); }
}