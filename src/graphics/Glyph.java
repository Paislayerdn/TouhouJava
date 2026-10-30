package graphics;

import entity.Thing;

public final class Glyph extends Thing {
	private static final int POP_DURATION_FRAMES = 12;
	private static final float POP_SCALE = 1.2f;

	private final int codePoint;
	private long observedCostumeRevision = -1;
	private int popElapsed;

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

	@Override
	public void update() {
		super.update();

		long costumeRevision = getAppearance().getCostumeRevision();
		if (costumeRevision != observedCostumeRevision) {
			observedCostumeRevision = costumeRevision;
			popElapsed = 0;
		} else if (popElapsed < POP_DURATION_FRAMES) {
			popElapsed++;
		}
	}

	@Override
	public float getRenderedSize() {
		float progress = Math.min(1.0f, popElapsed / (float) POP_DURATION_FRAMES);
		float easedProgress = progress * progress * (3.0f - 2.0f * progress);
		float popScale = 1.0f + (POP_SCALE - 1.0f) * (1.0f - easedProgress);
		return getAppearance().size * popScale;
	}
}
