package graphics.opengl;

import graphics.Renderer;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

import main.Settings;

import resource.ResourceLoader;

import entity.Appearance;
import entity.Thing;
import background3d.Background3D;

public class GLRenderer implements Renderer {
	private static final String VERTEX_SHADER = ResourceLoader.text("/graphics/opengl/", "sprite.vert");
	private static final String FRAGMENT_SHADER = ResourceLoader.text("/graphics/opengl/", "sprite.frag");
	private final int width;
	private final int height;
	private final GLB3D b3d;
	
	private final GLShader shader;
	private GLTexture currentTexture;

	
	private boolean shaderActive;
	private void beginShader() {
		if (shaderActive) { return; }

		shader.use();
		shader.setInt("tex", 0);
		shaderActive = true;
	}

	private void endShader() {
		if (!shaderActive) { return;	}

		shader.stop();
		shaderActive = false;
	}
	private void bindTexture(GLTexture texture) {
		if (currentTexture == texture) {
			return;
		}

		texture.bind();
		currentTexture = texture;
	}
	
	public GLRenderer(int width, int height) {
		this.shader = new GLShader(VERTEX_SHADER, FRAGMENT_SHADER);
		this.width = width;
		this.height = height;
		this.b3d = new GLB3D();
		
		GL11.glViewport(0, 0, width, height);

		// Game coordinate system:
		// (0,0) = screen center
		// +X = right
		// +Y = up
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GL11.glOrtho(
			-width / 2.0,
			 width / 2.0,
			-height / 2.0,
			 height / 2.0,
			-1,
			 1
		);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(
			GL11.GL_SRC_ALPHA,
			GL11.GL_ONE_MINUS_SRC_ALPHA
		);
	}
	
	@Override
	public void scale(float x, float y) {
		GL11.glScalef(x, y, 1.0f);
	}

	@Override
	public void clear() {
		GL11.glClearColor(0.05f, 0.05f, 0.05f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void image(BufferedImage image, float x, float y) {
		GLTexture texture = GLTexture.get(image);

		renderImage(texture, x, y, texture.getWidth(), texture.getHeight());
	}

	@Override
	public void image(
		BufferedImage image,
		float x,
		float y,
		float width,
		float height
	) {
		GLTexture texture = GLTexture.get(image);

		renderImage(texture, x, y, width, height);
	}

	private void renderImage(GLTexture texture,
		float x, float y,
		float wwidth, float hheight
	) {
		shader.setFloat("color", 0.0f);
		shader.setFloat("brightness", 0.0f);
		shader.setFloat("alpha", 1.0f);
		
		float halfWidth = wwidth / 2.0f;
		float halfHeight = hheight / 2.0f;

		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		bindTexture(texture);

		drawTexturedQuad(
			x1, y1,
			x2, y1,
			x2, y2,
			x1, y2
		);
	}

	private void drawTexturedQuad(
		float x1, float y1,
		float x2, float y2,
		float x3, float y3,
		float x4, float y4
	) {
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2f(x1, y1);

		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2f(x2, y2);

		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2f(x3, y3);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2f(x4, y4);

		GL11.glEnd();
	}

	@Override
	public void thing(Thing thing) {
		Appearance appearance = thing.getAppearance();

		if (appearance.costume == null) { return; }
		if (appearance.size == 0 || appearance.ghost == 100) { return; }

		GLTexture texture = GLTexture.get(appearance.costume);

		float scale = appearance.size / 100.0f;
		float alpha = (100.0f - appearance.ghost) / 100.0f;
		float angle = thing.getAngleOverride()? thing.getAppearAngle(): thing.getTrueAngle();

		drawThing(texture,
			thing.getX(), thing.getY(),
			scale, angle,
			appearance.color, appearance.brightness, alpha
		);
	}

	private void drawThing(GLTexture texture,
		float x, float y,
		float scale, float angle,
		float color, float brightness, float alpha
	) {
		float radians = (float) Math.toRadians(angle);

		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);

		float halfWidth = texture.getWidth() * scale / 2.0f;
		float halfHeight = texture.getHeight() * scale / 2.0f;

		float x1 = -halfWidth * cos + halfHeight * sin + x;
		float y1 = -halfWidth * sin - halfHeight * cos + y;

		float x2 = halfWidth * cos + halfHeight * sin + x;
		float y2 = halfWidth * sin - halfHeight * cos + y;

		float x3 = halfWidth * cos - halfHeight * sin + x;
		float y3 = halfWidth * sin + halfHeight * cos + y;

		float x4 = -halfWidth * cos - halfHeight * sin + x;
		float y4 = -halfWidth * sin + halfHeight * cos + y;

		shader.setInt("tex", 0);
		shader.setFloat("color", color);
		shader.setFloat("brightness", brightness);
		shader.setFloat("alpha", alpha);

		bindTexture(texture);

		drawTexturedQuad(
			x1, y1,
			x2, y2,
			x3, y3,
			x4, y4
		);
	}
	
	@Override
	public void background3D(Background3D background) {
		b3d.render(background);
	}

	@Override
	public void beginPlayfield() {
		GL11.glPushMatrix();

		GL11.glTranslatef(
			Settings.PLAYFIELD_CENTER_X,
			Settings.PLAYFIELD_CENTER_Y,
			0.0f
		);
		beginShader();
	}

	@Override
	public void endPlayfield() {
		endShader();
		
		if (currentTexture != null) {
			currentTexture.unbind();
			currentTexture = null;
		}
		
		GL11.glPopMatrix();
	}

	@Override
	public void beginTitle() {
		GL11.glPushMatrix();
		beginShader();
	}

	@Override
	public void endTitle() {
		endShader();
		if (currentTexture != null) {
			currentTexture.unbind();
			currentTexture = null;
		}
		
		GL11.glPopMatrix();
	}

	private void beginDebug() {
		endShader();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glLineWidth(2.0f);
	}

	private void endDebug() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		beginShader();
	}
	@Override
	public void rectangleOutline(float x, float y, float width, float height) {
		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;
		beginDebug();
		GL11.glBegin(GL11.GL_LINE_LOOP);

		GL11.glVertex2f(x - halfWidth, y - halfHeight);
		GL11.glVertex2f(x + halfWidth, y - halfHeight);
		GL11.glVertex2f(x + halfWidth, y + halfHeight);
		GL11.glVertex2f(x - halfWidth, y + halfHeight);

		GL11.glEnd();
		endDebug();
	}

	@Override
	public void circleOutline(float x, float y, float radius) {
		int segments = 64;

		beginDebug();
		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int i = 0; i < segments; i++) {
			float angle = (float)(2.0 * Math.PI * i / segments);

			float px = x + (float)Math.cos(angle) * radius;
			float py = y + (float)Math.sin(angle) * radius;

			GL11.glVertex2f(px, py);
		}

		GL11.glEnd();
		endDebug();
	}
}