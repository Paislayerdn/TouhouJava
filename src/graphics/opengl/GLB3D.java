package graphics.opengl;

import background3d.BackgroundObject;
import entity.Appearance;

import org.lwjgl.opengl.GL11;

public final class GLB3D {
	public void render(BackgroundObject object) {
		if (!object.visible) {
			return;
		}

		Appearance appearance = object.appearance;

		if (appearance.costume == null
			|| appearance.size == 0
			|| appearance.ghost == 100
		) {
			return;
		}

		GLTexture texture = GLTexture.get(
			appearance.getRenderedCostume()
		);

		GL11.glPushMatrix();

		GL11.glTranslatef(
			object.x,
			object.y,
			object.z
		);

		GL11.glRotatef(
			object.roll,
			0.0f,
			0.0f,
			1.0f
		);

		GL11.glRotatef(
			object.yaw,
			0.0f,
			1.0f,
			0.0f
		);

		GL11.glRotatef(
			object.pitch,
			1.0f,
			0.0f,
			0.0f
		);

		drawTexturedPlane(
			texture,
			object.width,
			object.height
		);

		GL11.glPopMatrix();
	}

	private void drawTexturedPlane(
		GLTexture texture,
		float width,
		float height
	) {
		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;

		texture.bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(
			-halfWidth,
			halfHeight,
			0.0f
		);

		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(
			halfWidth,
			halfHeight,
			0.0f
		);

		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(
			halfWidth,
			-halfHeight,
			0.0f
		);

		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(
			-halfWidth,
			-halfHeight,
			0.0f
		);

		GL11.glEnd();
	}
}