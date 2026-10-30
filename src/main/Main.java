package main;

import graphics.opengl.GLRenderer;
import graphics.opengl.GLWindow;

import javax.swing.JFrame;

import resource.ResourceLoader;

public class Main {
	private static final boolean OPENGL = true;
	
	public static void main(String[] args) {
		if (OPENGL) { runOpenGL(); } else { runJava2D(); }
	}

	private static void runJava2D() {
		JFrame window = new JFrame("Touhou Java");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setIconImage(ResourceLoader.image("Icon"));

		GamePanel panel = new GamePanel();
		window.add(panel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		panel.startGameThread();
		panel.requestFocusInWindow();
	}
	
	private static void runOpenGL() {
		GLWindow window = new GLWindow(
				Settings.getWindowWidth(), Settings.getWindowHeight(), "Touhou JaVA"
		);
		window.setIcon(ResourceLoader.image("Icon"));

		GLRenderer renderer = new GLRenderer(
			window.getFramebufferWidth(),
			window.getFramebufferHeight()
		);

		Game game = new Game();

		long nextFrame = System.nanoTime();

		while (!window.shouldClose() && !game.isQuitting()) {
			double frameTime = 1_000_000_000.0 / Settings.FPS;

			renderer.clear();

			Input.update();
			game.update();
			game.draw(renderer);

			window.update();
			window.refreshFramebufferSize();
			renderer.updateFramebufferSize(
				window.getFramebufferWidth(),
				window.getFramebufferHeight()
			);

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
				nextFrame = System.nanoTime();
			}
		}

		window.destroy();
	}
}
