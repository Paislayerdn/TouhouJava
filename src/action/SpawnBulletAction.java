package action;

import game.BulletManager;

import entity.Boss;
import entity.Player;
import entity.Bullet;

public class SpawnBulletAction extends Action {
	private double x;
	private double y;
	private double angle;
	private boolean hasPosition;

	public boolean consumesFrame() { return false; }

	private final Action bulletAction;

	public SpawnBulletAction(Action bulletAction) {
		this.bulletAction = bulletAction;
		this.hasPosition = false;
	}
	public SpawnBulletAction(double x, double y, double angle, Action bulletAction) {
		this.x = x; this.y = y;
		this.angle = angle;
		this.bulletAction = bulletAction;
		this.hasPosition = true;
	}

	@Override
	public void update() {
		Bullet bullet = new Bullet();

		ActionContext bulletContext = new ActionContext(getContext());
		bullet.setActionContext(bulletContext);

		if (hasPosition) {
			bullet.setXY(x, y);
			bullet.setTrueAngle(angle);
		}

		BulletManager.add(bullet);
		bullet.run(bulletAction);

		finish();
	}
}