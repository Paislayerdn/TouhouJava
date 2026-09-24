package action;

import java.util.ArrayList;

import entity.Thing;
import java.util.HashMap;

public final class ActionRunner {
	private final ActionContext context;
	private final ArrayList<Action> actions;
	private final HashMap<String, TweenAction> tweens;

	public ActionRunner() {
		actions = new ArrayList<>();
		tweens = new HashMap<>();
		context = new ActionContext();
	}
	public ActionRunner(ActionContext context) {
		actions = new ArrayList<>();
		tweens = new HashMap<>();
		this.context = context;
	}
	
	public ActionContext getContext() { return context; }

	public void add(Action action) {
		add(action, null);
	}
	public void add(Action action, Thing owner) {
		action.setOwner(owner);
		action.setContext(context);

		if (action instanceof TweenAction tween) {
			registerTween(tween);
		}

		actions.add(action);
		action.start();
	}
	private void registerTween(TweenAction tween) {
		String property = tween.getPropertyName();

		TweenAction old = tweens.put(property, tween);

		if (old != null && old != tween) {
			old.finish();
		}
	}

	public void update() {
		ArrayList<Action> snapshot = new ArrayList<>(actions);

		for (Action action : snapshot) {
			if (!action.isFinished()) {
				action.update();
			}
		}

		actions.removeIf(Action::isFinished);

		tweens.entrySet().removeIf(entry -> entry.getValue().isFinished());
	}

	public void clear() {
		actions.clear();
		tweens.clear();
	}
}