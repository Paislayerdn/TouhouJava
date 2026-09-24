package entity;

import graphics.Renderer;
import main.Input;

import static action.JScratch.*;

import collision.Hitbox;
import collision.CollisionResult;
import static collision.CollisionTag.*;
import static collision.CollisionType.*;

import state.gameplay.HUD;
import state.gameplay.PlayingStats;

public class Player extends Entity {
	private boolean focusing = false;

	private int shootCooldown = 0;
	private boolean autoFire = false;
	private boolean lastPageUp = false;

	private static final int SHOOT_INTERVAL = 6;
	private boolean lastX = false;
	
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

				AddCircleHitbox("deathHB", 2.5),
				AddHitboxTag("deathHB", PDEATH),
				
				Sound("fire", "[TH] Fires"),
				SetSoundVolume("fire", -12.0f)
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
		
		boolean bombPressed = Input.X && !lastX;
		if (bombPressed) {
			bomb();
		}
		lastX = Input.X;
		
		if (shootCooldown > 0) { shootCooldown--; }
		boolean firing = Input.Z || autoFire;
		if (firing && shootCooldown <= 0) {
			shoot();
			shootCooldown = SHOOT_INTERVAL;
		}
	}
	private void bomb() {
		HUD.showSCT("REIMU");
		int total = 12;
		run( Sequence(
			Wait(60),
			For("i", 1, total, 
				() -> Sequence(
					SpawnBullet(
						Parallel(
							Var("index", Get("i")),
							Warp(this),
							SetCostume("CircleBullet"),
							SetSize(120),
							SetBrightness(60),
							AddCircleHitbox("bulletHB", 70),
							AddHitboxTag("bulletHB", PLAYER_BULLET),
							AddHitboxTag("bulletHB", BOMB),
							Look( Random(1,360) ),

							Forever("Sequence",
								Forward(5),
								ChangeColor(2)
							),
							Sequence(
								PlaySound("fire"),
								Wait(300),
								Destroy()
							)
						)
					),
					Wait(15)
				)
			)
		) );
	}
	
	private void shoot() {
		int total = 4;
		run( Sequence (
			For("i", 1, total, 
				() -> SpawnBullet(
					Parallel(
						Var("index", Get("i")),
						Warp(this),
						MoveX( Mul( 15, Sub( Get("index"), (total+1.0)/2.0  )) ),
						MoveY(5),
						SetCostume("OvalBullet"),
						SetSize(9),
						SetColor(15),
						AddCircleHitbox("bulletHB", 6),
						AddHitboxTag("bulletHB", PLAYER_BULLET),
						Look(90),
						
						Forever("Sequence",
							Forward(10)
						),
						Sequence(
							PlaySound("fire"),
							Wait(300),
							Destroy()
						)
					)
				)
			)
		) );
	}
	
	@Override
	public void onHit(CollisionResult collision) {
		if (collision.getType() == DEATH) {
			onDeath(
				collision.getSelf(this),
				collision.getOther(this)
			);
			return;
		}

		if (collision.getType() == DAMAGE) {
			alive = false;
		}
	}
	
	public void onDeath(Hitbox mine, Hitbox other) {
		System.out.println("[Player] Pichu'd.");
	}
	
	@Override
	public void draw(Renderer renderer) {
		if (!focusing) this.run( SetColor(15) );
		else this.run( SetColor(40) );
		renderer.thing(this);
	}
}