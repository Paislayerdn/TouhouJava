package resource;

import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
			data = AudioLoader.load(ResourceFinder.SOUND, name);
			sounds.put(name, data);
		}

		return new Sound(data);
	}
	public static Music music(String name) {
		AudioData data = AudioLoader.load(ResourceFinder.MUSIC, name);
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
	
	public static String lua(String name) {
		return text(ResourceFinder.SPELL, name, ".lua");
	}

	public static String dialogue(String name) {
		return text(ResourceFinder.DIALOGUE, name, ".txt");
	}
	private static String text(
		String folder,
		String name,
		String... extensions
	) {
		URL url = ResourceFinder.find(folder, name, extensions );

		if (url == null) {
			throw new RuntimeException("Text resource not found: " + name);
		}

		try (InputStream in = url.openStream()) {
			return new String(
				in.readAllBytes(),
				StandardCharsets.UTF_8
			);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load text resource: " + name, e);
		}
	}
}