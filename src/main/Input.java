package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseMotionListener, MouseListener {
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
	public static boolean mousePressed;
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:		W = true; break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:		S = true; break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:		A = true; break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:		D = true; break;
				
			case KeyEvent.VK_SPACE:		SPACE = true; break;
			
			case KeyEvent.VK_Z:			Z = true; break;
			case KeyEvent.VK_PAGE_UP:	PAGEUP = true; break;

			case KeyEvent.VK_P:			P = true; break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:		W = false; break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:		S = false; break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:		A = false; break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:		D = false; break;

			case KeyEvent.VK_SPACE:		SPACE = false; break;
			
			case KeyEvent.VK_Z:			Z = false; break;
			case KeyEvent.VK_PAGE_UP:	PAGEUP = false; break;
			
			case KeyEvent.VK_P:			P = false; break;

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