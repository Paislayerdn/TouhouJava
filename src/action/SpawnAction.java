package action;

public final class SpawnAction extends Action {
	private final String className;
	private final String alias;
	@Override
	public boolean consumesFrame() { return false; }

	public SpawnAction(String className, String alias) {
		this.className = className;
		this.alias = alias;
	}

	@Override
	public void start() {
		ActionTarget target = owner == null
			? SpawnRegistry.create(className, alias)
			: owner.spawn(className, alias);
		ActionContext childContext = new ActionContext(context);
		target.setActionContext(childContext);
		context.putLocal(alias, target);
		finish();
	}
}
