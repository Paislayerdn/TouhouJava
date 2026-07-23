package action;

import java.util.ArrayList;
import entity.Entity;


public class ActionRunner {
	private final ArrayList<Action> actions;

	public ActionRunner() {
		actions = new ArrayList<>();
	}

	public void add(Action action, Entity owner) {
		action.setOwner(owner);

		// Instant initialization
		action.start();

		actions.add(action);
	}


	public void update() {
		for (Action action : actions) {
			if (!action.isFinished()) {
				action.update();
			}
		}

		actions.removeIf(Action::isFinished);
	}


	public void clear() {
		actions.clear();
	}
}