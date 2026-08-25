package resource;

import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.Font;

public class ResourceLoader {
	private static final Map<String, BufferedImage> images = new HashMap<>();
	private static final Map<String, AudioData> sounds = new HashMap<>();
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
		AudioData data = sounds.get(name);

		if (data == null) {
			data = AudioLoader.load(ResourcePath.SOUND, name);
			sounds.put(name, data);
		}

		return new Sound(data);
	}
	public static Music music(String name) {
		AudioData data = AudioLoader.load(ResourcePath.MUSIC, name);
		return new Music(data);
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