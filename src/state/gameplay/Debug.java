package state.gameplay;

import java.awt.Graphics2D;

import main.Game;
import main.Input;

import graphics.TextDrawer;
import static graphics.Coordinate.*;

import entity.Player;
import entity.Boss;
import graphics.Renderer;

public final class Debug {
	private static Player player;
	private static Boss boss;
	
	private static final int PLAYER_PRECISION = 2;
	private static final int MOUSE_PRECISION = 0;
	
	private static boolean showHitboxes = false;
	
	private static int line;
	
	private Debug() {}
	
	public static void init(Player player, Boss boss) {
		Debug.player = player;
		Debug.boss = boss;
	}
	
	public static void update() {
		if (PlayingStats.debugMode != showHitboxes) {
			showHitboxes = PlayingStats.debugMode;
		}
	}

	public static void draw(Renderer renderer) {
		if (!PlayingStats.debugMode) { return; }
		line = 320;

		print(renderer, "=== DEBUG ===");
		print(renderer, "");

		print(renderer, "Player PF");
		print(renderer, format( player.getX(),player.getY(), PLAYER_PRECISION));
		print(renderer, "");
		print(renderer, "Player ABS");
		print(renderer, format( toScreen( player.getX(), player.getY()), PLAYER_PRECISION));

		print(renderer, "");

		print(renderer, "Mouse PF");
		print(renderer, format( toWorld(Input.mouseX, Input.mouseY), MOUSE_PRECISION ) );
		print(renderer, "");
		print(renderer, "Mouse ABS");
		print(renderer, format(Input.mouseX,Input.mouseY, MOUSE_PRECISION));
		
		print(renderer, "");
		print(renderer, "Bullets: " + BulletManager.getBulletCount());
	}

	private static void print(Renderer renderer, String text) {
		renderer.text(text, -460, line);
		line -= 20;
	}
	
	public static void setShowHitboxes(boolean value) { showHitboxes = value; }
	public static boolean isShowHitboxes() { return showHitboxes; }
}