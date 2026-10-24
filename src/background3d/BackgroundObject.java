package background3d;

import entity.Appearance;

public final class BackgroundObject extends BO3D {
	public float width;
	public float height;
	public final boolean tiledPlane;
	public boolean visible = true;

	public BackgroundObject(String name, float x, float y, float z,
		float width, float height, boolean tiledPlane) {
		this(name, x, y, z, width, height, tiledPlane, new Appearance());
	}

	private BackgroundObject(String name, float x, float y, float z,
		float width, float height, boolean tiledPlane, Appearance appearance) {
		super(x, y, z);
		setName(name);
		this.width = width;
		this.height = height;
		this.tiledPlane = tiledPlane;
		registerScriptComponents(appearance);
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public BackgroundObject copyAt(float x, float y, float z) {
		BackgroundObject copy = new BackgroundObject(getName(), x, y, z,
			width, height, tiledPlane, getAppearance());
		copy.setRotation(getPitch(), getYaw(), getRoll());
		copy.visible = visible;
		return copy;
	}
}
