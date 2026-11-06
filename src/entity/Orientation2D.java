package entity;

import action.ScriptProperty;
import action.ScriptPropertySetter;

public final class Orientation2D implements ScriptPropertySetter {
	@ScriptProperty private float trueAngle, appearAngle;
	@ScriptProperty private boolean angleOverride;

	public float getTrueAngle() { return trueAngle; }
	public void setTrueAngle(float value) { trueAngle = value; }
	public float getAppearAngle() { return appearAngle; }
	public void setAppearAngle(float value) { appearAngle = value; }
	public boolean getAngleOverride() { return angleOverride; }
	public void setAngleOverride(boolean value) { angleOverride = value; }
	public float getAngle() { return angleOverride ? appearAngle : trueAngle; }
	public void setAngle(float value) { if (angleOverride) appearAngle = value; else trueAngle = value; }

	@Override
	public String[] getAdditionalScriptProperties() { return new String[] {"angle"}; }
	@Override
	public Object getScriptProperty(String name) { return name.equals("angle") ? getAngle() : null; }
	@Override
	public boolean setScriptProperty(String name, Object value) {
		if (!name.equals("angle")) return false;
		setAngle(((Number) value).floatValue());
		return true;
	}
}
