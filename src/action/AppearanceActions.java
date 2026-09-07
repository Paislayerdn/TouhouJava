package action;

import resource.ResourceLoader;

class SetCostume extends Action {
	private final String name;

	@Override
	public boolean consumesFrame() { return false; }

	public SetCostume(String name) {
		this.name = name;
	}

	@Override
	public void update() {
		owner.getAppearance().setCostume(
			ResourceLoader.image(name)
		);

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
	public void update() {
		owner.getAppearance().setColor(
			(int) resolveDouble(color)
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
	public void update() {
		owner.getAppearance().changeColor(
			(int) resolveDouble(amount)
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
	public void update() {
		owner.getAppearance().setPixelate(
			(int) resolveDouble(pixelate)
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
	public void update() {
		owner.getAppearance().changePixelate(
			(int) resolveDouble(amount)
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
	public void update() {
		owner.getAppearance().setBrightness(
			(int) resolveDouble(brightness)
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
	public void update() {
		owner.getAppearance().changeBrightness(
			(int) resolveDouble(amount)
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
	public void update() {
		owner.getAppearance().setGhost(
			(int) resolveDouble(ghost)
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
	public void update() {
		owner.getAppearance().changeGhost(
			(int) resolveDouble(amount)
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
	public void update() {
		owner.getAppearance().setSize(
			resolveDouble(size)
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
	public void update() {
		owner.getAppearance().changeSize(
			resolveDouble(amount)
		);

		finish();
	}
}