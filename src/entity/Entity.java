package entity;

import java.awt.Graphics2D;

import collision.Hitbox;
import java.util.ArrayList;

import action.Action;
import action.ActionRunner;

public abstract class Entity {
	protected double x;
	protected double y;
	
	protected boolean alive = true;
	
	protected ArrayList<Hitbox> hitboxes;
	protected ActionRunner actions;
	
	//debugs
	protected String name = "[unnamed entity]";

	public Entity() {
		this.alive = true;
		hitboxes = new ArrayList<>();
		actions = new ActionRunner();
	}

	public void run(Action action) {
		actions.add(action, this);
	}

	public void updateActions() {
		actions.update();
	}
	
	public void addHitbox(Hitbox hitbox) { hitboxes.add(hitbox); }

	public ArrayList<Hitbox> getHitboxes() { return hitboxes; }

	public double getX() { return x; }
	public double getY() { return y; }
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	
	public boolean isAlive() { return alive; }
	public String getName(){ return name; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void move(double dx, double dy) {
		x += dx;
		y += dy;
	}
	public void kill() { this.alive = false; }
	
	// the abstracts
	public void onHit(Hitbox mine, Hitbox other) {}
	public void onGraze(Hitbox mine, Hitbox other){}
	public abstract void update();
	public abstract void draw(Graphics2D g2);
}