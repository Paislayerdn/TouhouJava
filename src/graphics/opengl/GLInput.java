package graphics.opengl;

import static main.Input.*;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

final class GLInput extends GLFWKeyCallback {
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

final class GLMouse extends GLFWCursorPosCallback {
	@Override
	public void invoke(long window, double xpos, double ypos) {
		mouseX = (float) xpos;
		mouseY = (float) ypos;
	}
}

final class GLMouseButton extends GLFWMouseButtonCallback {
	@Override
	public void invoke(long window, int button, int action, int mods) {
		boolean pressed = action != GLFW_RELEASE;

		if (button == GLFW_MOUSE_BUTTON_LEFT)
			mousePressed = pressed;
	}
}