package background3d;

import java.util.ArrayList;
import java.util.List;

public final class Background3D {
	private final BackgroundCamera camera = new BackgroundCamera();
	private final List<BackgroundObject> objects = new ArrayList<>();

	public BackgroundCamera getCamera() {
		return camera;
	}

	public List<BackgroundObject> getObjects() {
		return objects;
	}

	public void add(BackgroundObject object) {
		objects.add(object);
	}

	public void remove(BackgroundObject object) {
		objects.remove(object);
	}

	public void update() {
		// Nothing yet.
	}
}