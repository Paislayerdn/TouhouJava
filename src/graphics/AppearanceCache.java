package graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class AppearanceCache {
	private static final Map<Key, BufferedImage> cache = new HashMap<>();

	private AppearanceCache() {}

	public static BufferedImage get(BufferedImage costume, double color, double pixelate, double brightness) {
		if (costume == null) { return null; }

		// No effects? Don't cache anything unnecessarily.
		if (color == 0 && pixelate == 0 && brightness == 0) {
			return costume;
		}

		Key key = new Key(costume, color, pixelate, brightness);

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

	private static final class Key {
		private final BufferedImage costume;
		private final double color;
		private final double pixelate;
		private final double brightness;

		private Key(BufferedImage costume, double color, double pixelate, double brightness) {
			this.costume = costume;
			this.color = color;
			this.pixelate = pixelate;
			this.brightness = brightness;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Key)) return false;

			Key other = (Key) obj;

			return costume == other.costume
				&& Double.compare(color, other.color) == 0
				&& Double.compare(pixelate, other.pixelate) == 0
				&& Double.compare(brightness, other.brightness) == 0;
		}

		@Override
		public int hashCode() {
			int result = System.identityHashCode(costume);
			long temp;

			temp = Double.doubleToLongBits(color);
			result = 31 * result + (int) (temp ^ (temp >>> 32));

			temp = Double.doubleToLongBits(pixelate);
			result = 31 * result + (int) (temp ^ (temp >>> 32));

			temp = Double.doubleToLongBits(brightness);
			result = 31 * result + (int) (temp ^ (temp >>> 32));

			return result;
		}
	}
}