package entity;

import action.ActionTarget;
import graphics.Renderer;

public class Thing extends ActionTarget {
	private final Position2D position;
	private final Orientation2D orientation;
	private final Appearance appearance;

	public Thing() {
		this.position = new Position2D(1024, 1024);
		this.orientation = new Orientation2D();
		this.appearance = new Appearance();
		setName("[UNNAMED THING]");
		registerScriptComponents(position, orientation, appearance);
	}

	public void draw(Renderer renderer) { renderer.thing(this); }
	public void update() { updateActions(); }

	@Override
	public final Appearance getAppearance() { return appearance; }
	public final Position2D getPosition() { return position; }
	public final Orientation2D getOrientation() { return orientation; }

}
