package entity;

import resource.ResourceLoader;
import resource.Sound;

import collision.CollisionResult;
import static collision.CollisionType.*;
import static collision.CollisionTag.*;

import static action.JScratch.*;
import action.ActionContext;
import graphics.Renderer;

public class Boss extends Entity {
	private final Sound lowHP;
	private Player player;
	
	private float maxHP = 0, hp = 0;
	private float displayedHP = 0;
	private boolean lowHPPlayed;

	public Boss(Player player, ActionContext parentContext) {
		setActionContext(new ActionContext(parentContext));
		setName("Boss");
		this.player = player;
		setXY(0, 120);
		this.lowHP = ResourceLoader.sound("[TH] LowHP");
		lowHP.setVolume(0.0f);
		
		this.run(
			Sequence(
				SetCostume("BulletCircle"),
				SetColor(120),
				SetBrightness(-20),
				SetSize(80),
				AddRectangleHitbox("bossHB", 85, 90),
				AddHitboxTag("bossHB", BOSS)
			)
		);
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
		this.hp = maxHP;
		this.displayedHP = maxHP;
		this.lowHPPlayed = false;
	}

	public float getMaxHP() { return maxHP; }
	public float getHP() { return hp; }
	public float getDisplayedHP() { return displayedHP; }

	@Override
	public void update() {
		super.update();
		if (maxHP <= 0) return;
		displayedHP += (hp - displayedHP) * 0.12f;
		if (Math.abs(hp - displayedHP) < 0.05f) displayedHP = hp;
	}

	@Override
	public void draw(Renderer renderer) {
		super.draw(renderer);
		if (maxHP <= 0) return;
		float fraction = Math.max(0, Math.min(1, displayedHP / maxHP));
		renderer.circleArc(getX(), getY(), 50, 90, -360, 0.2f, 0.2f, 0.2f, 6);
		renderer.circleArc(getX(), getY(), 50, 90, -360 * fraction,
			1 - fraction, fraction, 0, 6);
	}
	
	public void damage(float amount) {
		if (hp <= 0) { hp = 0; } else {
			hp = Math.max(0, hp - amount);
			if (!lowHPPlayed && hp <= 0.15 * maxHP) {
				lowHP.play();
				lowHPPlayed = true;
			}
//			System.out.println(hp);
		}
	}
	@Override
	public void onHit(CollisionResult collisionResult) {
		if (collisionResult.getType() == DAMAGE) {
			damage(1);
		}
	}
}
