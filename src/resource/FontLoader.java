package resource;

import java.awt.Font;
import java.io.IOException;
import java.net.URL;

public final class FontLoader {
	private static final String[] EXTENSIONS = {
		".ttd",
		".otf"
	};
	private FontLoader() {}

	public static Font load(String name) {

		URL url = ResourceFinder.find(ResourcePath.FONT, name, EXTENSIONS);

		if (url == null) {
			throw new RuntimeException(
				"Font not found: " + name
			);
		}

		try {
			return Font.createFont(
				Font.TRUETYPE_FONT,
				url.openStream()
			);

		} catch (Exception e) {

			throw new RuntimeException(
				"Failed to load font: " + name,
				e
			);

		}

	}

}