package graphics;

import entity.Thing;

public final class Glyph extends Thing {
	private final int codePoint;

	public Glyph(char character) {
		this((int) character);
	}

	public Glyph(int codePoint) {
		super();

		setName("[Glyph]");
		this.codePoint = codePoint;
		getAppearance().setCostume(
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
