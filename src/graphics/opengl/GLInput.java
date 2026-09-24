package graphics.opengl;

import static main.Input.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public final class GLInput extends GLFWKeyCallback {
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		boolean pressed = action != GLFW_RELEASE;

		switch (key) {
			case GLFW_KEY_W, GLFW_KEY_UP -> W = pressed;
			case GLFW_KEY_S, GLFW_KEY_DOWN -> S = pressed;

			case GLFW_KEY_A, GLFW_KEY_LEFT -> A = pressed;
			case GLFW_KEY_D, GLFW_KEY_RIGHT -> D = pressed;

			case GLFW_KEY_SPACE -> SPACE = pressed;

			case GLFW_KEY_Z -> Z = pressed;
			case GLFW_KEY_X -> X = pressed;
			case GLFW_KEY_PAGE_UP -> PAGEUP = pressed;

			case GLFW_KEY_P -> P = pressed;
		}
	}
}