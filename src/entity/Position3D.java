package entity;

import action.ScriptProperty;

public final class Position3D extends Position2D {
	@ScriptProperty private float z;

	public Position3D() { this(0, 0, 0); }
	public Position3D(float x, float y, float z) {
		super(x, y);
		setZ(z);
	}
	public float getZ() { return z; }
	public void setZ(float z) { this.z = z; }
	public void set(float x, float y, float z) {
		super.set(x, y);
		setZ(z);
	}
}
