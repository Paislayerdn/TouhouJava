package entity;

import java.awt.Graphics2D;

import action.Action;
import action.ActionRunner;

public abstract class Entity {
	protected double x;
	protected double y;

	protected ActionRunner actions;


	public Entity() {
		actions = new ActionRunner();
	}


	public void run(Action action) {
		actions.add(action, this);
	}


	public void updateActions() {
		actions.update();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void move(double dx, double dy) {
		x += dx;
		y += dy;
	}
	public abstract void update();

	public abstract void draw(Graphics2D g);
}