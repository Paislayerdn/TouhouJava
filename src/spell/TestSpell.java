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
		int spokes = 13;
		double spread = 215/(spokes); 
		return Sequence(
			Var("offset", 0),
			Forever("Sequence",
				For("i", 1, spokes, 
					() -> SpawnBullet(
						Par(
							Var("index", Get("i")),
							Var("speed", 2),
							Sequence(
								GoTo(boss),
								Look( Get("offset") ),
								Turn( Mul(spread, Sub(Get("index"), (spokes+1)/2) ) ),
								Forever("Sequence",
									Forward(Get("speed")),
									Change("speed", 0.075)
								)
							),
							Seq(
								Wait(120),
								Destroy()
							)
						)
					)
				),
				Wait(2),
				Change("offset", 11)
			)
		);
	}
}