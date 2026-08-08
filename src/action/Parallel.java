package action;

import java.util.ArrayList;

public class Parallel extends Action {
	private ArrayList<Action> actions;
	@Override
	public boolean consumesFrame() {
		for (Action action : actions) {
			if (!action.isFinished() && action.consumesFrame()) {
				return true;
			}
		}

		return false;
	}

	public Parallel(Action... actions) {
		this.actions = new ArrayList<>();

		for (Action action : actions) {
			this.actions.add(action);
		}
	}

	@Override
	public void start() {
		for (Action action : actions) {
			action.setOwner(owner);
			action.setContext(context);
			action.start();
		}
	}


	@Override
	public void update() {
		boolean allFinished = true;

		for (Action action : actions) {
			if (!action.isFinished()) {
				action.update();
			}

			if (!action.isFinished()) {
				allFinished = false;
			}
		}

		if (allFinished) {
			finish();
		}
	}
}