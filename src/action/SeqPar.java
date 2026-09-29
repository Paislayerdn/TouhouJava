package action;

import java.util.ArrayList;

final class Sequence extends Action {
	private final ArrayList<Action> actions;
	private int currentIndex;
	private boolean consumedFrame;
	
	@Override
	public boolean consumesFrame() {
		return consumedFrame;
	}

	public Sequence(Action... actions) {
		this.actions = new ArrayList<>();
		for (Action action : actions) {
			this.actions.add(action);
		}

		currentIndex = 0;
	}

	@Override
	public void reset() {
		super.reset();
		currentIndex = 0;
		consumedFrame = false;

		for (Action action : actions) {
			action.reset();
		}
	}
	
	@Override
	public void start() {
		consumedFrame = false;
		startNextActions();
	}

	
	@Override
	public void update() {
		if (finished) return;

		Action current = actions.get(currentIndex);

		if (!current.isFinished()) {
			current.update();
		}

		consumedFrame = current.consumesFrame();

		if (!current.isFinished()) {
			return;
		}

		currentIndex++;

		if (currentIndex >= actions.size()) {
			finish();
			return;
		}

		startNextActions();
	}
	
	private void startNextActions() {
		while (!finished) {
			startCurrent();

			Action current = actions.get(currentIndex);

			if (!current.isFinished()) {
				consumedFrame = current.consumesFrame();
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
}

final class Parallel extends Action {
	private final ArrayList<Action> actions;
	private boolean consumedOnFinish;

	@Override
	public boolean consumesFrame() {
		if (finished) return consumedOnFinish;
		for (Action action : actions) {
			if (!action.isFinished() && action.consumesFrame()) return true;
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
		boolean allFinished = true;
		for (Action action : actions) {
			action.setOwner(owner);
			action.setContext(context);
			action.start();
			if (!action.isFinished()) allFinished = false;
		}
		if (allFinished) finish();
	}

	@Override
	public void update() {
		boolean allFinished = true;
		boolean consumed = false;
		for (Action action : actions) {
			if (!action.isFinished()) {
				action.update();
				if (action.isFinished() && action.consumesFrame()) consumed = true;
			}
			if (!action.isFinished()) allFinished = false;
		}
		if (allFinished) {
			consumedOnFinish = consumed;
			finish();
		}
	}
}