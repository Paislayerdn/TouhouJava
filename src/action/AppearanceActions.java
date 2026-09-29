package action;

import java.awt.image.BufferedImage;
import resource.ResourceLoader;
import entity.Appearance;

final class SetCostume extends Action {
	private final String name;
	private final BufferedImage image;

	@Override
	public boolean consumesFrame() {
		return false;
	}

	public SetCostume(String name) {
		this.name = name;
		this.image = null;
	}

	public SetCostume(BufferedImage image) {
		this.name = null;
		this.image = image;
	}

	@Override
	public void start() {
		if (image != null) {
			owner.getAppearance().setCostume(image);
		} else {
			owner.getAppearance().setCostume(
				ResourceLoader.image(name)
			);
		}

		finish();
	}
}

final class AppearanceAction extends Action {
	enum Type {
		COLOR {
			void set(Appearance a, float v) { a.setColor(v); }
			void change(Appearance a, float v) { a.changeColor(v); }
		},
		DESATURATION {
			void set(Appearance a, float v) { a.setDesaturation(v); }
			void change(Appearance a, float v) { a.changeDesaturation(v); }
		},
		PIXELATE {
			void set(Appearance a, float v) { a.setPixelate(v); }
			void change(Appearance a, float v) { a.changePixelate(v); }
		},
		BRIGHTNESS {
			void set(Appearance a, float v) { a.setBrightness(v); }
			void change(Appearance a, float v) { a.changeBrightness(v); }
		},
		GHOST {
			void set(Appearance a, float v) { a.setGhost(v); }
			void change(Appearance a, float v) { a.changeGhost(v); }
		},
		SIZE {
			void set(Appearance a, float v) { a.setSize(v); }
			void change(Appearance a, float v) { a.changeSize(v); }
		};

		abstract void set(Appearance a, float value);
		abstract void change(Appearance a, float value);
	}
	enum Mode { SET, CHANGE }

	private final AppearanceAction.Type type;
	private final Mode mode;
	private final Object value;

	public AppearanceAction(Type type, Mode mode, Object value) {
		this.type = type;
		this.mode = mode;
		this.value = value;
	}

	@Override
	public boolean consumesFrame() {
		return false;
	}

	@Override
	public void start() {
		float amount = (float) resolveFloat(value);

		if (mode == Mode.SET) {
			type.set(owner.getAppearance(), amount);
		} else {
			type.change(owner.getAppearance(), amount);
		}

		finish();
	}
}