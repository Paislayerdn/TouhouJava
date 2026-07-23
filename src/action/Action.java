package action;

import entity.Entity;

public abstract class Action {
	protected Entity owner;

	protected boolean finished = false;


	// Called once when this action starts
	public void start() {

	}


	// Called every frame
	public abstract void update();


	public void setOwner(Entity owner) {
		this.owner = owner;
	}


	public boolean isFinished() {
		return finished;
	}


	// Allows an action to stop itself
	public void finish() {
		finished = true;
	}


	// Used later for Forever / reuse actions
	public void reset() {
		finished = false;
	}
}