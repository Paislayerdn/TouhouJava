package collision;

import java.awt.Graphics2D;

import graphics.Depict;

import entity.Entity;

public class CircleHitbox extends Hitbox {
	private double radius;

	public CircleHitbox(Entity owner, double radius) {
		super(owner);
		this.radius = radius;
	}
	public CircleHitbox(Entity owner, String name, double radius) {
		super(owner, name);
		this.radius = radius;
	}

	public double getRadius() { return radius; }
	
	@Override
	public void drawDebug(Graphics2D g2) {
		Depict.circleOutline(g2, getWorldX(), getWorldY(), radius);
	}
}