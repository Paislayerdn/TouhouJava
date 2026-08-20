package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseMotionListener {
	public static boolean up;
	public static boolean down;
	public static boolean left;
	public static boolean right;
	public static boolean focus;
	
	public static double mouseX;
	public static double mouseY;
	public static boolean debug;

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

			case KeyEvent.VK_W, KeyEvent.VK_UP -> up = true;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = true;

			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = true;

			case KeyEvent.VK_SPACE -> focus = true;

			case KeyEvent.VK_P -> debug = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {

			case KeyEvent.VK_W, KeyEvent.VK_UP -> up = false;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = false;

			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = false;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = false;

			case KeyEvent.VK_SPACE -> focus = false;
			
			case KeyEvent.VK_P -> debug = false;

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}