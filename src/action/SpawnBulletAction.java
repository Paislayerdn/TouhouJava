package action;

import entity.Boss;
import entity.Bullet;

public class SpawnBulletAction extends Action {
	private Object index;
	private double x;
	private double y;
	private double angle;
	private boolean hasPosition;
	public boolean consumesFrame() { return false; }
	
	private final Action bulletAction;

	public SpawnBulletAction(Object index, Action bulletAction) {
		this.index = index;
		this.bulletAction = bulletAction;
		this.hasPosition = false;
	}
	public SpawnBulletAction(Object index, double x, double y, double angle, Action bulletAction) {
		this.index = index;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.bulletAction = bulletAction;
		this.hasPosition = true;
	}
	public int getIndex() {
		return (int)resolveDouble(this.index);
	}

	@Override
	public void update() {
		Boss boss = (Boss) owner;
		
		int intIndex = (int)resolveDouble(this.index);

		Bullet bullet = new Bullet(boss);
		ActionContext bulletContext = getContext().snapshot();
		bullet.setActionContext(bulletContext);
		bullet.getActionContext().declare("index", intIndex);
		
		Object resolvedIndex = resolve(index);
		bulletContext.declare("index",resolvedIndex);

		if (hasPosition) {
			bullet.setPosition(x, y);
			bullet.setTrueAngle(angle);
		}

		boss.getBulletManager().add(bullet);
		bullet.run(bulletAction);
		
		finish();
	}
}