package collision;

import entity.Entity;

public abstract class Hitbox {

	protected Entity owner;

	private boolean enabled = true;


	public Hitbox(Entity owner) {
		this.owner = owner;
	}


	public Entity getOwner() {
		return owner;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean value) {
		enabled = value;
	}


	public abstract boolean checkCollision(Hitbox other);

}