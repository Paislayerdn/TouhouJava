package collision;

import entity.Entity;

public class CircleHitbox extends Hitbox {
	private double radius;

	public CircleHitbox(Entity owner, double radius) {
		super(owner);
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public boolean checkCollision(Hitbox other) {
		if (!(other instanceof CircleHitbox)) return false;
		
		CircleHitbox circle = (CircleHitbox) other;

		double dx = owner.getX() - circle.owner.getX();
		double dy = owner.getY() - circle.owner.getY();
		double distanceSquared = dx * dx + dy * dy;
		double r = radius + circle.radius;

		return distanceSquared < r * r;
	}
}