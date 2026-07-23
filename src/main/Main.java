package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame("Touhou Java");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		GamePanel panel = new GamePanel();
		window.add(panel);
		window.pack();
		window.setLocationRelativeTo(null);
		
		window.setVisible(true);
		panel.startGameThread();
		panel.requestFocusInWindow();
		
	}
}