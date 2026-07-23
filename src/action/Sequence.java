package action;

import java.util.ArrayList;

public class Sequence extends Action {
	private ArrayList<Action> actions;
	private int currentIndex;

	public Sequence(Action... actions) {
		this.actions = new ArrayList<>();
		for(Action action : actions) {
			this.actions.add(action);
		}

		currentIndex = 0;
	}


	@Override
	public void start() {
		if(actions.isEmpty()) {
			finish();
			return;
		}


		startCurrent();
	}


	private void startCurrent() {
		Action current = actions.get(currentIndex);

		current.setOwner(owner);
		current.start();
	}


	@Override
	public void update() {
		if(finished)
			return;

		Action current = actions.get(currentIndex);


		if(!current.isFinished()) {
			current.update();
		}

		if(current.isFinished()) {
			currentIndex++;

			if(currentIndex >= actions.size()) {
				finish();
			}
			else {
				startCurrent();
			}
		}
	}
}