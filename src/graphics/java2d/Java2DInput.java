package graphics.java2d;

import java.awt.event.*;
import main.Input;

public final class Java2DInput implements KeyListener, MouseMotionListener, MouseListener {
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_UP ->
				Input.keyPressed(Input.Key.W);
			case KeyEvent.VK_S, KeyEvent.VK_DOWN ->
				Input.keyPressed(Input.Key.S);
			case KeyEvent.VK_A, KeyEvent.VK_LEFT ->
				Input.keyPressed(Input.Key.A);
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT ->
				Input.keyPressed(Input.Key.D);
			case KeyEvent.VK_SPACE ->
				Input.keyPressed(Input.Key.SPACE);
			case KeyEvent.VK_Z ->
				Input.keyPressed(Input.Key.Z);
			case KeyEvent.VK_X ->
				Input.keyPressed(Input.Key.X);
			case KeyEvent.VK_PAGE_UP ->
				Input.keyPressed(Input.Key.PAGEUP);
			case KeyEvent.VK_P ->
				Input.keyPressed(Input.Key.P);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_UP ->
				Input.keyReleased(Input.Key.W);
			case KeyEvent.VK_S, KeyEvent.VK_DOWN ->
				Input.keyReleased(Input.Key.S);
			case KeyEvent.VK_A, KeyEvent.VK_LEFT ->
				Input.keyReleased(Input.Key.A);
			case KeyEvent.VK_D, KeyEvent.VK_RIGHT ->
				Input.keyReleased(Input.Key.D);
			case KeyEvent.VK_SPACE ->
				Input.keyReleased(Input.Key.SPACE);
			case KeyEvent.VK_Z ->
				Input.keyReleased(Input.Key.Z);
			case KeyEvent.VK_X ->
				Input.keyReleased(Input.Key.X);
			case KeyEvent.VK_PAGE_UP ->
				Input.keyReleased(Input.Key.PAGEUP);
			case KeyEvent.VK_P ->
				Input.keyReleased(Input.Key.P);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Input.keyPressed(Input.Key.MOUSE_LEFT);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Input.keyReleased(Input.Key.MOUSE_LEFT);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		Input.mouseX = e.getX();
		Input.mouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
}