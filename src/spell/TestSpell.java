package spell;

import action.Spell;
import action.Action;
import static action.JScratch.*;

import entity.Boss;
import entity.Player;

public final class TestSpell extends Spell {
	public TestSpell(Boss boss, Player player) {
		super(boss, player);
		configure();
	}
	
	@Override
	public void configure() {
		name = "TestSpell";
		playerCandidateRadius = 60;
		timer = 3000;
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
		int spokes = 13;
		float spread = 215/(spokes); 
		return Sequence(
			Var("offset", 0),
			Var("count", 1),
			Forever("Sequence",
				For("i", 1, spokes, () ->
					SpawnBullet(
						Par(
							Var("index", Get("i")),
							Var("speed", 2),
							SetCostume("OvalBullet"),
							SetSize(11),
							SetBrightness(90),
							AddCircleHitbox("bulletHB", 7),
							AddHitboxTag("bulletHB", "ENEMY_BULLET"),
							If( Equal( Mod( Get("index"), 2 ), Get("count") ),
								() -> SetColor(50)
							),
							
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
				Change("offset", 11),
				Set("count", Mod( Add(Get("count"), 1), 2 ))
			)
		);
	}
}