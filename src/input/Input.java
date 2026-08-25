package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseMotionListener {
	public static boolean W;
	public static boolean S;
	public static boolean A;
	public static boolean D;
	public static boolean SPACE;
	public static boolean Z;
	
	public static double mouseX;
	public static double mouseY;
	public static boolean P;
	public static boolean PAGEUP;

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

			case KeyEvent.VK_W, KeyEvent.VK_UP -> W = true;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> S = true;

			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> A = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> D = true;

			case KeyEvent.VK_SPACE -> SPACE = true;
			
			case KeyEvent.VK_Z -> Z = true;
			case KeyEvent.VK_PAGE_UP -> PAGEUP = true;

			case KeyEvent.VK_P -> P = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {

			case KeyEvent.VK_W, KeyEvent.VK_UP -> W = false;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> S = false;

			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> A = false;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> D = false;

			case KeyEvent.VK_SPACE -> SPACE = false;
			
			case KeyEvent.VK_Z -> Z = false;
			case KeyEvent.VK_PAGE_UP -> PAGEUP = false;
			
			case KeyEvent.VK_P -> P = false;

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