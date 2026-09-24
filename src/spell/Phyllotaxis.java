package spell;

import action.Spell;
import action.Action;
import static action.JScratch.*;

import entity.Boss;
import entity.Player;

public final class Phyllotaxis extends Spell {
	public Phyllotaxis(Boss boss, Player player) {
		super(boss, player);
		configure();
	}
	
	@Override
	public void configure() {
		name = "Replication Sign \"Digitalized Pebbles\"";
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
		int count = 270;
		float step = 2f;
		float waitIteration = 0.2f;
		float goldenAngle = 360*( 1-2/( 1+ (float) Math.sqrt(5) ) );
		return Sequence(
			Var("offset", Mul(Random(), 360) ),
			
			Sound("jingle", "[TH] Jingle"),
			SetSoundVolume("jingle", -0.25f),
			
			Sound("shot", "[TH] Shot"),
			SetSoundVolume("shot", -17.5f),

			Forever("Sequence",
				PlaySound("jingle"),
				For("i", 1, count,
					() -> bullete(goldenAngle, step, waitIteration)
				),
				Wait(150)
			)
		);
	}
	
	private Action bullete(float goldenAngle, float step, float waitIteration) {
		float capSpeed = -4.0f;
		return SpawnBullet(
			Sequence(
				Var("index", Get("i")),
				Var("speed", 0.15),
				
				SetCostume("OvalBullet"),
				AddCircleHitbox("bulletHB", 5),
				AddHitboxTag("bulletHB", "ENEMY_BULLET"),
				AddHitboxTag("bulletHB", "CLEARABLE"),
				DisableHitbox("bulletHB"),
				Wait(Add(Mul(Get("index"), waitIteration), 1) ),
				Parallel(
					Sequence(
						GoTo(boss), Look( Get("offset") ),
						Turn(Mul(Get("index"), goldenAngle) ),
						Forward( Mul(Get("index"), step) ),
						PlaySound("shot"),
						Forever("Sequence",
							Forward( Get("speed") )
						)
					),
					Sequence(
						Wait(30),
						While( Greater( Get("speed"), capSpeed),
							() -> Sequence(
								Change("speed", -0.035),
								Wait()
							)
						)
					),
					Sequence(
						Wait(480), Destroy()
					),
					Tween("color", 240, 130, 90),
					Sequence(
						Tween("size", 30, 11, "brightness", 100, 60, "ghost", 90, 0, 45),
						EnableHitbox("bulletHB")
					)
				)
			)
		);
	}
}