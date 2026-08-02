package collision;

import java.util.HashSet;

import entity.Entity;

public abstract class Hitbox {
	private Entity owner;
	private String name = "[unnamed hitbox]";

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
		this.name = name;
	}

	public Entity getOwner() { return owner; }
	public String getName() { return name; }

	public double getOffsetX() { return offsetX; }
	public double getOffsetY() { return offsetY; }
	public double getWorldX() { return owner.getX() + offsetX; }
	public double getWorldY() { return owner.getY() + offsetY; }
	
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
	
	public boolean isEnabled() { return enabled; }

	public void setEnabled(boolean value) { enabled = value; }

	public void addTag(String tag) { tags.add(tag); }
	public void removeTag(String tag) { tags.clear(); }
	public void clearTags() { tags.removeAll(tags); }
	public boolean hasTag(String tag) { return tags.contains(tag); }
	public HashSet<String> getTags() { return tags; }
	
	public abstract boolean checkCollision(Hitbox other);
}