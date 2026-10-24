package background3d;

public final class BackgroundCamera extends BO3D {
	public float zoom = 1.0f;
	private final CameraFog fog;

	public BackgroundCamera() {
		super();
		fog = new CameraFog();
		registerScriptComponents(fog);
	}

	public CameraFog getFog() { return fog; }
	public float getFogRed() { return fog.fogRed; }
	public float getFogGreen() { return fog.fogGreen; }
	public float getFogBlue() { return fog.fogBlue; }
	public float getFogDistance() { return fog.fogDistance; }
	public void setFogColor(float red, float green, float blue) {
		fog.setColor(red, green, blue);
	}
	public void setFogDistance(float distance) {
		fog.fogDistance = Math.max(0, distance);
	}
}
