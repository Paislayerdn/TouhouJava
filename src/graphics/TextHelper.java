package graphics;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;

import java.text.AttributedString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TextHelper {
	private static final Font FONT = new Font("Arial", Font.PLAIN, 64);
	private static final FontRenderContext FRC = new FontRenderContext(null, true, true);
	private static final Map<Integer, BufferedImage> glyphCache = new HashMap<>();

	private TextHelper() {}

	/*
	 * ------------------------------------------------------------
	 * Glyph images
	 * ------------------------------------------------------------
	 */

	public static BufferedImage getGlyphImage(int codePoint) {
		BufferedImage image = glyphCache.get(codePoint);

		if (image != null)
			return image;

		image = createGlyphImage(codePoint);
		glyphCache.put(codePoint, image);

		return image;
	}

	private static BufferedImage createGlyphImage(int codePoint) {
		String text =
			new String(Character.toChars(codePoint));

		BufferedImage temporary = new BufferedImage(
			1,
			1,
			BufferedImage.TYPE_INT_ARGB
		);

		Graphics2D g2 = temporary.createGraphics();

		g2.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		g2.setFont(FONT);

		var bounds = FONT.getStringBounds(text, FRC);

		int width = Math.max(
			1,
			(int) Math.ceil(bounds.getWidth())
		);

		int height = Math.max(
			1,
			(int) Math.ceil(
				FONT.getLineMetrics(text, FRC).getHeight()
			)
		);

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

		g2.setFont(FONT);

		float baseline =
			FONT.getLineMetrics(text, FRC).getAscent();

		g2.drawString(text, 0, baseline);

		g2.dispose();

		return image;
	}

	/*
	 * ------------------------------------------------------------
	 * Layout
	 * ------------------------------------------------------------
	 */

	public static List<Glyph> createGlyphs(
		String text,
		float x,
		float y,
		float width,
		float height,
		TextAlign align,
		float size
	) {
		List<Glyph> glyphs = new ArrayList<>();

		if (text == null || text.isEmpty())
			return glyphs;

		float scale = size / 100.0f;
		float lineHeight = getLineHeight() * scale;
		float cursorY = y;

		int paragraphStart = 0;

		for (int i = 0; i <= text.length(); i++) {
			boolean end =
				i == text.length();

			boolean newline =
				!end && text.charAt(i) == '\n';

			if (!end && !newline)
				continue;

			String paragraph =
				text.substring(paragraphStart, i);

			layoutParagraph(
				paragraph,
				x,
				cursorY,
				width,
				align,
				size,
				glyphs
			);

			int lineCount =
				getLineCount(paragraph, width, size);

			cursorY -= lineCount * lineHeight;

			if (newline) {
				if (paragraph.isEmpty())
					cursorY -= lineHeight;

				paragraphStart = i + 1;
			}
		}

		if (y - cursorY > height) {
			System.err.println(
				"[TextHelper] Text exceeds TextBox height."
			);
		}

		return glyphs;
	}

	private static void layoutParagraph(
		String text,
		float x,
		float startY,
		float width,
		TextAlign align,
		float size,
		List<Glyph> output
	) {
		if (text.isEmpty())
			return;

		float scale = size / 100.0f;

		AttributedString attributed =
			new AttributedString(text);

		attributed.addAttribute(
			java.awt.font.TextAttribute.FONT,
			FONT
		);

		LineBreakMeasurer measurer =
			new LineBreakMeasurer(
				attributed.getIterator(),
				FRC
			);

		int paragraphEnd = text.length();
		float cursorY = startY;

		while (measurer.getPosition() < paragraphEnd) {
			int lineStart =
				measurer.getPosition();

			TextLayout layout =
				measurer.nextLayout(width / scale);

			int lineEnd =
				measurer.getPosition();

			float lineWidth =
				layout.getAdvance() * scale;

			float offset =
				getAlignmentOffset(lineWidth, align);

			createLineGlyphs(
				text,
				lineStart,
				lineEnd,
				x + offset,
				cursorY,
				size,
				output
			);

			cursorY -= getLineHeight() * scale;
		}
	}

	private static void createLineGlyphs(
		String text,
		int start,
		int end,
		float x,
		float y,
		float size,
		List<Glyph> output
	) {
		float cursorX = x;
		float scale = size / 100.0f;

		for (int i = start; i < end;) {
			int codePoint =
				text.codePointAt(i);

			int charCount =
				Character.charCount(codePoint);

			float advance =
				getAdvance(codePoint) * scale;

			Glyph glyph =
				new Glyph(codePoint);

			glyph.getAppearance().setSize(size);

			glyph.setXY(
				cursorX + advance / 2.0f,
				y
			);

			output.add(glyph);

			cursorX += advance;
			i += charCount;
		}
	}

	private static float getAlignmentOffset(
		float lineWidth,
		TextAlign align
	) {
		return switch (align) {
			case LEFT -> 0;
			case CENTER -> -lineWidth / 2.0f;
			case RIGHT -> -lineWidth;
			default -> 0;
		};
	}

	/*
	 * ------------------------------------------------------------
	 * Metrics
	 * ------------------------------------------------------------
	 */

	private static float getAdvance(int codePoint) {
		String text =
			new String(Character.toChars(codePoint));

		return (float) FONT
			.getStringBounds(text, FRC)
			.getWidth();
	}

	private static float getLineHeight() {
		return FONT
			.getLineMetrics("Ag", FRC)
			.getHeight();
	}

	private static int getLineCount(
		String text,
		float width,
		float size
	) {
		if (text.isEmpty())
			return 1;

		float scale = size / 100.0f;

		AttributedString attributed =
			new AttributedString(text);

		attributed.addAttribute(
			java.awt.font.TextAttribute.FONT,
			FONT
		);

		LineBreakMeasurer measurer =
			new LineBreakMeasurer(
				attributed.getIterator(),
				FRC
			);

		int count = 0;

		while (
			measurer.getPosition()
			< text.length()
		) {
			measurer.nextLayout(width / scale);
			count++;
		}

		return count;
	}
}