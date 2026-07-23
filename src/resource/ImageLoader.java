package resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageLoader {
	private static final String IMAGE_PATH = "/assets/images/";
	private static final String[] EXTENSIONS = {
		".png",
		".jpg",
		".jpeg",
		".gif",
		".bmp"
	};

	private static final Map<String, BufferedImage> cache = new HashMap<>();

	public static BufferedImage load(String name) {
		// Already loaded?
		BufferedImage image = cache.get(name);

		if (image != null) { return image; }

		URL url = ResourceFinder.find(IMAGE_PATH, name, EXTENSIONS);

		if (url == null) {
			throw new RuntimeException("Image not found: " + name);
		}

		try {
			image = ImageIO.read(url);
			cache.put(name, image);
			System.out.println("[ImageLoader] Loaded " + name);
			return image;

		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to load image: " + name,
					e
			);

		}

	}
}