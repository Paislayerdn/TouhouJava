package spell;

import action.Spell;
import action.Action;
import static action.JScratch.*;

import entity.Boss;
import entity.Player;

public final class Ariya extends Spell {
	public Ariya(Boss boss, Player player) {
		super(boss, player);
		configure();
	}
	
	@Override
	public void configure() {
		name = "Replicated [Pebbles That Become Boulders]";
		playerCandidateRadius = 40;
		timer = 3600;
		countableTime = timer;
		isSpell = true;
		caster = "LAMBDA";
	}
	
	@Override
	public void onStart() {
		boss.setMaxHP(75);
		startTimer();
		startCounting();
	}
	
	@Override
	protected Action buildAction() {
		int count = 800;
		float step = 0.65f;
		float waitIteration = 0.125f;
		float goldenAngle = 360*( 1-2/( 1+ (float) Math.sqrt(5) ) );
		return Sequence(
			Var("offset", Mul(Random(), 360) ),
			
			Sound("jingle", "[TH] Jingle", -0.25f),
			Sound("shot", "[TH] Shot", -17.5f),

			Forever("Sequence",
				PlaySound("jingle"),
				For("i", 1, count,
					() -> bullete(goldenAngle, step, waitIteration)
				),
				Wait(300)
			)
		);
	}
	
	private Action bullete(float goldenAngle, float step, float waitIteration) {
		float capSpeed = -3.5f;
		return SpawnBullet( Sequence(
			Replicate("i"),
			Var("speed", 0.30f),

			SetGhost(100),
			SetCostume("BulletOval"),
			AddCircleHitbox("bulletHB", 4),
			AddHitboxTag("bulletHB", "ENEMY_BULLET", "CLEARABLE"),
			DisableHitbox("bulletHB"),
			GoTo(boss), Look( Get("offset") ),
			Turn(Mul(Get("i"), goldenAngle) ),
			Forward( Mul(Get("i"), step) ),
			Wait(Add(Mul(Get("i"), waitIteration), 1) ),
			If(Less(Distance(player), 100),
				() -> {
					return Destroy();
				}
			),
			Parallel(
				Sequence(
					PlaySound("shot"),
					Forever("Sequence",
						Forward( Get("speed") ),
						ChangeDesaturation(-0.1),
						ChangeBrightness(-0.1),
						Wait()
					)
				),
				Tween("color", 240, 130, 180),
				Sequence(
					Tween("size", 30, 9,
						"brightness", 100, 60,
						"ghost", 90, 0, 45),
					EnableHitbox("bulletHB"),
					Var("aa", Random(1, 360) ),
					Turn( Get("aa") , 60),
					While( Greater( Get("speed"), capSpeed),
						() -> Sequence(
							Change("speed", -0.035f),
							Wait()
						)
					)
				),
				Sequence(
					Wait(600),
					DisableHitbox("bulletHB"),
					Tween("ghost", 100, 60),
					Destroy()
				)
			)
		) );
	}
}
