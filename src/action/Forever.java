package action;

public final class Forever extends Action {
	private Action action;
	private boolean consumedFrame;

	@Override
	public boolean consumesFrame() {
		return consumedFrame;
	}

	public Forever(Action action) {
		this.action = action;
	}

	@Override
	public void start() {
		consumedFrame = false;

		action.setOwner(owner);
		action.setContext(context);
		action.start();

		consumedFrame = action.consumesFrame();
	}

	@Override
	public void update() {
		while (true) {
			if (action.isFinished()) {
				action.reset();
				action.setOwner(owner);
				action.setContext(context);
				action.start();

				consumedFrame = action.consumesFrame();

				if (consumedFrame) {
					return;
				}
			}

			action.update();

			consumedFrame = action.consumesFrame();

			if (!action.isFinished() || consumedFrame) {
				return;
			}
		}
	}
}