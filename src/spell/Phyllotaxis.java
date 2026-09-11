package spell;

import action.Spell;
import action.Action;
import static action.JScratch.*;

import entity.Boss;
import entity.Player;

public class Phyllotaxis extends Spell {
	private final Player player;

	public Phyllotaxis(Boss boss, Player player) {
		super(boss, "Replication Sign \"Digitalized Pebbles\"");
		this.player = player;
	}
	
	@Override
	public void onStart() {
		boss.setMaxHP(1000);
	}
	
	private Action bullete(double goldenAngle, double step) {
		double maxBright = 60;
		return SpawnBullet(
			Sequence(
				SetCostume("OvalBullet"),
				SetSize(11), SetColor(240),
				SetBrightness(maxBright), SetGhost(90),
				AddCircleHitbox("bulletHB", 7),
				AddHitboxTag("bulletHB", "ENEMY_BULLET"),

				Var("index", Get("i")),
				Var("speed", 0.15),
				GoTo(999,999),
				Wait(Add(Mul(Get("index"), 0.35)) ),
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
						Wait(35),
						Forever("Sequence",
							Change("speed", -0.035)
						)
					),
					Sequence(
						Wait(360), Destroy()
					),
					While( More( Get("color"), 45),
						() -> Sequence(
							ChangeColor(-1),
							Wait(2)
						)
					),
					Sequence(
						While( More( Get("ghost"), 0),
							() -> Sequence(
								ChangeGhost(-2)
							)
						),
						SetGhost(0)
					)
				)
			)
		);
	}
	
	@Override
	protected Action buildAction() {
		int count = 250;
		double step = 4;
		double goldenAngle = 360*( 1-2/( 1+Math.sqrt(5) ) );
		return Forever("Sequence",
			Var("offset", Mul(Random(), 360) ),
			
			Sound("jingle", "[TH] Jingle"),
			SetSoundVolume("jingle", -0.25f),
			PlaySound("jingle"),
			
			Sound("shot", "[TH] Shot"),
			SetSoundVolume("shot", -27.5f),

			For("i", 1, count,
				() -> bullete(goldenAngle, step)
			),
			Wait(150)
		);
	}
}