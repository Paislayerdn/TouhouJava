package action;

import main.Debug;
import entity.Thing;

public enum ReservedVariable {
	X("x"),
	Y("y"),
	ANGLE("angle"),
	TRUE_ANGLE("trueAngle"),
	APPEAR_ANGLE("appearAngle"),
	ANGLE_OVERRIDE("angleOverride"),
	SIZE("size"),
	COLOR("color"),
	DESATURATION("desaturation"),
	BRIGHTNESS("brightness"),
	GHOST("ghost"),
	PIXELATE("pixelate");

	private final String name;

	ReservedVariable(String name) {
		this.name = name;
	}

	public final String getName() { return name; }

	public final Object get(Action action) {
		Thing owner = action.getOwner();

		switch (this) {
			case X: return owner.getX();
			case Y: return owner.getY();
			case ANGLE:
				return owner.getAngleOverride()? owner.getAppearAngle(): owner.getTrueAngle();

			case TRUE_ANGLE: return owner.getTrueAngle();
			case APPEAR_ANGLE: return owner.getAppearAngle();
			case ANGLE_OVERRIDE: return owner.getAngleOverride();

			case SIZE: return owner.getAppearance().size;
			case COLOR: return owner.getAppearance().color;
			case DESATURATION: return owner.getAppearance().desaturation;
			case BRIGHTNESS: return owner.getAppearance().brightness;
			case GHOST: return owner.getAppearance().ghost;
			case PIXELATE: return owner.getAppearance().pixelate;
		}

		throw Debug.terminate("JScratch", this, "Unhandled reserved property: " + name);
	}
	
	public final void set(Action action, float value) {
		Thing owner = action.getOwner();

		switch (this) {
			case X: owner.setX(value); break;
			case Y: owner.setY(value); break;
			case ANGLE:
				if ( owner.getAngleOverride() ) { owner.setAppearAngle(value); } else { owner.setTrueAngle(value); }
				break;
				
			case SIZE: owner.getAppearance().setSize(value); break;
			case COLOR: owner.getAppearance().setColor(value); break;
			case DESATURATION: owner.getAppearance().setDesaturation(value); break;
			case BRIGHTNESS: owner.getAppearance().setBrightness(value); break;
			case GHOST: owner.getAppearance().setGhost(value); break;
			case PIXELATE: owner.getAppearance().setPixelate(value); break;
			default:
				throw Debug.terminate("JScratch", this, "Property is not writable/tweenable: " + name);
		}
	}
	
	public final void warnUsage(String operation) {
		String replacement;

		switch (this) {
			case X:
				replacement = operation.equals("Set")? "SetX(...)": "MoveX(...)"; break;
			case Y:
				replacement = operation.equals("Set")? "SetY(...)": "MoveY(...)"; break;
			case ANGLE:
				replacement = operation.equals("Set")? "Look(...)": "Turn(...)"; break;
			default: return;
		}

		Debug.log("JScratch", this,
			operation + "(\"" + name + "\", ...) is discouraged. "
			+ "Use " + replacement + " instead."
		);
	}
	
	public final static boolean isReserved(String input) { return fromName(input) != null; }
	public final static ReservedVariable fromName(String input) {
		// Correct spelling: no warning.
		for (ReservedVariable property : values()) {
			if (property.name.equals(input)) {
				return property;
			}
		}

		// Wrong capitalization: still works, but warn.
		for (ReservedVariable property : values()) {
			if (property.name.equalsIgnoreCase(input)) {
				Debug.log("JScratch", property,
					input + "\" is a reserved value. "
					+ "The standard spelling is \""
					+ property.name + "\". Try to be sober."
				);

				return property;
			}
		}

		return null;
	}
}