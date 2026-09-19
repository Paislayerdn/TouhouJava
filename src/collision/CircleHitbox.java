package collision;

import graphics.Renderer;

import entity.Entity;

public final class CircleHitbox extends Hitbox {
	private float radius;

	public CircleHitbox(Entity owner, float radius) {
		super(owner);
		this.radius = radius;
	}
	public CircleHitbox(Entity owner, String name, float radius) {
		super(owner, name);
		this.radius = radius;
	}

	public float getRadius() { return radius; }
	
	@Override
	public void drawDebug(Renderer renderer) {
		renderer.circleOutline(getWorldX(), getWorldY(), radius);
	}
}