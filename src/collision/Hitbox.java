package collision;

import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import graphics.Renderer;

import entity.Entity;

public abstract class Hitbox {
	private Entity owner;
	private String name = "[UNNAMED HITBOX]";

	private float offsetX;
	private float offsetY;

	private final Set<CollisionTag> tags = new HashSet<>();

	private boolean enabled = true;

	public Hitbox(Entity owner) {
		this.owner = owner;
		System.out.printf("[Collision] %s created a new hitbox without a name.\n", owner.getName() );
	}
	public Hitbox(Entity owner, String name) {
		this.owner = owner;
		this.name = name.toUpperCase(Locale.ROOT);
	}

	public final Entity getOwner() { return owner; }
	public final String getName() { return name; }
	public final void setName(String name) { this.name = name; }

	public final float getOffsetX() { return offsetX; }
	public final float getOffsetY() { return offsetY; }
	public final void setOffsetX(float x) { this.offsetX = x; }
	public final void setOffsetY(float y) { this.offsetY = y; }
	public final void setOffset(float x, float y) {
		this.offsetX = x;
		this.offsetY = y;
	}
	public final void changeOffset(float dx, float dy) {
		this.offsetX += dx;
		this.offsetY += dy;
	}
	
	public final float getWorldX() { return owner.getX() + offsetX; }
	public final float getWorldY() { return owner.getY() + offsetY; }
		
	public final boolean isEnabled() { return enabled; }
	public final void setEnabled(boolean value) { enabled = value; }

	public final void addTag(CollisionTag tag) { tags.add(tag); }
	public final void removeTag(CollisionTag tag) { tags.remove(tag); }
	public final void clearTags() { tags.clear(); }
	public final boolean hasTag(CollisionTag tag) {
		return tags.contains(tag);
	}
	public final boolean hasAnyTag(CollisionTag... tags) {
		for (CollisionTag tag : tags) {
			if (hasTag(tag)) {
				return true;
			}
		}
		return false;
	}
	
	public abstract void drawDebug(Renderer renderer);
}