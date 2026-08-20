package game;

import java.awt.Graphics2D;

import input.Input;
import graphics.TextDrawer;
import graphics.Coordinate;

import entity.Player;

public class Debug {
	private final Player player;
	private final BulletManager bulletManager;
	
	private static final int PLAYER_PRECISION = 2;
	private static final int MOUSE_PRECISION = 0;
	
	private boolean showPlayerHitboxes = false;
	private boolean showBulletHitboxes = false;
	
	private int line;
	
	public Debug(Player player, BulletManager bulletManager) {
		this.player = player;
		this.bulletManager = bulletManager;
	}
	
	public void update() {
		if (GameStats.debugMode != showBulletHitboxes) {
			showBulletHitboxes = GameStats.debugMode;
		}
	}

	public void draw(Graphics2D g2) {
		if (!GameStats.debugMode) {
			return;
		}
		line = 20;

		print(g2, "=== DEBUG ===");
		print(g2, "");

		print(g2, "Player PF");
		print(g2, Coordinate.format( player.getX(),player.getY(), PLAYER_PRECISION));
		print(g2, "");
		print(g2, "Player ABS");
		print(g2, Coordinate.format( Coordinate.toScreen( player.getX(), player.getY()), PLAYER_PRECISION));

		print(g2, "");

		print(g2, "Mouse PF");
		print(g2, Coordinate.format( Coordinate.toWorld( Input.mouseX, Input.mouseY), MOUSE_PRECISION ) );
		print(g2, "");
		print(g2, "Mouse ABS");
		print(g2, Coordinate.format(Input.mouseX,Input.mouseY, MOUSE_PRECISION));
		
		print(g2, "");
		print(g2, "Bullets: " + bulletManager.getBulletCount());
	}

	private void print(Graphics2D g2, String text) {
		TextDrawer.draw(g2, text, 20, line);
		line += 20;
	}
	public void setShowPlayerHitboxes(boolean value) { showPlayerHitboxes = value; }
	public boolean isShowPlayerHitboxes() { return showPlayerHitboxes; }
	
	public void setShowBulletHitboxes(boolean value) { showBulletHitboxes = value; }
	public boolean isShowBulletHitboxes() { return showBulletHitboxes; }
}