package graphics;

import java.awt.image.BufferedImage;

public final class DepictUtility {
	private DepictUtility() {}

	public static BufferedImage pixelate(BufferedImage source, double amount) {
		if (amount <= 0) {
			return source;
		}

		double blockSize = amount + 1;

		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);

		for (int y = 0; y < source.getHeight(); y += blockSize) {
			for (int x = 0; x < source.getWidth(); x += blockSize) {

				int sample = source.getRGB(x, y);

				double maxX = Math.min(x + blockSize, source.getWidth());
				double maxY = Math.min(y + blockSize, source.getHeight());

				for (int py = y; py < maxY; py++) {
					for (int px = x; px < maxX; px++) {
						result.setRGB(px, py, sample);
					}
				}
			}
		}

		return result;
	}

	public static BufferedImage color(BufferedImage source, double amount) {
		if (amount == 0) {
			return source;
		}

		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);

		float hueShift = (float)amount / 256.0f;

		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				int argb = source.getRGB(x, y);

				int alpha = (argb >>> 24) & 0xFF;

				if (alpha == 0) {
					result.setRGB(x, y, argb);
					continue;
				}

				int red = (argb >>> 16) & 0xFF;
				int green = (argb >>> 8) & 0xFF;
				int blue = argb & 0xFF;

				float[] hsb = java.awt.Color.RGBtoHSB(
					red, green, blue, null
				);

				if (hsb[1] != 0) {
					hsb[0] = (hsb[0] + hueShift) % 1.0f;
				}

				int rgb = java.awt.Color.HSBtoRGB(
					hsb[0], hsb[1], hsb[2]
				);

				result.setRGB(
					x, y,
					(alpha << 24) | (rgb & 0x00FFFFFF)
				);
			}
		}

		return result;
	}

	public static BufferedImage brightness(BufferedImage source, double amount) {
		if (amount == 0) {
			return source;
		}

		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
			BufferedImage.TYPE_INT_ARGB
		);

		double factor = Math.abs(amount) / 100.0;

		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				int argb = source.getRGB(x, y);

				int alpha = (argb >>> 24) & 0xFF;

				if (alpha == 0) {
					result.setRGB(x, y, argb);
					continue;
				}

				int red = (argb >>> 16) & 0xFF;
				int green = (argb >>> 8) & 0xFF;
				int blue = argb & 0xFF;

				if (amount < 0) {
					red = (int)(red * (1.0 - factor));
					green = (int)(green * (1.0 - factor));
					blue = (int)(blue * (1.0 - factor));
				} else {
					red = (int)(red + (255 - red) * factor);
					green = (int)(green + (255 - green) * factor);
					blue = (int)(blue + (255 - blue) * factor);
				}

				result.setRGB(
					x, y,
					(alpha << 24)
					| (red << 16)
					| (green << 8)
					| blue
				);
			}
		}

		return result;
	}
}