package background3d;

import action.ActionTarget;
import entity.Orientation3D;
import entity.Position3D;

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
	public final float getZ() { return position.getZ(); }
	public final float getPitch() { return orientation.getPitch(); }
	public final float getYaw() { return orientation.getYaw(); }
	public final float getRoll() { return orientation.getRoll(); }
	public final void setZ(float value) { position.setZ(value); }
	public final void setXYZ(float x, float y, float z) { position.set(x, y, z); }
	public final void setPitch(float value) { orientation.setPitch(value); }
	public final void setYaw(float value) { orientation.setYaw(value); }
	public final void setRoll(float value) { orientation.setRoll(value); }
	public final void setRotation(float pitch, float yaw, float roll) {
		orientation.set(pitch, yaw, roll);
	}
	public final void changeXYZ(float x, float y, float z) {
		position.set(position.getX() + x, position.getY() + y, position.getZ() + z);
	}
	public final void changeRotation(float pitch, float yaw, float roll) {
		orientation.set(getPitch() + pitch, getYaw() + yaw, getRoll() + roll);
	}
}
