package action;

public final class BestowAction extends Action {
	private final String targetAlias;
	private final ActionFactory factory;
	@Override
	public boolean consumesFrame() { return false; }

	public BestowAction(String targetAlias, ActionFactory factory) {
		this.targetAlias = targetAlias;
		this.factory = factory;
	}

	@Override
	public void start() {
		Object value = context.get(targetAlias);
		if (!(value instanceof ActionTarget target)) {
			throw new IllegalArgumentException("'" + targetAlias + "' does not name an ActionTarget");
		}
		Action action = factory.create();
		target.getActionRunner().add(action, target);
		SpawnRegistry.activate(target);
		finish();
	}
}
