package state.gameplay;

import graphics.Renderer;
import graphics.TextAlign;
import graphics.TextBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entity.Thing;
import action.Spell;

import resource.ResourceLoader;

public final class HUD {
	private static final int LABEL_OFFSET = 22;
	private static final int BUFFER_ZERO = 9;
	
	private final ArrayList<TextBox> PLAYFIELD_TEXTS = new ArrayList<>();
	private final ArrayList<TextBox> OVERLAY_TEXTS = new ArrayList<>();

	private TextBox spellWhole;
	private TextBox spellDecimal;

	private final TextBox scoreValue;
	private final TextBox grazeValue;
	private final TextBox powerValue;
	private final TextBox livesValue;
	private final TextBox bombsValue;
	
	private BufferedImage foreground;
	
	private final ArrayList<Thing> SCTs = new ArrayList<>();
	private Spell currentSpell;

	public HUD() {
		this.foreground = ResourceLoader.image("Foreground");
		
		this.spellWhole = addText(PLAYFIELD_TEXTS, "0",0,300,200,100,TextAlign.RIGHT);
		this.spellWhole.setGlyphSize(50.0f);
		this.spellDecimal = addText(PLAYFIELD_TEXTS, ".00",0,300,200,100,TextAlign.LEFT);
		this.spellDecimal.setGlyphSize(31.25f);
		
		TextBox temp;
		temp = addText(OVERLAY_TEXTS, "SCORE", 260, 290, 300, 100, TextAlign.LEFT);
		temp.setGlyphSize(28.125f);
		this.scoreValue = addText(OVERLAY_TEXTS, "", 260, 290 - LABEL_OFFSET, 300, 100, TextAlign.LEFT);
		this.scoreValue.setGlyphSize(28.125f);
		
		temp = addText(OVERLAY_TEXTS, "GRAZE", 260, 215, 300, 100, TextAlign.LEFT);
		temp.setGlyphSize(28.125f);
		this.grazeValue = addText(OVERLAY_TEXTS, "", 260, 215 - LABEL_OFFSET, 300, 100, TextAlign.LEFT);
		this.grazeValue.setGlyphSize(28.125f);
		
		temp = addText(OVERLAY_TEXTS, "POWER", 260, 140, 300, 100, TextAlign.LEFT);
		temp.setGlyphSize(28.125f);
		this.powerValue = addText(OVERLAY_TEXTS, "", 260, 140 - LABEL_OFFSET, 300, 100, TextAlign.LEFT);
		this.powerValue.setGlyphSize(28.125f);
		
		temp = addText(OVERLAY_TEXTS, "LIVES", 260, 65, 300, 100, TextAlign.LEFT);
		temp.setGlyphSize(28.125f);
		this.livesValue = addText(OVERLAY_TEXTS, "", 260, 65 - LABEL_OFFSET, 300, 100, TextAlign.LEFT);
		this.livesValue.setGlyphSize(28.125f);
		
		temp = addText(OVERLAY_TEXTS, "BOMBS", 260, -10, 300, 100, TextAlign.LEFT);
		temp.setGlyphSize(28.125f);
		this.bombsValue = addText(OVERLAY_TEXTS, "", 260, -10 - LABEL_OFFSET, 300, 100, TextAlign.LEFT);
		this.bombsValue.setGlyphSize(28.125f);
	}
	private TextBox addText(
		ArrayList<TextBox> texts,
		String text,
		float x, float y,
		float width, float height,
		TextAlign align
	) {
		TextBox box = new TextBox(
			text, x, y, width, height, align
		);

		texts.add(box);
		return box;
	}

	public void setCurrentSpell(Spell cS) { this.currentSpell = cS; }

	public void showSCT(String caster) {
		SCTs.add(new SCT(caster));
	}

	public void update() {
		for (Thing portrait : new ArrayList<>(SCTs))
			portrait.update();

		SCTs.removeIf(thing -> !thing.isAlive());

		updateSpellTimer();
		updatePlayerStats();

		for (TextBox text : PLAYFIELD_TEXTS)
			text.update();

		for (TextBox text : OVERLAY_TEXTS)
			text.update();
	}
	private void updateSpellTimer() {
		if (currentSpell == null)
			return;

		float seconds = currentSpell.getTimer() / 60.0f;

		int whole = (int) seconds;
		int decimal = (int) ((seconds - whole) * 100);

		spellWhole.setText(String.valueOf(whole));
		spellDecimal.setText(String.format(".%02d", decimal));
	}

	private void updatePlayerStats() {
		scoreValue.setText(
			pad(PlayingStats.getScore(), BUFFER_ZERO)
		);

		grazeValue.setText(
			pad(PlayingStats.getGraze(), 6)
		);

		powerValue.setText(
			String.valueOf(PlayingStats.getPower())
		);

		livesValue.setText(
			String.valueOf(PlayingStats.getLives())
		);

		bombsValue.setText(
			String.valueOf(PlayingStats.getBombs())
		);
	}


	public void drawPlayfield(Renderer renderer) {
		drawSCTs(renderer);

		for (TextBox text : PLAYFIELD_TEXTS)
			text.draw(renderer);
	}

	public void drawOverlay(Renderer renderer) {
		renderer.image(foreground, 0, 0, 960, 720);

		for (TextBox text : OVERLAY_TEXTS)
			text.draw(renderer);
	}

	private void drawSCTs(Renderer renderer) {
		for (Thing thing : SCTs) {
			thing.draw(renderer);
		}
	}
	
	private String pad(long value, int digits) {
		return String.format("%0" + digits + "d", value);
	}
}