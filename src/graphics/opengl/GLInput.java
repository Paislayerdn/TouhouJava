package graphics.opengl;

import main.Input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

final class GLInput extends GLFWKeyCallback {
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_REPEAT) return;

		boolean pressed = action == GLFW_PRESS;
		Input.Key mappedKey = switch (key) {
			case GLFW_KEY_W -> Input.Key.W;
			case GLFW_KEY_S -> Input.Key.S;
			case GLFW_KEY_A -> Input.Key.A;
			case GLFW_KEY_D -> Input.Key.D;
			case GLFW_KEY_UP -> Input.Key.UP;
			case GLFW_KEY_DOWN -> Input.Key.DOWN;
			case GLFW_KEY_LEFT -> Input.Key.LEFT;
			case GLFW_KEY_RIGHT -> Input.Key.RIGHT;
			case GLFW_KEY_SPACE -> Input.Key.SPACE;
			case GLFW_KEY_Z -> Input.Key.Z;
			case GLFW_KEY_X -> Input.Key.X;
			case GLFW_KEY_PAGE_UP -> Input.Key.PAGEUP;
			case GLFW_KEY_P -> Input.Key.P;
			case GLFW_KEY_Q -> Input.Key.Q;
			default -> null;
		};

		if (mappedKey == null) return;

		if (pressed) {
			Input.keyPressed(mappedKey);
		} else if (action == GLFW_RELEASE) {
			Input.keyReleased(mappedKey);
		}
	}
}

final class GLMouse extends GLFWCursorPosCallback {
	@Override
	public void invoke(long window, double xpos, double ypos) {
		Input.mouseX = (float) xpos;
		Input.mouseY = (float) ypos;
	}
}

final class GLMouseButton extends GLFWMouseButtonCallback {
	@Override
	public void invoke(long window, int button, int action, int mods) {
		if (button != GLFW_MOUSE_BUTTON_LEFT) return;

		if (action == GLFW_PRESS) {
			Input.keyPressed(Input.Key.MOUSE_LEFT);
		} else if (action == GLFW_RELEASE) {
			Input.keyReleased(Input.Key.MOUSE_LEFT);
		}
	}
}
