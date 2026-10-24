package entity;

import action.ScriptProperty;

public final class Orientation3D {
	@ScriptProperty private float pitch;
	@ScriptProperty private float yaw;
	@ScriptProperty private float roll;

	public Orientation3D() { this(0, 0, 0); }
	public Orientation3D(float pitch, float yaw, float roll) {
		set(pitch, yaw, roll);
	}
	public float getPitch() { return pitch; }
	public float getYaw() { return yaw; }
	public float getRoll() { return roll; }
	public void setPitch(float value) { pitch = value; }
	public void setYaw(float value) { yaw = value; }
	public void setRoll(float value) { roll = value; }
	public void set(float pitch, float yaw, float roll) {
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
}
