package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import game.Game;
import input.Input;


public class GamePanel extends JPanel implements Runnable {
	private Thread gameThread;
	private Game game;
	private Input input;
	
	public static final int WIDTH = 960;
	public static final int HEIGHT = 720;
	
	public void startGameThread() {
		gameThread = new Thread(this);

		gameThread.start();
	}
	
	public GamePanel(){
		this.setPreferredSize(
			new Dimension(
				Settings.WINDOW_WIDTH,
				Settings.WINDOW_HEIGHT
			)
		);
		
		this.setBackground(Color.BLACK);
		
		game = new Game();
		input = new Input();
		
		this.addKeyListener(input);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	@Override
	public void run() {
		while (true) {
			game.update();
			repaint();

			try {
				Thread.sleep(16);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		game.draw(g2);
		g2.dispose();

	}
}