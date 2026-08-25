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
	protected Action buildAction() {
		int count = 350;
		double step = 2.25;
		double goldenAngle = 360*( 1-2/( 1+Math.sqrt(5) ) );
		return Forever("Sequence",
			Var("speed", 0.15),
			Var("offset", Mul(Random(), 360) ),
			Sound("jingle", "[TH] Jingle"),
			GetSound("jingle").setVolume(-0.25f),
			GetSound("jingle").play(),
			Sound("shot", "[TH] Shot"),
			GetSound("shot").setVolume(-27.5f),
			For("i", 1, count,
				() -> SpawnBullet(Get("i"),
					Sequence(
						GoTo(999,999),
						Wait(Add(Mul(Get("i"), 0.35)) ),
						Parallel(
							Sequence(
								GoTo(boss),
								Look(0),
								Turn(Mul(Get("i"), goldenAngle) ),
								Forward( Mul(Get("i"), step) ),
								GetSound("shot").play(),
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
								Wait(360),
								Destroy()
							)
						)
					)
				)
			),
			Wait(150)
		);
	}
}