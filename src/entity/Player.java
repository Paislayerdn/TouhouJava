package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import collision.Hitbox;
import static collision.Hitboxes.*;

import graphics.Depict;

import input.Input;

public class Player extends Entity {
	private boolean focusing = false;

	public Player() {
		name = "Player";
		
		x = 0;
		y = -80;

		addHitbox( circleHB(this, "grazeHB", 5) );
		addHitbox( circleHB(this, "deathHB", 2) );
		this.hitboxes.get(1).setEnabled(false);
	}
	
	@Override
	public void update() {
		updateActions();
		
		double speed = 4.2;
		focusing = Input.focus;
		
		if (focusing) { speed = 2; }

		double dx = 0;
		double dy = 0;

		if (Input.up) dy++;
		if (Input.down) dy--;
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
	public void onHit(Hitbox mine, Hitbox other) {
//		System.out.println(
//			"[Player] "
//			+ mine.getName()
//			+ " was hit by "
//			+ other.getName()
//		);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		if (!focusing) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);
		Depict.oval(g2, x, y, 20, 20);
	}
}