package entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

import collision.Hitbox;

import action.Action;
import action.ActionRunner;
import action.ActionContext;

public abstract class Entity {
	protected double x;
	protected double y;
	protected double trueAngle;
	protected double appearAngle;
	protected boolean angleOverride;
	
	protected boolean visible = true;
	protected boolean alive = true;
	
	protected ArrayList<Hitbox> hitboxes;
	protected ActionRunner actions;
	
	//debugs
	protected String name = "[UNNAMED ENTITY]";

	public Entity() {
		this.alive = true;
		hitboxes = new ArrayList<>();
		actions = new ActionRunner();
	}

	public void run(Action action) {
		actions.add(action, this);
	}
	public ActionContext getActionContext() { return actions.getContext(); }
	public void setActionContext(ActionContext context) {
		actions = new ActionRunner(context);
	}
	public Object getVariable(String name) { return actions.getContext().get(name); }
	public void updateActions() { actions.update(); }
	
	public void addHitbox(Hitbox hitbox) { hitboxes.add(hitbox); }

	public ArrayList<Hitbox> getHitboxes() { return hitboxes; }

	public double getX() { return x; }
	public double getY() { return y; }
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void set(double x, double y) { setPosition(x,y); }
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void move(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	public double getTrueAngle() { return trueAngle; }
	public double getAppearAngle() { return appearAngle; }
	public void setTrueAngle(double angle) { this.trueAngle = angle; }
	public void setAppearAngle(double angle) { this.appearAngle = angle; }
	
	public boolean getAngleOverride() { return angleOverride; }
	public void getAngleOverride(boolean state) { this.angleOverride = state; }
	
	public boolean isVisible() { return visible; }
	public void setVisible(boolean visible) { this.visible = visible; }
	
	public boolean isAlive() { return alive; }
	public void setAlive(boolean alive) { this.alive = alive; }
	public void destroy() { this.alive = false; }
	
	public String getName() { return name; }
	
	// the abstracts
	public void onHit(Hitbox mine, Hitbox other) {}
	public abstract void update();
	public abstract void draw(Graphics2D g2);
}