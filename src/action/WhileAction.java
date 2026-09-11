package action;

public class WhileAction extends Action {
	private final Object condition;
	private final ActionFactory factory;

	private Action action;

	public WhileAction(Object condition, ActionFactory factory) {
		this.condition = condition;
		this.factory = factory;
	}

	@Override
	public boolean consumesFrame() {
		if (finished) return false;
		return action != null && action.consumesFrame();
	}

	@Override
	public void start() {
		action = null;
	}

	private void startNextIteration() {
		boolean result = LogicValue.toBoolean(resolve(condition), this);

		if (!result) {
			finish();
			return;
		}

		action = factory.create();
		action.setOwner(owner);
		action.setContext(context);
		action.start();
	}

	@Override
	public void update() {
		if (finished) return;

		while (!finished) {
			if (action == null) {
				startNextIteration();
				if (finished) return;
			}

			if (!action.isFinished()) { action.update(); }
			if (!action.isFinished()) { return; }

			boolean consumed = action.consumesFrame();
			action = null;
			if (consumed) { return; }
		}
	}
}