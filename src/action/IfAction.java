package action;

public class IfAction extends Action {
	private final Object condition;
	private final ActionFactory thenFactory;
	private final ActionFactory elseFactory;

	private Action action;

	public IfAction(Object condition,
		ActionFactory thenFactory
	) {
		this(condition, thenFactory, null);
	}

	public IfAction(Object condition,
		ActionFactory thenFactory,
		ActionFactory elseFactory
	) {
		this.condition = condition;
		this.thenFactory = thenFactory;
		this.elseFactory = elseFactory;
	}

	@Override
	public boolean consumesFrame() {
		if (finished) return false;
		return action != null && action.consumesFrame();
	}

	@Override
	public void start() {
		boolean result = LogicValue.toBoolean(resolve(condition), this);

		if (result) {
			action = thenFactory.create();
		} else if (elseFactory != null) {
			action = elseFactory.create();
		} else {
			finish();
			return;
		}

		action.setOwner(owner);
		action.setContext(context);
		action.start();

		if (action.isFinished()) {
			finish();
		}
	}

	@Override
	public void update() {
		if (finished) return;

		if (!action.isFinished()) {
			action.update();
		}

		if (action.isFinished()) {
			finish();
		}
	}
}