package action;

import java.awt.image.BufferedImage;
import resource.ResourceLoader;

class SetCostume extends Action {
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

class SetColor extends Action {
	private final Object color;

	@Override
	public boolean consumesFrame() { return false; }

	public SetColor(Object color) {
		this.color = color;
	}

	@Override
	public void start() {
		owner.getAppearance().setColor(
			(double) resolveDouble(color)
		);

		finish();
	}
}

class ChangeColor extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeColor(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeColor(
			(double) resolveDouble(amount)
		);

		finish();
	}
}

class SetPixelate extends Action {
	private final Object pixelate;

	@Override
	public boolean consumesFrame() { return false; }

	public SetPixelate(Object pixelate) {
		this.pixelate = pixelate;
	}

	@Override
	public void start() {
		owner.getAppearance().setPixelate(
			(double) resolveDouble(pixelate)
		);

		finish();
	}
}

class ChangePixelate extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangePixelate(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changePixelate(
			(double) resolveDouble(amount)
		);

		finish();
	}
}

class SetBrightness extends Action {
	private final Object brightness;

	@Override
	public boolean consumesFrame() { return false; }

	public SetBrightness(Object brightness) {
		this.brightness = brightness;
	}

	@Override
	public void start() {
		owner.getAppearance().setBrightness(
			(double) resolveDouble(brightness)
		);

		finish();
	}
}

class ChangeBrightness extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeBrightness(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeBrightness(
			(double) resolveDouble(amount)
		);

		finish();
	}
}

class SetGhost extends Action {
	private final Object ghost;

	@Override
	public boolean consumesFrame() { return false; }

	public SetGhost(Object ghost) {
		this.ghost = ghost;
	}

	@Override
	public void start() {
		owner.getAppearance().setGhost(
			(double) resolveDouble(ghost)
		);

		finish();
	}
}

class ChangeGhost extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeGhost(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeGhost(
			(double) resolveDouble(amount)
		);

		finish();
	}
}

class SetSize extends Action {
	private final Object size;

	@Override
	public boolean consumesFrame() { return false; }

	public SetSize(Object size) {
		this.size = size;
	}

	@Override
	public void start() {
		owner.getAppearance().setSize(
			(double) resolveDouble(size)
		);

		finish();
	}
}

class ChangeSize extends Action {
	private final Object amount;

	@Override
	public boolean consumesFrame() { return false; }

	public ChangeSize(Object amount) {
		this.amount = amount;
	}

	@Override
	public void start() {
		owner.getAppearance().changeSize(
			(double) resolveDouble(amount)
		);

		finish();
	}
}