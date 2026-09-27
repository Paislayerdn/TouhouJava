package jbml.ast;

import background3d.BO3D;

public final class JBMLObject extends BO3D {
	public final String name;

	public JBMLObject(
		String name,
		float x, float y, float z,
		float pitch, float yaw, float roll
	) {
		super(x,y,z, pitch, yaw, roll);
		this.name = name;
	}
}