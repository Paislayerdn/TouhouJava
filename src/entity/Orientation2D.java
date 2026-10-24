package entity;

import action.ScriptProperty;

public final class Orientation2D {
	@ScriptProperty private float trueAngle;
	@ScriptProperty private float appearAngle;
	@ScriptProperty private boolean angleOverride;

	public float getTrueAngle() { return trueAngle; }
	public void setTrueAngle(float value) { trueAngle = value; }
	public float getAppearAngle() { return appearAngle; }
	public void setAppearAngle(float value) { appearAngle = value; }
	public boolean getAngleOverride() { return angleOverride; }
	public void setAngleOverride(boolean value) { angleOverride = value; }
	public float getAngle() { return angleOverride ? appearAngle : trueAngle; }
	public void setAngle(float value) {
		if (angleOverride) appearAngle = value;
		else trueAngle = value;
	}
}
