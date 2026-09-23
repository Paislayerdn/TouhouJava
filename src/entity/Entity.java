package entity;

import java.util.Locale;
import java.util.ArrayList;

import collision.Hitbox;
import collision.CollisionResult;
import graphics.Renderer;

public abstract class Entity extends Thing {
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
	
	// the abstracts
	public void onHit(CollisionResult collisionResult) {}
}