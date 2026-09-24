package graphics.java2d;

import java.awt.event.*;
import static main.Input.*;

public final class Java2DInput implements KeyListener, MouseMotionListener, MouseListener {
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_UP -> W = true;
			case KeyEvent.VK_S, KeyEvent.VK_DOWN -> S = true;

			case KeyEvent.VK_A, KeyEvent.VK_LEFT -> A = true;
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> D = true;

			case KeyEvent.VK_SPACE -> SPACE = true;
			
			case KeyEvent.VK_Z -> Z = true;
			case KeyEvent.VK_X -> X = true;
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
			case KeyEvent.VK_X -> X = false;
			case KeyEvent.VK_PAGE_UP -> PAGEUP = false;
			
			case KeyEvent.VK_P -> P = false;

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

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