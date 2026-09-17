package entity;

import java.awt.Graphics2D;

import action.Action;
import action.ActionContext;
import action.ActionRunner;

import graphics.Depict;
import graphics.Renderer;

public abstract class Thing {
	protected Appearance appearance;
	protected float x;
	protected float y;
	protected float trueAngle;
	protected ActionRunner actions;
	
	protected boolean alive = true;
	
	//debugs
	protected String name;

	public Thing() {
		name = "[UNNAMED THING]";
		x = 1024.0f;	y = 1024.0f;
		actions = new ActionRunner();
		appearance = new Appearance();
	}

	public Appearance getAppearance() { return appearance; }
	
	public void run(Action action) {
		actions.add(action, this);
	}
	public ActionContext getActionContext() { return actions.getContext(); }
	public void setActionContext(ActionContext context) {
		actions = new ActionRunner(context);
	}
	public Object getVariable(String name) { return actions.getContext().get(name); }
	public void updateActions() { actions.update(); }

	public float getX() { return x; }
	public float getY() { return y; }
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void move(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public float getTrueAngle() { return trueAngle; }
	public void setTrueAngle(float angle) { this.trueAngle = angle; }
	
	public boolean isAlive() { return alive; }
	public void setAlive(boolean alive) { this.alive = alive; }
	public void destroy() { this.alive = false; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public void draw(Renderer renderer) { renderer.thing(this); }
	// the abstracts
	public abstract void update();
}