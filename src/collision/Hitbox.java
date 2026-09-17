package collision;

import graphics.Renderer;
import java.util.Locale;
import java.awt.Graphics2D;
import java.util.HashSet;

import entity.Entity;

public abstract class Hitbox {
	private Entity owner;
	private String name = "[UNNAMED HITBOX]";

	private float offsetX;
	private float offsetY;

	private final HashSet<String> tags = new HashSet<>();

	private boolean enabled = true;

	public Hitbox(Entity owner) {
		this.owner = owner;
		System.out.printf("[Collision] %s created a new hitbox without a name.\n", owner.getName() );
	}
	public Hitbox(Entity owner, String name) {
		this.owner = owner;
		this.name = name.toUpperCase(Locale.ROOT);
	}

	public Entity getOwner() { return owner; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public float getOffsetX() { return offsetX; }
	public float getOffsetY() { return offsetY; }
	public void setOffsetX(float x) { this.offsetX = x; }
	public void setOffsetY(float y) { this.offsetY = y; }
	public void setOffset(float x, float y) {
		this.offsetX = x;
		this.offsetY = y;
	}
	public void changeOffset(float dx, float dy) {
		this.offsetX += dx;
		this.offsetY += dy;
	}
	
	public float getWorldX() { return owner.getX() + offsetX; }
	public float getWorldY() { return owner.getY() + offsetY; }
		
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean value) { enabled = value; }

	public void addTag(String tag) { tags.add( tag.toUpperCase( Locale.ROOT ) ); }
	public void removeTag(String tag) { tags.remove( tag.toUpperCase( Locale.ROOT ) ); }
	public void clearTags() { tags.clear(); }
	public boolean hasTag(String tag) { return tags.contains( tag.toUpperCase( Locale.ROOT ) ); }
	public boolean hasAnyTag(String... tags) {
		for (String tag : tags) {
			if (hasTag(tag)) {
				return true;
			}
		}

		return false;
	}
	public HashSet<String> getTags() { return tags; }
	
	public abstract void drawDebug(Renderer renderer);
}