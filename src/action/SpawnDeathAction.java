package action;

import state.gameplay.BulletManager;
import entity.Bullet;

final class SpawnBulletAction extends Action {
	@Override
	public boolean consumesFrame() { return false; }
	private final Action bulletAction;

	public SpawnBulletAction(Action bulletAction) {
		this.bulletAction = bulletAction;
	}

	@Override
	public void start() {
		Bullet bullet = new Bullet();

		ActionContext bulletContext = new ActionContext(getContext());
		bullet.setActionContext(bulletContext);

		BulletManager.spawnEnemy(bullet);
		bullet.run(bulletAction);

		finish();
	}
}

final class DestroyAction extends Action {
	@Override
	public boolean consumesFrame() { return false; }

	@Override
	public void start() {
		owner.setAlive(false);
		finish();
	}
}