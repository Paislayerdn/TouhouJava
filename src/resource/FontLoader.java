package resource;

import java.net.URL;
import java.awt.Font;
import main.Debug;

public final class FontLoader {
	private static final String[] EXTENSIONS = {
		".ttd",
		".otf"
	};
	private FontLoader() {}

	public static Font load(String name) {

		URL url = ResourceFinder.find(ResourceFinder.FONT, name, EXTENSIONS);

		if (url == null) {
			throw Debug.terminate(FontLoader.class, "Font not found: " + name);
		}

		try {
			return Font.createFont(
				Font.TRUETYPE_FONT,
				url.openStream()
			);

		} catch (Exception e) {
			throw Debug.terminate(FontLoader.class,  "Failed to load font: " + name + ", " + e);
		}
	}
}