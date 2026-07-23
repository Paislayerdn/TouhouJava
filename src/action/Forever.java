package action;

public class Forever extends Action {
	private Action action;

	public Forever(Action action) {
		this.action = action;
	}

	@Override
	public void start() {
		action.setOwner(owner);
		action.start();
	}

	@Override
	public void update() {
		if(action.isFinished()) {
			action.reset();
			action.start();
		}

		action.update();
	}
}