package state.gameplay;

import action.Action;
import static action.JScratch.*;

import entity.Thing;

public final class SCT extends Thing {
	private final SCTType character;

	public SCT(String caster) {
		super();
		this.character = resolveCaster(caster);
		setup();
	}
	private static SCTType resolveCaster(String caster) {
	for (SCTType type : SCTType.values()) {
			if (type.name().equals(caster)) {
				return type;
			}
		}

		String upper = caster.toUpperCase();

		for (SCTType type : SCTType.values()) {
			if (type.name().equals(upper)) {
				System.out.println("SCT caster \"" + caster
					+ "\" should be written as \""
					+ upper + "\"."
				);
				return type;
			}
		}

		throw new IllegalStateException("Unknown SCT caster: " + caster);
	}

	private void setup() {
		switch (character) {
			case LAMBDA:	run(lambda());	break;
			case REIMU:		run(reimu());	break;
			case MARISA:	run(marisa());	break;
		}
		run( initiation() );
	}

	private Action initiation() {
		return Sequence(
			Sound("spell", "[TH] Spellcard"),
			SetSoundVolume("spell", -6.0f),
			PlaySound("spell")
		);
	}
	private Action lambda() {
		return Sequence(
			SetCostume("LAMBDASCT"),
			SetSize(40),
			Look(180),
			Warp(0, 480),
			Var("speed", -4),
			Var("exit", 0),
			Parallel(
				While( Equal(Get("exit"), 0),
					() -> Sequence(
						MoveY(Get("speed")),
						Wait(1)
					)
				),
				Sequence(
					Tween("brightness", -100, 0, "ghost", 100, 0, 30),
					Tween("speed", -1, 15),
					Tween("speed", -5, 15),
					Tween("size", 45,"ghost", 100, 30),
					Set("exit", 1)
				)
			)
		);
	}

	private Action reimu() {
		return Sequence(
			SetCostume("ReimuSCT"),
			SetSize(35),
			Warp(0, -480),
			Var("speed", 4),
			Var("exit", 0),
			Parallel(
				While( Equal(Get("exit"), 0),
					() -> Sequence(
						MoveY(Get("speed")),
						Wait(1)
					)
				),
				Sequence(
					Tween("brightness", -100, 0, "ghost", 100, 0, 30),
					Tween("speed", 1, 15),
					Tween("speed", 5, 15),
					Tween("size", 45,"ghost", 100, 30),
					Set("exit", 1)
				)
			)
		);
	}

	private Action marisa() {
		return Sequence( Wait(29) );
	}
}