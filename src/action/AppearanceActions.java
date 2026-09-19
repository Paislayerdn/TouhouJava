package action;

import java.awt.image.BufferedImage;
import resource.ResourceLoader;

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

final class SetColor extends Action {
	private final Object color;

	@Override
	public boolean consumesFrame() { return false; }

	public SetColor(Object color) {
		this.color = color;
	}

	@Override
	public void start() {
		owner.getAppearance().setColor(
			(float) resolveFloat(color)
		);

		finish();
	}
}

final class ChangeColor extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeColor(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeColor(
			(float) resolveFloat(amount)
		);

		finish();
	}
}

final class SetPixelate extends Action {
	private final Object pixelate;

	@Override
	public boolean consumesFrame() { return false; }

	public SetPixelate(Object pixelate) {
		this.pixelate = pixelate;
	}

	@Override
	public void start() {
		owner.getAppearance().setPixelate(
			(float) resolveFloat(pixelate)
		);

		finish();
	}
}

final class ChangePixelate extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangePixelate(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changePixelate(
			(float) resolveFloat(amount)
		);

		finish();
	}
}

final class SetBrightness extends Action {
	private final Object brightness;

	@Override
	public boolean consumesFrame() { return false; }

	public SetBrightness(Object brightness) {
		this.brightness = brightness;
	}

	@Override
	public void start() {
		owner.getAppearance().setBrightness(
			(float) resolveFloat(brightness)
		);

		finish();
	}
}

final class ChangeBrightness extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeBrightness(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeBrightness(
			(float) resolveFloat(amount)
		);

		finish();
	}
}

final class SetGhost extends Action {
	private final Object ghost;

	@Override
	public boolean consumesFrame() { return false; }

	public SetGhost(Object ghost) {
		this.ghost = ghost;
	}

	@Override
	public void start() {
		owner.getAppearance().setGhost(
			(float) resolveFloat(ghost)
		);

		finish();
	}
}

final class ChangeGhost extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeGhost(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeGhost(
			(float) resolveFloat(amount)
		);

		finish();
	}
}

final class SetSize extends Action {
	private final Object size;

	@Override
	public boolean consumesFrame() { return false; }

	public SetSize(Object size) {
		this.size = size;
	}

	@Override
	public void start() {
		owner.getAppearance().setSize(
			(float) resolveFloat(size)
		);

		finish();
	}
}

final class ChangeSize extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeSize(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeSize(
			(float) resolveFloat(amount)
		);

		finish();
	}
}