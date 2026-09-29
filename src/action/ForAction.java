package action;

public final class ForAction extends Action {
	private final String variable;
	private final Object start;
	private final Object end;
	private final ActionFactory factory;

	private Action action;
	private ActionContext loopContext;
	private boolean consumedFrame;

	@Override
	public boolean consumesFrame() { return consumedFrame; }

	private float current;
	private float target;
	private float step;

	public ForAction(String variable, Object start, Object end, ActionFactory factory) {
		this.variable = variable;
		this.start = start;
		this.end = end;
		this.factory = factory;
	}

	@Override
	public void start() {
		consumedFrame = false;

		current = resolveFloat(start);
		target = resolveFloat(end);

		step = current <= target ? 1 : -1;

		loopContext = new ActionContext(getContext());
		loopContext.declare(variable, current);

		action = factory.create();
		action.setOwner(owner);
		action.setContext(loopContext);
		action.start();

		consumedFrame = action.consumesFrame();
	}

	@Override
	public void update() {
		while (true) {
			if (!action.isFinished()) {
				action.update();

				if (!action.isFinished()) {
					consumedFrame = action.consumesFrame();
					return;
				}

				// The child finished during this update.
				// Its consumption belongs to the frame it just completed.
				// For is allowed to continue immediately.
				consumedFrame = false;
			}

			current += step;

			if ((step > 0 && current > target)
					|| (step < 0 && current < target)) {
				finish();
				return;
			}

			loopContext.set(variable, current);

			action = factory.create();
			action.setOwner(owner);
			action.setContext(loopContext);
			action.start();

			consumedFrame = action.consumesFrame();

			if (consumedFrame) {
				return;
			}
		}
	}
}