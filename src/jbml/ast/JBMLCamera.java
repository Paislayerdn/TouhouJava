package jbml.ast;

import background3d.BO3D;

public final class JBMLCamera extends BO3D implements JBMLDeclaration {
	public JBMLCamera(
		float x, float y, float z,
		float pitch, float yaw, float roll
	) {
		super(x,y,z,pitch,yaw,roll);
	}
}