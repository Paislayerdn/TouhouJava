package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;

import graphics.Java2DRenderer;

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
		this.setPreferredSize( new Dimension(
			Settings.getWindowWidth(), Settings.getWindowHeight()
		));
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
		double frameTime = 1_000_000_000.0 / Settings.FPS;
		long nextFrame = System.nanoTime();

		while (true) {
			game.update();
			repaint();

			nextFrame += (long) frameTime;

			long remaining = nextFrame - System.nanoTime();

			if (remaining > 0) {
				try {
					Thread.sleep(
						remaining / 1_000_000,
						(int)(remaining % 1_000_000)
					);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			} else {
				// The frame took longer than the target frame time.
				nextFrame = System.nanoTime();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		g2.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR
		);

		// Move origin from top-left to screen center.
		g2.translate( getWidth() / 2.0, getHeight() / 2.0 );
		// Cartesian coordinates: +Y goes UP.
		g2.scale(1, -1);
		g2.scale(Settings.SCALE, Settings.SCALE);

		Java2DRenderer renderer = new Java2DRenderer(g2);
		game.draw(renderer);
		g2.dispose();
	}
}