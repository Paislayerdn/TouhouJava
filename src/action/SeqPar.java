package action;

import java.util.ArrayList;

final class Sequence extends Action {
	private final ArrayList<Action> actions;
	private int currentIndex;
	@Override
	public boolean consumesFrame() {
		if (finished) return false;
		return actions.get(currentIndex).consumesFrame();
	}

	public Sequence(Action... actions) {
		this.actions = new ArrayList<>();
		for (Action action : actions) {
			this.actions.add(action);
		}

		currentIndex = 0;
	}

	@Override
	public void start() {
		if (actions.isEmpty()) {
			finish();
			return;
		} while (!finished) {
			startCurrent();

			Action current = actions.get(currentIndex);

			if (!current.isFinished()) {
				return;
			}

			currentIndex++;

			if (currentIndex >= actions.size()) {
				finish();
				return;
			}
		}
	}

	private void startCurrent() {
		Action current = actions.get(currentIndex);

		current.setOwner(owner);
		current.setContext(context);
		current.start();
	}
	
	@Override
	public void reset() {
		super.reset();
		currentIndex = 0;

		for (Action action : actions) {
			action.reset();
		}
	}

	@Override
	public void update() {
		if (finished) return;

		while (!finished) {
			Action current = actions.get(currentIndex);
			
			boolean wasFinished = current.isFinished();

			if (!wasFinished) { current.update(); }
			if (!current.isFinished()) { return; }

			// Only yield if this action actually ran this frame.
			if (!wasFinished && current.consumesFrame()) { return; }

			currentIndex++;
			if (currentIndex >= actions.size()) {
				finish();
				return;
			}

			startCurrent();
		}
	}
}

final class Parallel extends Action {
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