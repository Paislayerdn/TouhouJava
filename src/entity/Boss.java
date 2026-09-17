package entity;

import resource.ResourceLoader;
import resource.Sound;

import collision.CollisionResult;
import static collision.CollisionType.*;
import static collision.CollisionTags.*;

import static action.JScratch.*;
import spell.*;

import dialogue.DialogueRunner;

public class Boss extends Entity {
	private Sound lowHP;
	private Player player;
	
	private float maxHP=0;
	private float hp=0;
	
	private int timer = 0;
	
	private DialogueRunner dialogueRunner;

	public Boss(Player player) {
		name = "Boss";
		this.player = player;
		x = 0;
		y = 120;
		this.lowHP = ResourceLoader.sound("[TH] LowHP");
		lowHP.setVolume(0.0f);
		
		this.run(
			Sequence(
				SetCostume("CircleBullet"),
				SetColor(120),
				SetBrightness(-20),
				SetSize(80),
				AddRectangleHitbox("bossHB", 85, 90),
				AddHitboxTag("bossHB", BOSS)
			)
		);
//		run(new Phyllotaxis(this, player));
		this.run(new LuaSpell(this, player, "DoubleBullet"));
//		dialogueRunner = new DialogueRunner("Test");
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
		this.hp = maxHP;
	}

	public float getMaxHP() { return maxHP; }
	public float getHP() { return hp; }
	
	public void damage(float amount) {
		if (hp < 0) { hp = 0; } else {
			hp -= amount;
			if (hp/maxHP<0.15) lowHP.play();
//			System.out.println(hp);
		}
	}
	@Override
	public void onHit(CollisionResult collisionResult) {
		if (collisionResult.getType() == DAMAGE) {
			damage(1);
		}
	}

	@Override
	public void update() {
		updateActions();
		if (dialogueRunner != null) {
			dialogueRunner.update();

			if (dialogueRunner.isFinished()) dialogueRunner = null;
		}
		timer++;
	}
}