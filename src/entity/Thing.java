package entity;

import action.ActionTarget;
import graphics.Renderer;
import resource.ResourceLoader;

public class Thing extends ActionTarget {
	private final Position2D position;
	private final Orientation2D orientation;
	private final Appearance appearance;

	public Thing() {
		this(1024, 1024, null, 100);
	}

	/** Creates a Thing from a resource name with its initial position and size. */
	public Thing(float x, float y, String spriteName, float size) {
		this.position = new Position2D(x, y);
		this.orientation = new Orientation2D();
		this.appearance = new Appearance();
		if (spriteName != null) {
			this.appearance.setCostume(ResourceLoader.image(spriteName));
		}
		this.appearance.setSize(size);
		setName("[UNNAMED THING]");
		registerScriptComponents(position, orientation, appearance);
	}

	public void draw(Renderer renderer) { renderer.thing(this); }
	public void update() { updateActions(); }

	@Override
	public final Appearance getAppearance() { return appearance; }
	public final Position2D getPosition() { return position; }
	public final Orientation2D getOrientation() { return orientation; }
	public float getRenderedSize() { return appearance.size; }

}
