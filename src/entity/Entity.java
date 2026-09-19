package entity;

import java.util.Locale;
import java.awt.Graphics2D;
import java.util.ArrayList;

import collision.Hitbox;
import collision.CollisionResult;
import graphics.Renderer;

public abstract class Entity extends Thing {
	protected float appearAngle;
	protected boolean angleOverride;
	
	protected ArrayList<Hitbox> hitboxes;

	public Entity() {
		super();
		name = "[UNNAMED ENTITY]";
		this.alive = true;
		hitboxes = new ArrayList<>();
	}

	public final void addHitbox(Hitbox hitbox) { hitboxes.add(hitbox); }
	public final ArrayList<Hitbox> getHitboxes() { return hitboxes; }
	public final Hitbox getHitbox(String name) {
		for (Hitbox hitbox : hitboxes) {
			if (hitbox.getName().equals(name.toUpperCase(Locale.ROOT))) {
				return hitbox;
			}
		}

		return null;
	}
	public final void drawHitboxes(Renderer renderer) {
		for (Hitbox hitbox : hitboxes) {
			if (!hitbox.isEnabled()) continue;

			hitbox.drawDebug(renderer);
		}
	}
	
	public final float getAppearAngle() { return appearAngle; }
	public final void setAppearAngle(float angle) { this.appearAngle = angle; }
	
	public final boolean getAngleOverride() { return angleOverride; }
	public final void setAngleOverride(boolean state) { this.angleOverride = state; }
	
	// the abstracts
	public void onHit(CollisionResult collisionResult) {}
}