package background3d;

import entity.Appearance;

public final class BackgroundObject extends BO3D {
	public final Appearance appearance;

	public float width;
	public float height;

	public boolean visible = true;

	public BackgroundObject(
		float x, float y, float z,
		float width, float height
	) {
		super(x,y,z);
		this.appearance = new Appearance();
		this.width = width;
		this.height = height;
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
}