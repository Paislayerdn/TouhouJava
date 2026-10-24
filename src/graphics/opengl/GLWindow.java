package graphics.opengl;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.system.MemoryStack;

import main.Input;

import main.Debug;

public class GLWindow {
	private final long window;
	private final int width, height;
	private int framebufferWidth, framebufferHeight;

	public GLWindow(int width, int height, String title) {
		this.width = width;
		this.height = height;

		if (!GLFW.glfwInit()) { throw Debug.terminate(this, "Unable to initialize GLFW"); }

		GLFW.glfwWindowHint(
			GLFW.GLFW_RESIZABLE,
			GLFW.GLFW_FALSE
		);
		GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);

		window = GLFW.glfwCreateWindow(
			width, height,
			title,
			0, 0
		);

		if (window == 0) {
			GLFW.glfwTerminate();
			throw Debug.terminate(this, "Unable to create GLFW window");
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
		refreshFramebufferSize();
		
		GLFW.glfwSetKeyCallback(window, new GLInput());
		GLFW.glfwSetCursorPosCallback(window, new GLMouse());
		GLFW.glfwSetMouseButtonCallback(window, new GLMouseButton());
		GLFW.glfwSetWindowFocusCallback(
			window,
			new GLFWWindowFocusCallback() {
				@Override
				public void invoke(long window, boolean focused) {
					if (!focused) {
						Input.clear();
					}
				}
			}
		);

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
	public int getFramebufferWidth() { return framebufferWidth; }
	public int getFramebufferHeight() { return framebufferHeight; }
	public void refreshFramebufferSize() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			java.nio.IntBuffer framebufferWidthBuffer = stack.mallocInt(1);
			java.nio.IntBuffer framebufferHeightBuffer = stack.mallocInt(1);
			GLFW.glfwGetFramebufferSize(window, framebufferWidthBuffer, framebufferHeightBuffer);
			framebufferWidth = framebufferWidthBuffer.get(0);
			framebufferHeight = framebufferHeightBuffer.get(0);
		}
	}
}
