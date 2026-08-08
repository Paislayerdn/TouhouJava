package action;

import java.util.ArrayList;

public class Sequence extends Action {
	private ArrayList<Action> actions;
	private int currentIndex;

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
		}

		startCurrent();
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