package collision;

import java.util.Locale;
import java.awt.Graphics2D;
import java.util.HashSet;

import entity.Entity;

public abstract class Hitbox {
	private Entity owner;
	private String name = "[UNNAMED HITBOX]";

	private double offsetX;
	private double offsetY;

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

	public double getOffsetX() { return offsetX; }
	public double getOffsetY() { return offsetY; }
	public void setOffsetX(double x) { this.offsetX = x; }
	public void setOffsetY(double y) { this.offsetY = y; }
	public void setOffset(double x, double y) {
		this.offsetX = x;
		this.offsetY = y;
	}
	public void changeOffset(double dx, double dy) {
		this.offsetX += dx;
		this.offsetY += dy;
	}
	
	public double getWorldX() { return owner.getX() + offsetX; }
	public double getWorldY() { return owner.getY() + offsetY; }
		
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
	
	public abstract void drawDebug(Graphics2D g2);
}