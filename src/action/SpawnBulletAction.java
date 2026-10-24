package action;

import entity.Bullet;
import state.gameplay.BulletManager;

public final class SpawnBulletAction extends Action {
	@Override
	public boolean consumesFrame() { return false; }
	private final Action bulletAction;

	public SpawnBulletAction(Action bulletAction) {
		this.bulletAction = bulletAction;
	}

	@Override
	public void start() {
		if (!getContext().has("bullets")) {
			throw new IllegalStateException("Missing required 'bullets' variable in action context");
		}
		Object managerValue = getContext().get("bullets");
		if (!(managerValue instanceof BulletManager bulletManager)) {
			throw new IllegalStateException("Action context variable 'bullets' must be a BulletManager");
		}
		Bullet bullet = (Bullet) SpawnRegistry.create("Bullet", "Bullet");
		bullet.setActionContext(new ActionContext(getContext()));
		bullet.getActionRunner().add(bulletAction, bullet);
		SpawnRegistry.activate(bullet, bulletManager);

		finish();
	}
}
