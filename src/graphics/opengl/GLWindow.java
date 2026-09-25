package graphics.opengl;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;

import org.lwjgl.glfw.GLFWVidMode;

public class GLWindow {
	private final long window;
	private final int width;
	private final int height;

	public GLWindow(int width, int height, String title) {
		this.width = width;
		this.height = height;

		if (!GLFW.glfwInit()) { throw new IllegalStateException("Unable to initialize GLFW"); }

		GLFW.glfwWindowHint(
			GLFW.GLFW_RESIZABLE,
			GLFW.GLFW_FALSE
		);

		window = GLFW.glfwCreateWindow(
			width, height,
			title,
			0, 0
		);

		if (window == 0) {
			GLFW.glfwTerminate();
			throw new IllegalStateException("Unable to create GLFW window");
		}
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(
			GLFW.glfwGetPrimaryMonitor()
		);

		GLFW.glfwSetWindowPos(
			window,
			(videoMode.width() - width) / 2,
			(videoMode.height() - height) / 2
		);

		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		GLFW.glfwSetKeyCallback(window, new GLInput());
		GLFW.glfwSetCursorPosCallback(window, new GLMouse());
		GLFW.glfwSetMouseButtonCallback(window, new GLMouseButton());

		GLFW.glfwSwapInterval(0);
	}

	public void setIcon(BufferedImage image) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		ByteBuffer pixels = BufferUtils.createByteBuffer(
			imageWidth * imageHeight * 4
		);

		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++) {

				int argb = image.getRGB(x, y);

				pixels.put((byte) ((argb >> 16) & 0xFF)); // R
				pixels.put((byte) ((argb >> 8) & 0xFF));  // G
				pixels.put((byte) (argb & 0xFF));         // B
				pixels.put((byte) ((argb >> 24) & 0xFF)); // A
			}
		}

		pixels.flip();
		GLFWImage icon = GLFWImage.malloc();

		icon.set(imageWidth, imageHeight, pixels);

		GLFWImage.Buffer icons = GLFWImage.malloc(1);
		icons.put(0, icon);

		GLFW.glfwSetWindowIcon(
			window,
			icons
		);

		icons.free();
		icon.free();
	}

	public boolean shouldClose() { return GLFW.glfwWindowShouldClose(window); }
	public void update() { GLFW.glfwSwapBuffers(window); GLFW.glfwPollEvents(); }
	public void destroy() { GLFW.glfwDestroyWindow(window); GLFW.glfwTerminate(); }
	public long getHandle() { return window; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}