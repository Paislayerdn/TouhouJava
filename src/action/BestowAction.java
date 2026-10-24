package action;

import entity.Bullet;
import state.gameplay.BulletManager;

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
		BulletManager bulletManager = null;
		if (target instanceof Bullet) {
			if (!context.has("bullets")) throw new IllegalStateException(
				"Missing required 'bullets' variable in action context");
			Object managerValue = context.get("bullets");
			if (!(managerValue instanceof BulletManager manager)) throw new IllegalStateException(
				"Action context variable 'bullets' must be a BulletManager");
			bulletManager = manager;
		}
		Action action = factory.create();
		target.getActionRunner().add(action, target);
		SpawnRegistry.activate(target, bulletManager);
		finish();
	}
}
