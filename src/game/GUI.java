package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Settings;
import resource.ResourceLoader;

public class GUI {
	private final BufferedImage foreground;

	public GUI() {
		foreground = ResourceLoader.image("Foreground");
	}

	public void update() {
		
	}

	public void draw(Graphics2D g2) {
		g2.drawImage(foreground, 0, 0, Settings.BASE_WIDTH, Settings.BASE_HEIGHT, null);

	}
}