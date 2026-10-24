package graphics.java2d;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class AppearanceCache {
	private static final Map<Key, BufferedImage> cache = new HashMap<>();

	private AppearanceCache() {}

	public static BufferedImage get(
			BufferedImage costume,
			float color,
			float desaturation,
			float pixelate,
			float brightness
	) {
		if (costume == null) {
			return null;
		}

		float quantizedColor = quantize(color, Java2DQuality.COLOR_STEP,
			Java2DQuality.COLOR_ROUNDING, 0.0f, 255.0f);
		float quantizedDesaturation = quantize(desaturation, Java2DQuality.DESATURATION_STEP,
			Java2DQuality.DESATURATION_ROUNDING, 0.0f, 100.0f);
		float quantizedPixelate = quantize(pixelate, Java2DQuality.PIXELATE_STEP,
			Java2DQuality.PIXELATE_ROUNDING, 0.0f, Float.POSITIVE_INFINITY);
		float quantizedBrightness = quantize(brightness, Java2DQuality.BRIGHTNESS_STEP,
			Java2DQuality.BRIGHTNESS_ROUNDING, -100.0f, 100.0f);

		// No effects? Don't cache anything unnecessarily.
		if (quantizedColor == 0
			&& quantizedDesaturation == 0
			&& quantizedPixelate == 0
			&& quantizedBrightness == 0) {
			return costume;
		}

		Key key;
		key = new Key(
				costume,
				quantizedColor,
				quantizedDesaturation,
				quantizedPixelate,
				quantizedBrightness
		);

		BufferedImage cached = cache.get(key);
		if (cached != null) {
			return cached;
		}

		BufferedImage image = costume;

		if (quantizedPixelate != 0) {
			image = DepictUtility.pixelate(image, quantizedPixelate);
		}

		if (quantizedColor != 0) {
			image = DepictUtility.color(image, quantizedColor);
		}

		if (quantizedBrightness != 0) {
			image = DepictUtility.brightness(image, quantizedBrightness);
		}

		if (quantizedDesaturation != 0) {
			image = DepictUtility.desaturation(image, quantizedDesaturation);
		}

		cache.put(key, image);
		return image;
	}

	private static float quantize(float value, float step,
		Java2DQuality.RoundingMode roundingMode, float min, float max) {
		if (value == 0.0f || step <= 1.0f || !Float.isFinite(step)
			|| !Float.isFinite(value)) {
			return value;
		}

		double units = (double) value / step;
		double roundedUnits = switch (roundingMode) {
			case ROUND_UP -> Math.ceil(units);
			case ROUND -> Math.floor(units + 0.5);
			case ROUND_DOWN -> Math.floor(units);
		};
		float quantized = (float) (roundedUnits * step);
		return Math.max(min, Math.min(max, quantized));
	}

	public static void clear() { cache.clear(); }

	private record Key(
		BufferedImage costume,
		float color,
		float desaturation,
		float pixelate,
		float brightness
	) {}
}
