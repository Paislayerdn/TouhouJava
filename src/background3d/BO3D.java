package background3d;

import action.ActionTarget;

public abstract class BO3D extends ActionTarget {
	private final Position3D position;
	private final Orientation3D orientation;

	protected BO3D() { this(0, 0, 0, 0, 0, 0); }
	protected BO3D(float x, float y, float z) { this(x, y, z, 0, 0, 0); }
	protected BO3D(float x, float y, float z,
		float pitch, float yaw, float roll) {
		position = new Position3D(x, y, z);
		orientation = new Orientation3D(pitch, yaw, roll);
		registerScriptComponents(position, orientation);
	}

	public final Position3D getPosition() { return position; }
	public final Orientation3D getOrientation() { return orientation; }
}
