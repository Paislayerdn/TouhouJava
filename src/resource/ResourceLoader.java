package resource;

import java.util.HashMap;
import java.util.Map;

import java.awt.image.BufferedImage;
import java.awt.Font;

public class ResourceLoader {
	private static final Map<String, BufferedImage> images = new HashMap<>();
	private static final Map<String, Sound> sounds = new HashMap<>();
	private static final Map<String, Font> fonts = new HashMap<>();

	public static BufferedImage image(String name) {
		BufferedImage image = images.get(name);

		if (image == null) {
			image = ImageLoader.load(name);
			images.put(name, image);
		}

		return image;
	}

	public static Sound sound(String name) {
		Sound sound = sounds.get(name);

		if (sound == null) {
			sound = new Sound(name);
			sounds.put(name, sound);
		}

		return sound;
	}
	
	public static Font font(String name) {
		Font font = fonts.get(name);

		if (font == null) {
			font = FontLoader.load(name);
			fonts.put(name, font);
		}

		return font;
	}
}