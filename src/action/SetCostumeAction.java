package action;

import java.awt.image.BufferedImage;
import entity.Thing;
import resource.ResourceLoader;

public final class SetCostumeAction extends Action {
	private final String name;
	private final BufferedImage image;

	@Override
	public boolean consumesFrame() {
		return false;
	}

	public SetCostumeAction(String name) {
		this.name = name;
		this.image = null;
	}

	public SetCostumeAction(BufferedImage image) {
		this.name = null;
		this.image = image;
	}

	@Override
	public void start() {
		Thing thing = ActionUtil.requireOwner(this, Thing.class);
		if (image != null) thing.getAppearance().setCostume(image); else {
			thing.getAppearance().setCostume(
				ResourceLoader.image(name)
			);
		}

		finish();
	}
}
