package background3d;

import entity.Appearance;

public final class BackgroundObject extends BO3D {
	private final Appearance appearance;
	public float width, height;
	public final boolean tiledPlane;
	public boolean visible = true;

	public BackgroundObject(String name, float x, float y, float z,
		float width, float height, boolean tiledPlane) {
		this(name, x, y, z, width, height, tiledPlane, new Appearance());
	}

	private BackgroundObject(String name, float x, float y, float z,
		float width, float height, boolean tiledPlane, Appearance appearance) {
		super(x, y, z);
		this.appearance = appearance;
		setName(name);
		this.width = width;
		this.height = height;
		this.tiledPlane = tiledPlane;
		registerScriptComponents(appearance);
	}

	public Appearance getAppearance() { return appearance; }

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public BackgroundObject copyAt(float x, float y, float z) {
		BackgroundObject copy = new BackgroundObject(getName(), x, y, z,
			width, height, tiledPlane, getAppearance());
		copy.getOrientation().set(
			getOrientation().getPitch(), getOrientation().getYaw(), getOrientation().getRoll());
		copy.visible = visible;
		return copy;
	}
}
