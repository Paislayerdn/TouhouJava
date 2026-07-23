package resource;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
	private static final Map<String, BufferedImage> images = new HashMap<>();
	private static final Map<String, Sound> sounds = new HashMap<>();

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
}