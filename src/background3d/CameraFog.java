package background3d;

import action.ScriptProperty;
import action.ScriptPropertySetter;

public final class CameraFog implements ScriptPropertySetter {
	@ScriptProperty public float fogRed;
	@ScriptProperty public float fogGreen;
	@ScriptProperty public float fogBlue;
	@ScriptProperty public float fogDistance;

	public void setColor(float red, float green, float blue) {
		fogRed = clamp(red);
		fogGreen = clamp(green);
		fogBlue = clamp(blue);
	}

	private static float clamp(float value) {
		return Math.max(0.0f, Math.min(255.0f, value));
	}

	@Override
	public boolean setScriptProperty(String name, Object value) {
		float number = ((Number) value).floatValue();
		switch (name) {
			case "fogRed" -> fogRed = clamp(number);
			case "fogGreen" -> fogGreen = clamp(number);
			case "fogBlue" -> fogBlue = clamp(number);
			case "fogDistance" -> fogDistance = Math.max(0, number);
			default -> { return false; }
		}
		return true;
	}
}
