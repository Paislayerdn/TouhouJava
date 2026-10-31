package entity;

import action.ActionContext;
import collision.CollisionResult;
import collision.Hitbox;
import main.Input;
import state.gameplay.HUD;
import state.gameplay.PlayingStats;
import state.gameplay.BulletManager;

import static collision.CollisionType.DAMAGE;
import static collision.CollisionType.DEATH;

/** Common player entity contract shared by playable characters. */
public abstract class Player extends Entity {
	protected final HUD hud;
	protected final PlayingStats stats;
	private int invulnerabilityFrames;
	protected Boss boss;

	protected Player(HUD hud, PlayingStats stats, ActionContext parentContext) {
		this.hud = hud;
		this.stats = stats;
		setActionContext(new ActionContext(parentContext));
		setName("Player");
		setXY(0, -80);
	}

	protected final void updatePlayerState() {
		if (Input.tap.I) {
			Hitbox deathHitbox = getHitbox("deathHB");
			if (deathHitbox != null) {
				deathHitbox.setEnabled(!deathHitbox.isEnabled());
			}
		}

		if (invulnerabilityFrames > 0) {
			invulnerabilityFrames--;
			getAppearance().setGhost((invulnerabilityFrames / 6) % 2 == 0 ? 55 : 0);
		} else {
			getAppearance().setGhost(0);
		}
	}

	protected final void grantInvulnerabilityFrames(int frames) {
		invulnerabilityFrames = Math.max(invulnerabilityFrames, frames);
	}

	public final void setBoss(Boss boss) { this.boss = boss; }

	public void onDeath(Hitbox mine, Hitbox other) {
		if (!isAlive() || invulnerabilityFrames > 0) return;

		boolean hasLivesRemaining = stats.loseLife();
		stats.restoreMinimumBombsAfterDeath();
		if (hasLivesRemaining) {
			setXY(0, -80);
			invulnerabilityFrames = 120;
			if (getActionContext().has("bullets")) {
				Object manager = getActionContext().get("bullets");
				if (manager instanceof BulletManager bulletManager) {
					bulletManager.clearBomb();
				}
			}
		} else {
			setAlive(false);
		}
	}

	public final boolean isInvulnerable() { return invulnerabilityFrames > 0; }
	public final PlayingStats getStats() { return stats; }

	@Override
	public void onHit(CollisionResult collision) {
		if (!isAlive()) return;
		if (collision.getType() == DEATH) {
			if (isInvulnerable()) return;
			onDeath(collision.getSelf(this), collision.getOther(this));
		} else if (collision.getType() == DAMAGE) {
			setAlive(false);
		}
	}
}
