package action;

import state.gameplay.BulletManager;
import entity.Bullet;

public final class SpawnBulletAction extends Action {
	@Override
	public boolean consumesFrame() { return false; }
	private final Action bulletAction;

	public SpawnBulletAction(Action bulletAction) {
		this.bulletAction = bulletAction;
	}

	@Override
	public void start() {
		Bullet bullet = (Bullet) SpawnRegistry.create("Bullet", "Bullet");
		bullet.setActionContext(new ActionContext(getContext()));
		bullet.getActionRunner().add(bulletAction, bullet);
		SpawnRegistry.activate(bullet);

		finish();
	}
}
