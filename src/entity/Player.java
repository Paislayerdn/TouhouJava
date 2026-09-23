package entity;

import java.awt.Graphics2D;
import java.awt.Color;

import graphics.java2d.Depict;
import main.Input;

import state.gameplay.BulletManager;
import static collision.Hitboxes.*;
import static collision.CollisionTags.*;

import static action.JScratch.*;
import graphics.Renderer;

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
		
		this.run(
			Sequence(
				SetCostume("CircleBullet"),
				SetSize(14),
				AddCircleHitbox("grazeHB", 7),
				AddHitboxTag("grazeHB", PGRAZE),

				AddCircleHitbox("deathHB", 4),
				AddHitboxTag("deathHB", PDEATH),
				DisableHitbox("deathHB")
			)
		);
	}
	
	@Override
	public void update() {
		updateActions();
		
		float speed = 4.2f;
		focusing = Input.SPACE;
		if (focusing) { speed = 2; }

		float dx = 0;	float dy = 0;
		if (Input.W) dy++;	if (Input.S) dy--;
		if (Input.A) dx--;	if (Input.D) dx++;

		float length = (float) Math.sqrt(dx * dx + dy * dy);

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
				SetCostume("OvalBullet"),
				SetSize(9),
				SetColor(15),
				AddCircleHitbox("bulletHB", 6),
				AddHitboxTag("bulletHB", PLAYER_BULLET),
				Look(90),
				MoveX( Mul( Random(), 15) ),
				MoveY( Mul( Random(), 3) ),
				Forever("Sequence",
					Forward(10)
				),
				Sequence(
					Sound("fire", "[TH] Fires"),
					SetSoundVolume("fire", -5.0f),
					PlaySound("fire"),
					Wait(300),
					Destroy()
				)
			)
		);

		BulletManager.spawnPlayer(bullet);
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
	public void draw(Renderer renderer) {
		if (!focusing) this.run( SetColor(15) );
		else this.run( SetColor(40) );
		renderer.thing(this);
	}
}