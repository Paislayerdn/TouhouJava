package background3d;

public abstract class BO3D { // Backgroudn Object 3D
	public float x;
	public float y;
	public float z;

	public float pitch;
	public float yaw;
	public float roll;
	
	public BO3D() {
		this(0,0,0);
	}
	public BO3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setXYZ(float x, float y, float z) {
	   this.x = x;
	   this.y = y;
	   this.z = z;
	}
	public void changeXYZ(float x, float y, float z) {
	   this.x += x;
	   this.y += y;
	   this.z += z;
	}

	public void setRotation(float pitch, float yaw, float roll) {
	   this.pitch = pitch;
	   this.yaw = yaw;
	   this.roll = roll;
	}
	public void changeRotation(float pitch, float yaw, float roll) {
	   this.pitch += pitch;
	   this.yaw += yaw;
	   this.roll += roll;
	}
}