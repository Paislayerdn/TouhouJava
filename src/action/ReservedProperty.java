package action;

import entity.Entity;
import entity.Thing;

public enum ReservedProperty {
	X("x"),
	Y("y"),
	ANGLE("angle"),
	TRUE_ANGLE("trueAngle"),
	APPEAR_ANGLE("appearAngle"),
	ANGLE_OVERRIDE("angleOverride"),
	SIZE("size"),
	COLOR("color"),
	BRIGHTNESS("brightness"),
	GHOST("ghost"),
	PIXELATE("pixelate");

	private final String name;

	ReservedProperty(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object get(Action action) {
		Thing owner = action.getOwner();

		switch (this) {
			case X: return owner.getX();
			case Y: return owner.getY();
			case ANGLE:
				if (owner instanceof Entity) {
					Entity entity = (Entity) owner;
					if (entity.getAngleOverride()) return entity.getAppearAngle();
				}

				return owner.getTrueAngle();

			case TRUE_ANGLE: return owner.getTrueAngle();
			case APPEAR_ANGLE:
				if (!(owner instanceof Entity)) {
					JDebug.log(
						"[JScratch] Warning: appearAngle used on a Thing. "
						+ "Using trueAngle instead."
					);
					return owner.getTrueAngle();
				}
				Entity appearEntity = (Entity) owner;
				return appearEntity.getAppearAngle();

			case ANGLE_OVERRIDE:
				if (!(owner instanceof Entity)) {
					JDebug.log(
						"[JScratch] Warning: angleOverride used on a Thing. "
						+ "Returning false."
					);
					return false;
				}
				Entity overrideEntity = (Entity) owner;
				return overrideEntity.getAngleOverride();

			case SIZE: return owner.getAppearance().size;
			case COLOR: return owner.getAppearance().color;
			case BRIGHTNESS: return owner.getAppearance().brightness;
			case GHOST: return owner.getAppearance().ghost;
			case PIXELATE: return owner.getAppearance().pixelate;
		}

		throw new IllegalStateException(
			"[JScratch] Unhandled reserved property: " + name
		);
	}
	
	public static boolean isReserved(String input) {
		return fromName(input) != null;
	}
	public static ReservedProperty fromName(String input) {
		// Correct spelling: no warning.
		for (ReservedProperty property : values()) {
			if (property.name.equals(input)) {
				return property;
			}
		}

		// Wrong capitalization: still works, but warn.
		for (ReservedProperty property : values()) {
			if (property.name.equalsIgnoreCase(input)) {
				JDebug.log(
					"ReservedProperty Warning: \"" + input
					+ "\" is a reserved value. "
					+ "The standard spelling is \""
					+ property.name + "\". Try to be sober."
				);

				return property;
			}
		}

		return null;
	}
}