package graphics.opengl;

import background3d.BackgroundObject;
import entity.Appearance;

import org.lwjgl.opengl.GL11;

public final class GLB3D {
	private static final float PLANE_EXTENT = 20000.0f;
	private static final float PLANE_TILE_SIZE = 100.0f;

	public void render(BackgroundObject object) {
		if (!object.visible) {
			return;
		}

		Appearance appearance = object.getAppearance();

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
			object.getX(),
			object.getY(),
			object.getZ()
		);

		GL11.glRotatef(
			object.getRoll(),
			0.0f,
			0.0f,
			1.0f
		);

		GL11.glRotatef(
			object.getYaw(),
			0.0f,
			1.0f,
			0.0f
		);

		GL11.glRotatef(
			object.getPitch(),
			1.0f,
			0.0f,
			0.0f
		);

		float width = object.tiledPlane ? PLANE_EXTENT : object.width;
		float height = object.tiledPlane ? PLANE_EXTENT : object.height;
		float tilesX = object.tiledPlane ? width / PLANE_TILE_SIZE : 1.0f;
		float tilesY = object.tiledPlane ? height / PLANE_TILE_SIZE : 1.0f;
		drawTexturedPlane(texture, width, height, tilesX, tilesY);

		GL11.glPopMatrix();
	}

	private void drawTexturedPlane(
		GLTexture texture,
		float width,
		float height,
		float tilesX,
		float tilesY
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

		GL11.glTexCoord2f(tilesX, 0.0f);
		GL11.glVertex3f(
			halfWidth,
			halfHeight,
			0.0f
		);

		GL11.glTexCoord2f(tilesX, tilesY);
		GL11.glVertex3f(
			halfWidth,
			-halfHeight,
			0.0f
		);

		GL11.glTexCoord2f(0.0f, tilesY);
		GL11.glVertex3f(
			-halfWidth,
			-halfHeight,
			0.0f
		);

		GL11.glEnd();
	}
}
