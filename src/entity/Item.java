package entity;

import resource.ResourceLoader;

/** A collectible dropped during boss encounters. */
public final class Item extends Entity {
	public enum Type { POINT, POWER, BOMB }

	private final Type type;
	private float velocityX;
	private float velocityY;
	private float targetX;
	private float targetY;
	private boolean homing;
	private int age;

	public Item(Type type, float x, float y, float velocityX, float velocityY) {
		this.type = type;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		setXY(x, y);
		setName(type + " Item");
		getAppearance().setCostume(ResourceLoader.image(switch (type) {
			case POINT -> "BulletStar";
			case POWER -> "BulletDonut";
			case BOMB -> "BulletHeart";
		}));
		getAppearance().setSize(switch (type) {
			case POINT -> 9;
			case POWER -> 11;
			case BOMB -> 14;
		});
		getAppearance().setBrightness(35);
	}

	public Type getType() { return type; }

	public void homeTo(float x, float y) {
		targetX = x;
		targetY = y;
		homing = true;
	}

	public boolean isHoming() { return homing; }

	@Override
	public void update() {
		updateActions();
		age++;
		if (homing) {
			float dx = targetX - getX();
			float dy = targetY - getY();
			float length = (float) Math.sqrt(dx * dx + dy * dy);
			if (length > 0) {
				velocityX = dx / length * 5.5f;
				velocityY = dy / length * 5.5f;
			}
		} else {
			velocityY -= 0.035f;
			velocityX *= 0.995f;
		}
		move(velocityX, velocityY);
		if (age > 900) destroy();
	}
}
