package collision;

import graphics.Renderer;

import entity.Entity;

public class RectangleHitbox extends Hitbox {
	private float width;
	private float height;

	public RectangleHitbox(Entity owner, float width, float height) {
		super(owner);
		this.width = width;
		this.height = height;
	}

	public RectangleHitbox(Entity owner, String name, float width, float height) {
		super(owner, name);
		this.width = width;
		this.height = height;
	}

	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
	@Override
	public void drawDebug(Renderer renderer) {
		renderer.rectangleOutline(getWorldX(), getWorldY(), width, height);
	}
}