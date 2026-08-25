package graphics;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

public final class TextDrawer {

	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);

	private TextDrawer() {
		// Prevent instantiation
	}

	public static void draw(Graphics2D g2, String text, int x, int y
	) {
		draw(g2, text, x, y, DEFAULT_FONT);
	}
	
	public static void draw(Graphics2D g2, String text, int x, int y, Font font) {
		Font oldFont = g2.getFont();
		Paint oldPaint = g2.getPaint();

		g2.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		g2.setFont(font);

		g2.setPaint(new GradientPaint(
				x,
				y - font.getSize(),
				Color.WHITE,
				x,
				y,
				new Color(80, 170, 255)
		));

		g2.drawString(text, x, y);

		g2.setFont(oldFont);
		g2.setPaint(oldPaint);
	}

}