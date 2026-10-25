package state.gameplay;

import main.Debug;

import action.Action;
import action.Easing;
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
				Debug.log(SCT.class, "caster \"" + caster
					+ "\" should be written as \""
					+ upper + "\"."
				);
				return type;
			}
		}

		throw Debug.terminate(SCT.class, "Unknown SCT caster: " + caster);
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
		int frames = 30;
		return Sequence(
			SetCostume("LAMBDASCT"),
			SetSize(35),
			Look(180),
			Warp(0, 280),
			Var("speed", -1.5),
			Var("exit", 0),
			Parallel(
				While( Equal(Get("exit"), 0),
					() -> Sequence(
						MoveY(Get("speed")),
						Wait()
					)
				),
				Sequence(
					Tween("brightness", -0, 0, "ghost", 100, 0, frames),
					Tween("speed", -0.5, 8),
					Tween("speed", -2, 8),
					Tween("size", 40, "ghost", 100, frames, Easing.QuintIn),
					Set("exit", 1)
				)
			)
		);
	}

	private Action reimu() {
		int frames = 30;
		return Sequence(
			SetCostume("ReimuSCT"),
			SetSize(32.5),
			Warp(0, -340),
			Var("speed", 4),
			Var("exit", 0),
			Parallel(
				While( Equal(Get("exit"), 0),
					() -> Sequence(
						MoveY(Get("speed")),
						Wait()
					)
				),
				Sequence(
					Tween("brightness", -0, 0, "ghost", 100, 0, frames),
					Tween("speed", 1.5, 8),
					Tween("size", 35, "ghost", 100, frames, Easing.QuintIn),
					Set("exit", 1)
				)
			)
		);
	}

	private Action marisa() {
		return Sequence( Wait(29) );
	}
}