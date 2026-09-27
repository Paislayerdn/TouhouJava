package background3d;

import graphics.Renderer;
import java.util.ArrayList;
import java.util.List;

import resource.ResourceLoader;
import jbml.*;
import jbml.ast.*;

public final class Background3D {
	private final BackgroundCamera camera = new BackgroundCamera();
	private final List<BackgroundObject> objects = new ArrayList<>();
	
	public void load(String name) {
		String source = ResourceLoader.jbml(name);

		List<JBMLToken> tokens = new JBMLLexer(source).tokenize();
		JBMLScene scene = new JBMLParser(tokens).parse();

		for (JBMLDeclaration declaration : scene.declarations) {
			if (declaration instanceof JBMLCamera camera) {
				loadCamera(camera);
			} else if (declaration instanceof JBMLWorld world) {
				loadWorld(world);
			} else if (declaration instanceof JBMLSky sky) {
				loadSky(sky);
			}
		}
	}
	private void loadCamera(JBMLCamera source) {
		camera.setXYZ(source.x, source.y, source.z);
		camera.setRotation(source.pitch, source.yaw, source.roll);
	}
	private void loadWorld(JBMLWorld source) {
		for (JBMLObject object : source.objects) {
			BackgroundObject backgroundObject = createObject(object);
			objects.add(backgroundObject);
		}
	}
	private void loadSky(JBMLSky source) {
		for (JBMLObject object : source.objects) {
			BackgroundObject backgroundObject = createObject(object);
			objects.add(backgroundObject);
		}
	}
	private BackgroundObject createObject(JBMLObject source) {
		BackgroundObject object = new BackgroundObject(
			source.x,
			source.y,
			source.z,
			100,
			100
		);

		object.setRotation(
			source.pitch,
			source.yaw,
			source.roll
		);

		object.appearance.setCostume(
			ResourceLoader.image("Icon")
		);

		return object;
	}

	public BackgroundCamera getCamera() { return camera; }
	public List<BackgroundObject> getObjects() { return objects; }
	public void add(BackgroundObject object) { objects.add(object); }
	public void remove(BackgroundObject object) { objects.remove(object); }

	public void render(Renderer renderer) {
		for (BackgroundObject object : objects) {
			if (!object.visible) {
				continue;
			}

			renderer.backgroundObject(object, camera);
		}
	}
	public void update() {
		// Nothing yet.
	}
}