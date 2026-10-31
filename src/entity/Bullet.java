package entity;

import state.gameplay.PlayingStats;

import java.util.Map;
import java.util.HashMap;

import resource.ResourceLoader;
import resource.Sound;

import collision.Hitbox;
import collision.CollisionResult;
import static collision.CollisionType.*;

public class Bullet extends Entity {
	private boolean grazable;
	private static final Map<Float, Sound> GRAZE_SOUNDS = new HashMap<>();


	public Bullet() {
		this(999,999);
	}
	public Bullet(float x, float y) {
		setXY(x, y);
		
		setName("Bullet");
		grazable = true;
	}
	
	private static Sound grazeSound(float volume) {
		return GRAZE_SOUNDS.computeIfAbsent(volume, v -> {
			Sound sound = ResourceLoader.sound("[TH] Graze");
			sound.setVolume(v);
			return sound;
		});
	}
	@Override
	public void onHit(CollisionResult collision) {
		if (collision.getType() == GRAZE) {
			onGraze(
				collision.getSelf(this),
				collision.getOther(this)
			);
			return;
		}

		if (collision.getType() == DAMAGE) {
			setAlive(false);
			return;
		}

		if (collision.getType() == DEATH) {
			setAlive(false);
		}
	}
	
	public void onGraze(Hitbox mine, Hitbox other) {
		if (grazable) {
			if (getActionContext().has("stats")) {
				Object stats = getActionContext().get("stats");
				if (stats instanceof PlayingStats playingStats) {
					playingStats.addGraze();
				}
			}
			grazeSound(0).play();
			grazable = false;
		} else {
//			System.out.println("[Bullet] Already grazed.");
		}
	}
}
