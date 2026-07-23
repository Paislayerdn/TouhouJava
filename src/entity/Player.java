package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Settings;

import input.Input;

public class Player extends Entity {

	private boolean focusing = false;

	public Player() {
		x = Settings.GAME_WIDTH / 2.0;
		y = Settings.GAME_HEIGHT - 80;
	}
	
	@Override
	public void update() {

		double speed = 4.2;
		focusing = Input.focus;
		
		if (focusing) {
			speed = 2;
		}

		double dx = 0;
		double dy = 0;

		if (Input.up) dy--;

		if (Input.down) dy++;

		if (Input.left) dx--;

		if (Input.right) dx++;

		double length = Math.sqrt(dx * dx + dy * dy);

		if (length > 0) {
			dx /= length; dy /= length;

			x += dx * speed;
			y += dy * speed;
		}

	}
	
	@Override
	public void draw(Graphics2D g2) {
		if (!focusing) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);
		g2.fillOval((int)x - 10, (int)y - 10, 20, 20);

	}
}