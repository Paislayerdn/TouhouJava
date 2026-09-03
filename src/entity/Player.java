package entity;

import java.awt.Graphics2D;
import java.awt.Color;

import graphics.Depict;
import input.Input;

import game.BulletManager;
import static collision.Hitboxes.*;
import static collision.CollisionTags.*;

import static action.JScratch.*;

public class Player extends Entity {
	private boolean focusing = false;

	private int shootCooldown = 0;
	private boolean autoFire = false;
	private boolean lastPageUp = false;

	private static final int SHOOT_INTERVAL = 6;
	
	public Player() {
		name = "Player";
		
		x = 0;
		y = -80;

		addHitbox(circleHB(this, "grazeHB", 5));
		getHitbox("grazeHB").addTag(PGRAZE);

		addHitbox(circleHB(this, "deathHB", 2));
		getHitbox("deathHB").addTag(PDEATH);
		getHitbox("deathHB").setEnabled(false);
	}
	
	@Override
	public void update() {
		updateActions();
		
		double speed = 4.2;
		focusing = Input.SPACE;
		if (focusing) { speed = 2; }

		double dx = 0;	double dy = 0;
		if (Input.W) dy++;	if (Input.S) dy--;
		if (Input.A) dx--;	if (Input.D) dx++;

		double length = Math.sqrt(dx * dx + dy * dy);

		if (length > 0) {
			dx /= length;	dy /= length;
			x += dx * speed;	y += dy * speed;
		}
		
		boolean pageUpPressed = Input.PAGEUP && !lastPageUp;
		if (pageUpPressed) { autoFire = !autoFire;}
		lastPageUp = Input.PAGEUP;
		
		if (shootCooldown > 0) { shootCooldown--; }
		boolean firing = Input.Z || autoFire;
		if (firing && shootCooldown <= 0) {
			shoot();
			shootCooldown = SHOOT_INTERVAL;
		}
	}
	
	private void shoot() {
		Bullet bullet = new Bullet(x, y + 12);

		bullet.run(
			Parallel(
				AddCircleHitbox("bulletHB", 12),
				AddHitboxTag("bulletHB", "PLAYER_BULLET"),
				Look(90),
				MoveX( Mul( Random(), 15) ),
				MoveY( Mul( Random(), 3) ),
				Forever("Sequence",
					Forward(10)
				),
				Sequence(
					Sound("fire", "[TH] Fires"),
					GetSound("fire").setVolume(-5.0f),
					GetSound("fire").play(),
					Wait(300),
					Destroy()
				)
			)
		);

		BulletManager.spawn(bullet);
	}
	
//	@Override
//	public void onHit(Hitbox mine, Hitbox other) {
//		System.out.println(
//			"[Player] "
//			+ mine.getName()
//			+ " was hit by "
//			+ other.getName()
//		);
//	}
	
	@Override
	public void draw(Graphics2D g2) {
		if (!focusing) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);
		Depict.oval(g2, x, y, 20, 20);
	}
}