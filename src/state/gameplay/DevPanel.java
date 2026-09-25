package state.gameplay;

import main.Input;

import graphics.Renderer;
import graphics.TextAlign;
import graphics.TextBox;

import static graphics.Coordinate.*;

import entity.Player;
import entity.Boss;

public final class DevPanel {
	private static final int PLAYER_PRECISION = 2;
	private static final int MOUSE_PRECISION = 0;
	
	private Player player;
	private Boss boss;

	private boolean showHitboxes = false;

	private final TextBox TEXT = new TextBox("",
		-460, 320,
		900, 900, TextAlign.LEFT
	);

	public DevPanel(Player player, Boss boss) {
		this.player = player;
		this.boss = boss;
		
		TEXT.setGlyphSize(28.125f);
	}
	
	public void update() {
		if (PlayingStats.debugMode != showHitboxes) {
			showHitboxes = PlayingStats.debugMode;
		}

		if (!PlayingStats.debugMode)
			return;

		TEXT.setText(
			"=== DEBUG ===\n" +
			"\n" +
			"Player PF\n" +
			format(player.getX(), player.getY(), PLAYER_PRECISION) + "\n" +
			"\n" +
			"Player ABS\n" +
			format(toScreen(player.getX(), player.getY()), PLAYER_PRECISION) + "\n" +
			"\n" +
			"Mouse PF\n" +
			format(toWorld(Input.mouseX, Input.mouseY), MOUSE_PRECISION) + "\n" +
			"\n" +
			"Mouse ABS\n" +
			format(Input.mouseX, Input.mouseY, MOUSE_PRECISION) + "\n" +
			"\n" +
			"Bullets: " + BulletManager.getBulletCount()
		);

		TEXT.update();
	}

	public void draw(Renderer renderer) {
		if (!PlayingStats.debugMode)
			return;

		TEXT.draw(renderer);
	}

	public void setShowHitboxes(boolean value) {
		showHitboxes = value;
	}

	public boolean isShowHitboxes() {
		return showHitboxes;
	}
}