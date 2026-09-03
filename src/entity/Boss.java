package entity;

import java.awt.Graphics2D;
import java.awt.Color;

import graphics.Depict;

import resource.ResourceLoader;
import resource.Sound;
import resource.Music;

import collision.Hitbox;
import static collision.Hitboxes.*;
import collision.CollisionResult;
import static collision.CollisionType.*;

import spell.LuaSpell;
import spell.Phyllotaxis;
import spell.TestSpell;

import dialogue.DialogueParser;
import dialogue.DialogueRunner;

public class Boss extends Entity {
	private Music ost;
	private Sound lowHP;
	private Player player;
	
	private double maxHP=0;
	private double hp=0;
	
	private int timer = 0;
	
	private DialogueRunner dialogueRunner;

	public Boss(Player player) {
		name = "Boss";
		this.player = player;
		x = 0;
		y = 120;
		this.ost = ResourceLoader.music("PACHAD");
		ost.setVolume(-22.5f);
		this.lowHP = ResourceLoader.sound("[TH] LowHP");
		lowHP.setVolume(-10.0f);
		
		addHitbox(rectangleHB(this, "bossHB", 85, 90));
		getHitbox("bossHB").addTag("BOSS");
//		dialogueRunner = new DialogueRunner("Test");
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
		this.hp = maxHP;
	}

	public double getMaxHP() { return maxHP; }
	public double getHP() { return hp; }
	public void damage(double amount) {
		if (hp < 0) { hp = 0; } else {
			hp -= amount;
			if (hp/maxHP<0.15) lowHP.play();
			System.out.println(hp);
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

			if (dialogueRunner.isFinished())
				dialogueRunner = null;
		}
		if (timer == 0) {
			ost.play();
//			run(new TestSpell(this, player));
			run(new LuaSpell(this, player, "Eirin"));
		}
		
		timer++;
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		Depict.circle(g2, x, y, 80);
	}
}