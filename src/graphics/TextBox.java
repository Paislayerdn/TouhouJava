package graphics;

import java.util.Collections;
import java.util.List;

public final class TextBox {
	private List<Glyph> glyphs;

	private String text;

	private float x;
	private float y;
	private float width;
	private float height;
	private TextAlign align;
	private float glyphSize = 100;
	
	public void setGlyphSize(float size) {
		glyphSize = size;
		rebuild();
	}

	public float getGlyphSize() {
		return glyphSize;
	}

	public TextBox(String text,
		float x, float y,
		float width, float height, TextAlign align
	) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.align = align;

		setText(text);
	}

	public void setText(String text) {
		this.text = text;

		glyphs = TextHelper.createGlyphs(
			text, x, y,
			width, height,
			align, glyphSize
		);
	}

	public String getText() { return text; }
	public float getX() { return x; }
	public float getY() { return y; }
	public void setX(float x) {
		this.x = x;
		rebuild();
	}
	public void setY(float y) {
		this.y = y;
		rebuild();
	}
	public void setPosition(float x, float y) {
		this.x = x; this.y = y;
		rebuild();
	}
	
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	public void setWidth(float width) {
		this.width = width;
		rebuild();
	}
	public void setHeight(float height) {
		this.height = height;
		rebuild();
	}

	public void setAlign(TextAlign align) {
		this.align = align;
		rebuild();
	}

	public TextAlign getAlign() { return align; }
	public List<Glyph> getGlyphs() { return Collections.unmodifiableList(glyphs); }
	public Glyph getGlyph(int index) { return glyphs.get(index); }

	public void update() {
		for (Glyph glyph : glyphs)
			glyph.update();
	}

	public void draw(Renderer renderer) {
		for (Glyph glyph : glyphs)
			glyph.draw(renderer);
	}

	private void rebuild() {
		glyphs = TextHelper.createGlyphs(
			text,
			x, y,
			width, height,
			align, glyphSize
		);
	}
}