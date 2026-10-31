package state.gameplay;

import graphics.Renderer;
import graphics.TextAlign;
import graphics.TextBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entity.Thing;
import action.Spell;

import resource.ResourceLoader;
import java.util.Locale;

public final class HUD {
	private static final int BUFFER_ZERO = 9;
	private static final float SIDEBAR_VALUE_RIGHT = 425;
	
	private final ArrayList<TextBox> PLAYFIELD_TEXTS = new ArrayList<>();
	private final ArrayList<TextBox> OVERLAY_TEXTS = new ArrayList<>();

	private TextBox spellWhole, spellDecimal, spellName, spellBonus, fpsValue;

	private final TextBox scoreValue, grazeValue,
			powerValue,  livesValue, bombsValue;
	
	private final BufferedImage foreground;
	private final PlayingStats stats;
	
	private final ArrayList<Thing> SCTs = new ArrayList<>();
	private final TextBox[] spellResult = new TextBox[4];
	private int spellResultFrames;
	private Spell currentSpell;

	public HUD(PlayingStats stats) {
		this.stats = stats;
		this.foreground = ResourceLoader.image("Foreground");
		
		this.spellWhole = addText(PLAYFIELD_TEXTS, "",0,260, 180,100,TextAlign.RIGHT);
		this.spellWhole.setGlyphSize(45.0f);
		this.spellDecimal = addText(PLAYFIELD_TEXTS, "",0,260, 180,100,TextAlign.LEFT);
		this.spellDecimal.setGlyphSize(25.0f);
		this.spellName = addText(PLAYFIELD_TEXTS, "", 275,315, 600,200,TextAlign.RIGHT);
		this.spellName.setGlyphSize(29.0f);
		this.spellBonus = addText(PLAYFIELD_TEXTS, "", 275, 280, 600, 100, TextAlign.RIGHT);
		this.spellBonus.setGlyphSize(22.0f);
		
		this.scoreValue = addText(OVERLAY_TEXTS, "", SIDEBAR_VALUE_RIGHT, 290, 250, 50, TextAlign.RIGHT);
		this.grazeValue = addText(OVERLAY_TEXTS, "", SIDEBAR_VALUE_RIGHT, 18, 250, 50, TextAlign.RIGHT);
		this.powerValue = addText(OVERLAY_TEXTS, "", SIDEBAR_VALUE_RIGHT, 42, 250, 50, TextAlign.RIGHT);
		this.livesValue = addText(OVERLAY_TEXTS, "", SIDEBAR_VALUE_RIGHT, 176, 250, 50, TextAlign.RIGHT);
		this.bombsValue = addText(OVERLAY_TEXTS, "", SIDEBAR_VALUE_RIGHT, 147, 250, 50, TextAlign.RIGHT);
		for (TextBox value : new TextBox[] {
			scoreValue, grazeValue, powerValue, livesValue, bombsValue
		}) {
			value.setGlyphSize(30.0f);
		}
		this.fpsValue = addText(OVERLAY_TEXTS, "FPS 0", 465, -345, 220, 50, TextAlign.RIGHT);
		this.fpsValue.setGlyphSize(18.0f);

		spellResult[0] = new TextBox("", 0, 150, 576, 70, TextAlign.CENTER);
		spellResult[1] = new TextBox("", 0, 80, 576, 70, TextAlign.CENTER);
		spellResult[2] = new TextBox("", 0, 10, 576, 70, TextAlign.CENTER);
		spellResult[3] = new TextBox("", 0, -60, 576, 70, TextAlign.CENTER);
		for (TextBox line : spellResult) line.setGlyphSize(32);
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

	public void setCurrentSpell(Spell cS) {
		this.currentSpell = cS;
		this.spellName.setText(cS == null ? "" : cS.getName());
	}

	public void setFps(int fps) {
		fpsValue.setText("FPS " + fps);
	}

	public void showSCT(String caster) {
		SCTs.add(new SCT(caster));
	}

	public void showSpellResult(Spell spell) {
		spellResult[0].setText("STAGE COMPLETED");
		spellResult[1].setText(spell.isSpell() ? "SPELL CAPTURED" : "NONSPELL CAPTURED");
		spellResult[2].setText("+" + spell.getScoreAwarded());
		spellResult[3].setText(String.format(Locale.ROOT, "time: %.2fs", spell.getElapsedSeconds()));
		spellResultFrames = 180;
	}

	public boolean isShowingSpellResult() { return spellResultFrames > 0; }

	public void update() {
		if (spellResultFrames > 0 && --spellResultFrames == 0) {
			for (TextBox line : spellResult) line.setText("");
		}
		for (Thing portrait : new ArrayList<>(SCTs))
			portrait.update();

		SCTs.removeIf(thing -> !thing.isAlive());

		updateSpellTimer();
		updatePlayerStats();

		for (TextBox text : PLAYFIELD_TEXTS)
			text.update();

		for (TextBox text : OVERLAY_TEXTS)
			text.update();
		for (TextBox text : spellResult)
			text.update();
	}
	private void updateSpellTimer() {
		if (currentSpell == null) {
			spellWhole.setText("");
			spellDecimal.setText("");
			spellBonus.setText("");
			return;
		}
		spellBonus.setText(!currentSpell.isSpell() ? ""
			: currentSpell.isBonusFailed() ? "BONUS FAILED"
			: String.format(Locale.ROOT, "BONUS %,d", currentSpell.getCurrentBonus()));

		float seconds = currentSpell.getTimer() / 60.0f;

		int whole = (int) seconds;
		int decimal = (int) ((seconds - whole) * 100);

		spellWhole.setText(String.valueOf(whole));
		spellDecimal.setText(String.format(".%02d", decimal));
	}

	private void updatePlayerStats() {
		scoreValue.setText(
			pad(stats.getScore(), BUFFER_ZERO)
		);

		grazeValue.setText(
			pad(stats.getGraze(), 6)
		);

		powerValue.setText(
			String.valueOf(stats.getPower())
		);

		livesValue.setText(
			String.valueOf(stats.getLives())
		);

		bombsValue.setText(
			String.valueOf(stats.getBombs())
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

		renderer.beginPlayfield();
		for (TextBox text : spellResult)
			text.draw(renderer);
		renderer.endPlayfield();
	}

	private void drawSCTs(Renderer renderer) {
		for (Thing thing : new ArrayList<>(SCTs))
			thing.draw(renderer);
	}
	
	private String pad(long value, int digits) {
		return String.format("%0" + digits + "d", value);
	}
}
