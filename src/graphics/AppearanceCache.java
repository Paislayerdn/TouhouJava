package graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class AppearanceCache {
	private static final Map<Key, BufferedImage> cache = new HashMap<>();

	private AppearanceCache() {}

	public static BufferedImage get(
			BufferedImage costume,
			double color,
			double pixelate,
			double brightness
	) {
		if (costume == null) {
			return null;
		}

		// No effects? Don't cache anything unnecessarily.
		if (color == 0
				&& pixelate == 0
				&& brightness == 0) {
			return costume;
		}

		Key key = new Key(
			costume,
			color,
			pixelate,
			brightness
		);

		return cache.computeIfAbsent(key, k -> {
			BufferedImage image = costume;

			if (pixelate != 0) {
				image = DepictUtility.pixelate(image, pixelate);
			}

			if (color != 0) {
				image = DepictUtility.color(image, color);
			}

			if (brightness != 0) {
				image = DepictUtility.brightness(image, brightness);
			}

			return image;
		});
	}

	public static void clear() {
		cache.clear();
	}

	private record Key(
		BufferedImage costume,
		double color,
		double pixelate,
		double brightness
	) {}
}