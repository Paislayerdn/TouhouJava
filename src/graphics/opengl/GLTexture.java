package graphics.opengl;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GLTexture {
	private static final Map<BufferedImage, GLTexture> cache = new IdentityHashMap<>();

	private final int id;
	private final int width;
	private final int height;

	public static GLTexture get(BufferedImage image) {
		GLTexture texture = cache.get(image);

		if (texture == null) {
			texture = new GLTexture(image);
			cache.put(image, texture);
		}

		return texture;
	}

	private GLTexture(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();

		id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

		GL11.glTexParameteri(
				GL11.GL_TEXTURE_2D,
				GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR
		);

		GL11.glTexParameteri(
				GL11.GL_TEXTURE_2D,
				GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR
		);

		ByteBuffer pixels =
				BufferUtils.createByteBuffer(width * height * 4);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				int argb = image.getRGB(x, y);

				pixels.put((byte) ((argb >> 16) & 0xFF));
				pixels.put((byte) ((argb >> 8) & 0xFF));
				pixels.put((byte) (argb & 0xFF));
				pixels.put((byte) ((argb >> 24) & 0xFF));
			}
		}

		pixels.flip();

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void bind() { GL11.glBindTexture(GL11.GL_TEXTURE_2D, id); }
	public void unbind() { GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}