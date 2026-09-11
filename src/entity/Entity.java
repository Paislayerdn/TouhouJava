package entity;

import java.util.Locale;
import java.awt.Graphics2D;
import java.util.ArrayList;

import collision.Hitbox;
import collision.CollisionResult;

public abstract class Entity extends Thing {
	protected double appearAngle;
	protected boolean angleOverride;
	
	protected ArrayList<Hitbox> hitboxes;

	public Entity() {
		super();
		name = "[UNNAMED ENTITY]";
		this.alive = true;
		hitboxes = new ArrayList<>();
	}


	public void addHitbox(Hitbox hitbox) { hitboxes.add(hitbox); }
	public ArrayList<Hitbox> getHitboxes() { return hitboxes; }
	public Hitbox getHitbox(String name) {
		for (Hitbox hitbox : hitboxes) {
			if (hitbox.getName().equals(name.toUpperCase(Locale.ROOT))) {
				return hitbox;
			}
		}

		return null;
	}
	public void drawHitboxes(Graphics2D g2) {
		for (Hitbox hitbox : hitboxes) {
			if (!hitbox.isEnabled()) continue;

			hitbox.drawDebug(g2);
		}
	}
	
	public double getAppearAngle() { return appearAngle; }
	public void setAppearAngle(double angle) { this.appearAngle = angle; }
	
	public boolean getAngleOverride() { return angleOverride; }
	public void setAngleOverride(boolean state) { this.angleOverride = state; }
	
	// the abstracts
	public void onHit(CollisionResult collisionResult) {}
}