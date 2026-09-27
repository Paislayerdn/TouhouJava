package jbml;

import java.util.List;

import jbml.ast.*;

public final class JBMLParser {
	private final List<JBMLToken> tokens;
	private int index;

	public JBMLParser(List<JBMLToken> tokens) {
		this.tokens = tokens;
		this.index = 0;
	}

	public JBMLScene parse() {
		expect(JBMLTokenType.SCENE);

		JBMLScene scene = new JBMLScene();

		while (!check(JBMLTokenType.END)) {
			switch (peek().type) {
				case CAMERA -> scene.declarations.add(parseCamera());
				case WORLD -> scene.declarations.add(parseWorld());
				case SKY -> scene.declarations.add(parseSky());
				case EVENT -> scene.events.add(parseEvent());

				default -> error();
			}
		}

		expect(JBMLTokenType.END);
		expect(JBMLTokenType.EOF);

		return scene;
	}

	private JBMLCamera parseCamera() {
		expect(JBMLTokenType.CAMERA);

		float[] transform = parseTransform();

		return new JBMLCamera(
			transform[0],
			transform[1],
			transform[2],
			transform[3],
			transform[4],
			transform[5]
		);
	}

	private JBMLWorld parseWorld() {
		expect(JBMLTokenType.WORLD);

		JBMLWorld world = new JBMLWorld();

		while (!check(JBMLTokenType.END)) {
			switch (peek().type) {
				case OBJECT, PLANE -> world.objects.add(parseObject());
				default -> error();
			}
		}

		expect(JBMLTokenType.END);
		return world;
	}

	private JBMLSky parseSky() {
		expect(JBMLTokenType.SKY);

		JBMLSky sky = new JBMLSky();

		while (!check(JBMLTokenType.END)) {
			switch (peek().type) {
				case OBJECT, PLANE -> sky.objects.add(parseObject());
				default -> error();
			}
		}

		expect(JBMLTokenType.END);
		return sky;
	}

	private JBMLObject parseObject() {
		advance(); // OBJECT or PLANE

		String name = expect(JBMLTokenType.IDENTIFIER).text;

		float[] transform = parseTransform();

		return new JBMLObject(
			name,
			transform[0],
			transform[1],
			transform[2],
			transform[3],
			transform[4],
			transform[5]
		);
	}

	private JBMLEvent parseEvent() {
		expect(JBMLTokenType.EVENT);

		String name = expect(JBMLTokenType.IDENTIFIER).text;

		JBMLEvent event = new JBMLEvent(name);

		while (!check(JBMLTokenType.END)) {
			switch (peek().type) {
				case SET -> event.commands.addAll(parseSet());
				default -> error();
			}
		}

		expect(JBMLTokenType.END);

		return event;
	}

	private List<JBMLCommand> parseSet() {
		expect(JBMLTokenType.SET);

		String target = expect(JBMLTokenType.IDENTIFIER).text;
		String property = expect(JBMLTokenType.IDENTIFIER).text;

		List<JBMLCommand> commands = new java.util.ArrayList<>();

		if (property.equalsIgnoreCase("XYZ")) {
			commands.add(new JBMLSet(target, "x", number()));
			commands.add(new JBMLSet(target, "y", number()));
			commands.add(new JBMLSet(target, "z", number()));
			return commands;
		}

		if (property.equalsIgnoreCase("ROTATION")) {
			commands.add(new JBMLSet(target, "pitch", number()));
			commands.add(new JBMLSet(target, "yaw", number()));
			commands.add(new JBMLSet(target, "roll", number()));
			return commands;
		}

		Object value;

		if (check(JBMLTokenType.NUMBER)) {
			value = number();
		} else if (check(JBMLTokenType.STRING)) {
			value = advance().text;
		} else {
			error();
			return null;
		}

		commands.add(new JBMLSet(target, property, value));

		return commands;
	}

	private float[] parseTransform() {
		float x = number();
		float y = number();
		float z = number();

		float pitch = 0;
		float yaw = 0;
		float roll = 0;

		if (check(JBMLTokenType.NUMBER)) {
			pitch = number();
			yaw = number();
			roll = number();
		}

		return new float[] {
			x, y, z,
			pitch, yaw, roll
		};
	}

	private float number() {
		return Float.parseFloat(
			expect(JBMLTokenType.NUMBER).text
		);
	}

	private JBMLToken expect(JBMLTokenType type) {
		if (!check(type)) {
			error();
		}

		return advance();
	}

	private JBMLToken advance() {
		return tokens.get(index++);
	}

	private JBMLToken peek() {
		return tokens.get(index);
	}

	private boolean check(JBMLTokenType type) {
		return peek().type == type;
	}

	private void error() {
		throw new RuntimeException(
			"JBML error on line " + peek().line + ". Fuck you."
		);
	}
}