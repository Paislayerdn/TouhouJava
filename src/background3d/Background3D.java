package background3d;

import graphics.Renderer;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import resource.ResourceLoader;
import jbml.*;
import jbml.ast.*;

public final class Background3D {
	private final BackgroundCamera camera = new BackgroundCamera();
	private final List<BackgroundObject> objects = new ArrayList<>();
	private final Map<String, JBMLEvent> events = new HashMap<>();
	private final Map<String, BackgroundObject> objectMap = new HashMap<>();
	
	public void load(String name) {
		String source = ResourceLoader.jbml(name);

		List<JBMLToken> tokens = new JBMLLexer(source).tokenize();
		JBMLScene scene = new JBMLParser(tokens).parse();

		objects.clear();
		objectMap.clear();
		events.clear();

		for (JBMLDeclaration declaration : scene.declarations) {
			if (declaration instanceof JBMLCamera camera) {
				loadCamera(camera);
			} else if (declaration instanceof JBMLWorld world) {
				loadWorld(world);
			} else if (declaration instanceof JBMLSky sky) {
				loadSky(sky);
			}
		}

		for (JBMLEvent event : scene.events) {
			events.put(event.name.toUpperCase(), event);
		}
	}
	public void render(Renderer renderer) {
		renderer.beginBackground(camera);

		for (BackgroundObject object : objects) {
			if (!object.visible) {
				continue;
			}

			renderer.backgroundObject(object);
		}

		renderer.endBackground();
	}
	public void update() {
		// Nothing yet.
	}
	
	private void loadCamera(JBMLCamera source) {
		camera.setXYZ(source.x, source.y, source.z);
		camera.setRotation(source.pitch, source.yaw, source.roll);
	}
	private void loadWorld(JBMLWorld source) {
		for (JBMLObject object : source.objects) {
			BackgroundObject backgroundObject = createObject(object);

			objects.add(backgroundObject);
			objectMap.put(object.name.toUpperCase(), backgroundObject);
		}
	}
	private void loadSky(JBMLSky source) {
		for (JBMLObject object : source.objects) {
			BackgroundObject backgroundObject = createObject(object);

			objects.add(backgroundObject);
			objectMap.put(object.name.toUpperCase(), backgroundObject);
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

		return object;
	}
	
	public void event(String name) {
		JBMLEvent event = events.get(name.toUpperCase());

		if (event == null) {
			throw new RuntimeException(
				"JBML event '" + name + "' does not exist. Fuck you."
			);
		}

		for (JBMLCommand command : event.commands) {
			execute(command);
		}
	}
	private void execute(JBMLCommand command) {
		if (command instanceof JBMLSet set) {
			executeSet(set);
			return;
		}

		throw new RuntimeException(
			"Unknown JBML command. Fuck you."
		);
	}
	
	private void executeSet(JBMLSet command) {
		String targetName = command.target.toUpperCase();
		String property = command.property.toLowerCase();

		if (targetName.equals("CAMERA")) {
			setCameraProperty(property, command.value);
			return;
		}

		BackgroundObject object = objectMap.get(targetName);

		if (object == null) {
			throw new RuntimeException(
				"JBML object '" + command.target + "' does not exist. Fuck you."
			);
		}

		switch (property) {
			case "x" -> object.x = number(command.value);
			case "y" -> object.y = number(command.value);
			case "z" -> object.z = number(command.value);

			case "pitch" -> object.pitch = number(command.value);
			case "yaw" -> object.yaw = number(command.value);
			case "roll" -> object.roll = number(command.value);

			case "costume" -> {
				if (!(command.value instanceof String costume)) {
					throw new RuntimeException(
						"JBML costume must be a string. Fuck you."
					);
				}

				object.appearance.setCostume(
					ResourceLoader.image(costume)
				);
			}

			default -> throw new RuntimeException(
				"Unknown JBML property '" + command.property + "'. Fuck you."
			);
		}
	}
	
	private float number(Object value) {
		if (!(value instanceof Float number)) {
			throw new RuntimeException(
				"JBML property requires a number. Fuck you."
			);
		}

		return number;
	}
	
	private void setCameraProperty(String property, Object value) {
		switch (property) {
			case "x" -> camera.x = number(value);
			case "y" -> camera.y = number(value);
			case "z" -> camera.z = number(value);

			case "pitch" -> camera.pitch = number(value);
			case "yaw" -> camera.yaw = number(value);
			case "roll" -> camera.roll = number(value);

			default -> throw new RuntimeException(
				"Unknown JBML camera property '" + property + "'. Fuck you."
			);
		}
	}

	public BackgroundCamera getCamera() { return camera; }
	public List<BackgroundObject> getObjects() { return objects; }
	public void add(BackgroundObject object) { objects.add(object); }
	public void remove(BackgroundObject object) { objects.remove(object); }
}