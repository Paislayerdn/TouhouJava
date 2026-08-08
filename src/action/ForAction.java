package action;

public class ForAction extends Action {
	private final String variable;
	private final Object start;
	private final Object end;
	private final ActionFactory factory;
	private Action action;
	@Override
	public boolean consumesFrame() {
		if (finished) return false;
		return action != null && action.consumesFrame();
	}

	private double current;
	private double target;
	private double step;

	public ForAction(String variable, Object start, Object end, ActionFactory factory) {
		this.variable = variable;
		this.start = start;
		this.end = end;
		this.factory = factory;
	}

	@Override
	public void start() {
		current = resolveDouble(start);
		target = resolveDouble(end);

		step = current <= target ? 1 : -1;

		getContext().declare(variable, current);
		
		action = factory.create();
		action.setOwner(owner);
		action.setContext(getContext());
		action.start();
	}

	@Override
	public void update() {
		while (!finished) {
			boolean wasFinished = action.isFinished();

			if (!wasFinished) { action.update(); }
			if (!action.isFinished()) { return; }
			if (!wasFinished && action.consumesFrame()) { return; }

			current += step;
			if ((step > 0 && current > target) || (step < 0 && current < target)) {
				finish();
				return;
			}

			getContext().set(variable, current);

			action = factory.create();
			action.setOwner(owner);
			action.setContext(getContext());
			action.start();
		}
	}
}