package collision;

import java.awt.Graphics2D;
import graphics.Depict;

import entity.Entity;

public class RectangleHitbox extends Hitbox {
	private double width;
	private double height;

	public RectangleHitbox(Entity owner, double width, double height) {
		super(owner);
		this.width = width;
		this.height = height;
	}

	public RectangleHitbox(Entity owner, String name, double width, double height) {
		super(owner, name);
		this.width = width;
		this.height = height;
	}

	public double getWidth() { return width; }
	public double getHeight() { return height; }
	
	@Override
	public void drawDebug(Graphics2D g2) {
		Depict.rectangleOutline(g2, getWorldX(), getWorldY(), width, height);
	}
}