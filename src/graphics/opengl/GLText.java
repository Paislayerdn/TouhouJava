package graphics.opengl;

import graphics.TextAlign;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class GLText {
	private GLText() {}

	public static void draw(GLRenderer renderer, String text, float x, float y) {
		draw(renderer, text, x, y, 18, TextAlign.LEFT);
	}

	public static void draw(GLRenderer renderer, String text, float x, float y, int size) {
		draw(renderer, text, x, y, size, TextAlign.LEFT);
	}

	public static void draw(
		GLRenderer renderer,
		String text,
		float x,
		float y,
		int size,
		TextAlign align
	) {
		Font font = new Font("Arial", Font.PLAIN, size);

		BufferedImage image = createImage(text, font);

		float width = image.getWidth();
		float height = image.getHeight();

		float offset = switch (align) {
			case LEFT -> 0.0f;
			case CENTER -> -width / 2.0f;
			case RIGHT -> -width;
		};

		float ascent = font.getSize() * 0.8f;

		renderer.image(
			image,
			x + offset + width / 2.0f,
			y + (ascent - height / 2.0f)
		);
	}

	private static BufferedImage createImage(String text, Font font) {
		Graphics2D g2 = new BufferedImage(
			1,
			1,
			BufferedImage.TYPE_INT_ARGB
		).createGraphics();

		g2.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		g2.setFont(font);

		var metrics = g2.getFontMetrics();

		int width = Math.max(1, metrics.stringWidth(text));
		int ascent = metrics.getAscent();
		int height = Math.max(1, ascent + metrics.getDescent());

		g2.dispose();

		BufferedImage image = new BufferedImage(
			width,
			height,
			BufferedImage.TYPE_INT_ARGB
		);

		g2 = image.createGraphics();

		g2.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		g2.setFont(font);

		g2.setPaint(new GradientPaint(
			0,
			0,
			Color.WHITE,
			0,
			ascent,
			new Color(80, 170, 255)
		));

		g2.drawString(text, 0, ascent);

		g2.dispose();

		return image;
	}
}