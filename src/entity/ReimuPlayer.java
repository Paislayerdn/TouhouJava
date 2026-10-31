package entity;

import graphics.Renderer;
import main.Input;

import static collision.CollisionTag.*;

import static action.JScratch.*;

import action.ActionContext;
import state.gameplay.HUD;
import state.gameplay.PlayingStats;

public final class ReimuPlayer extends Player {
	private boolean focusing = false;

	private int shootCooldown = 0;
	private boolean autoFire = false;
	private int bombFramesRemaining;

	private static final int SHOOT_INTERVAL = 6;

	public ReimuPlayer(HUD hud, PlayingStats stats, ActionContext parentContext) {
		super(hud, stats, parentContext);
		run(
			Sequence(
				SetCostume("BulletStar"),
				Look(90),
				SetSize(26),
				AddCircleHitbox("grazeHB", 9),
				AddHitboxTag("grazeHB", PGRAZE),

				AddCircleHitbox("deathHB", 2.5),
				AddHitboxTag("deathHB", PDEATH),

				Sound("fire", "[TH] Fires", -12.0f)
			)
		);
	}

	@Override
	public void update() {
		updateActions();
		updatePlayerState();
		if (bombFramesRemaining > 0) bombFramesRemaining--;

		float speed = 4.8f;
		focusing = Input.hold.SPACE;
		if (focusing) speed = 2;

		float dx = 0;
		float dy = 0;
		if (Input.hold.W || Input.hold.UP) dy++;
		if (Input.hold.S || Input.hold.DOWN) dy--;
		if (Input.hold.A || Input.hold.LEFT) dx--;
		if (Input.hold.D || Input.hold.RIGHT) dx++;

		float length = (float) Math.sqrt(dx * dx + dy * dy);
		if (length > 0) {
			dx /= length;
			dy /= length;
			move(dx * speed, dy * speed);
		}

		if (Input.tap.PAGEUP) autoFire = !autoFire;
		if (Input.tap.X && bombFramesRemaining == 0 && stats.useBomb()) bomb();

		if (shootCooldown > 0) shootCooldown--;
		boolean firing = Input.hold.Z || autoFire;
		if (firing && shootCooldown <= 0) {
			shoot();
			shootCooldown = SHOOT_INTERVAL;
		}
	}

	private void bomb() {
		bombFramesRemaining = 60 + 12 * 15 + 300;
		grantInvulnerabilityFrames(180);
		hud.showSCT("REIMU");
		Object manager = getActionContext().get("bullets");
		if (manager instanceof state.gameplay.BulletManager bulletManager) {
			bulletManager.clearBomb();
		}
		int total = 12;
		run(Sequence(
			Wait(60),
			For("i", 1, total,
				() -> Sequence(
					SpawnBullet(
						Parallel(
							Replicate("i"),
							Warp(this),
							SetCostume("BulletCircle"),
							SetSize(120),
							SetBrightness(60),
							AddCircleHitbox("bulletHB", 70),
							AddHitboxTag("bulletHB", PLAYER_BULLET, BOMB),
							Look(Mul(30, Sub(Get("i"), 1))),
							Sequence(Wait(30), LookTowards(boss, 90)),
							Forever("Sequence", Forward(10), ChangeColor(2), Wait()),
							Sequence(PlaySound("fire"), Wait(300), Destroy())
						)
					),
					Wait(15)
				)
			)
		));
	}

	private void shoot() {
		int total = 4;
		run(Sequence(
			For("i", 1, total,
				() -> SpawnBullet(
					Parallel(
						Replicate("i"),
						Warp(this),
						MoveX(Mul(15, Sub(Get("i"), (total + 1.0) / 2.0))),
						MoveY(5),
						Look(90),
						SetCostume("BulletOval"),
						SetSize(9), SetColor(15),
						AddCircleHitbox("bulletHB", 6),
						AddHitboxTag("bulletHB", PLAYER_BULLET),
						Forever("Sequence", Forward(20), Wait()),
						Sequence(PlaySound("fire"), Wait(300), Destroy())
					)
				)
			)
		));
	}

	@Override
	public void draw(Renderer renderer) {
		run(SetDesaturation(focusing ? 100 : 0));
		renderer.thing(this);
	}
}
