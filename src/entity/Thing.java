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
	
	public void run(Action action) { actions.add(action, this); }
	
	public final Appearance getAppearance() { return appearance; }
	public final ActionContext getActionContext() { return actions.getContext(); }
	public final void setActionContext(ActionContext context) { actions = new ActionRunner(context); }
	public final Object getVariable(String name) { return actions.getContext().get(name); }
	public final void updateActions() { actions.update(); }

	public final float getX() { return x; }
	public final float getY() { return y; }
	public final void setX(float x) { this.x = x; }
	public final void setY(float y) { this.y = y; }
	public final void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public final void move(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public final float getTrueAngle() { return trueAngle; }
	public final void setTrueAngle(float angle) { this.trueAngle = angle; }
	
	public final boolean isAlive() { return alive; }
	public final void setAlive(boolean alive) { this.alive = alive; }
	public final void destroy() { this.alive = false; }
	
	public final String getName() { return name; }
	public final void setName(String name) { this.name = name; }
	
	public void draw(Renderer renderer) { renderer.thing(this); }
	// the abstracts
	public abstract void update();
}