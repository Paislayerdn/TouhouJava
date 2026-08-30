package spell;

import action.Spell;
import action.Action;
import static action.JScratch.*;

import entity.Boss;
import entity.Player;

public class TestSpell extends Spell {
	private final Player player;

	public TestSpell(Boss boss, Player player) {
		super(boss, "Test Spell");
		this.player = player;
	}
	
	@Override
	public void onStart() {
		boss.setMaxHP(1000);
	}

	@Override
	protected Action buildAction() {
		int spokes = 7;
		double spread = 35; 
		return Sequence(
			Var("offset",0),
			Forever("Sequence",
				For("i", 1, spokes, 
					() -> SpawnBullet(Get("i"),
						Par(
							Sequence(
								GoTo(boss),
								Look(0),
								Turn( Get("offset") ),
								Turn( Mul(spread, Sub(Get("i"), (spokes+1)/2) ) ),
								Var("speed", 2),
								Forever("Sequence",
									Forward(Get("speed")),
									Change("speed", 0.075)
								)
							),
							Seq(
								Wait(60),
								Destroy()
							)
						)
					)
				),
				Change("offset", -20)
			)
		);
	}
}