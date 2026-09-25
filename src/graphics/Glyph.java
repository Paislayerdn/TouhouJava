package graphics;

import entity.Thing;

public final class Glyph extends Thing {
	private final int codePoint;

	public Glyph(char character) {
		this((int) character);
	}

	public Glyph(int codePoint) {
		super();

		this.name = "[Glyph]";
		this.codePoint = codePoint;
		appearance.setCostume(
			TextHelper.getGlyphImage(codePoint)
		);
	}

	public int getCodePoint() {
		return codePoint;
	}

	public String getText() {
		return new String(Character.toChars(codePoint));
	}
}