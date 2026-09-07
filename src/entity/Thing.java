package entity;

import java.awt.Graphics2D;

import action.Action;
import action.ActionContext;
import action.ActionRunner;

public abstract class Thing {
	protected double x;
	protected double y;
	protected double trueAngle;
	protected ActionRunner actions;
	
	protected boolean alive = true;
	
	//debugs
	protected String name;

	public Thing() {
		name = "[UNNAMED THING]";
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

	public double getX() { return x; }
	public double getY() { return y; }
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void move(double dx, double dy) {
		x += dx;
		y += dy;
	}
	
	public double getTrueAngle() { return trueAngle; }
	public void setTrueAngle(double angle) { this.trueAngle = angle; }
	
	public boolean isAlive() { return alive; }
	public void setAlive(boolean alive) { this.alive = alive; }
	public void destroy() { this.alive = false; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	// the abstracts
	public abstract void update();
	public abstract void draw(Graphics2D g2);
}