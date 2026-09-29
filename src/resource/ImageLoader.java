package resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import main.Debug;

public final class ImageLoader {
	private static final String[] EXTENSIONS = {
		".png",
		".jpg",
		".jpeg",
		".gif",
		".bmp"
	};
	public static BufferedImage load(String name) {
		URL url = ResourceFinder.find(ResourceFinder.IMAGE, name, EXTENSIONS);

		if (url == null) {
			throw Debug.terminate("Image not found: " + name);
		}

		try {
			BufferedImage image = ImageIO.read(url);
			Debug.log(ImageLoader.class, "Loaded " + name);
			return image;

		} catch (IOException e) {
			throw Debug.terminate(ImageLoader.class, "Failed to load image: " + name + ", " + e);
		}
	}
}