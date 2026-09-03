package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;

import game.Game;
import input.Input;

public class GamePanel extends JPanel implements Runnable {
	private Thread gameThread;
	private Game game;
	private Input input;
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void initialize() {
		javax.swing.SwingUtilities.invokeLater(this::requestFocusInWindow);
		setFocusTraversalKeysEnabled(false);
	}
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT ) );
		
		this.setBackground(Color.BLACK);
		
		game = new Game();
		input = new Input();
		
		this.addKeyListener(input);
		this.addMouseMotionListener(input);
		this.setFocusable(true);
		this.initialize();
	}
	
	@Override
	public void run() {
		while (true) {
			game.update();
			repaint();

			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		g2.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR
		);
		game.draw(g2);
		g2.dispose();

	}
}