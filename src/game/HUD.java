package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.TextDrawer;
import main.Settings;
import resource.ResourceLoader;

public final class HUD {
	private static final int LABEL_OFFSET = 22;
	private static final int BUFFER_ZERO = 9;

	private static BufferedImage foreground;

	private HUD() {}
	
	public static void init() {
		foreground = ResourceLoader.image("Foreground");
	}

	public static void update() {

	}

	public static void draw(Graphics2D g2) {
		drawForeground(g2);
		drawPlayerStats(g2);
	}

	private static void drawForeground(Graphics2D g2) {
		g2.drawImage(foreground, 0, 0, Settings.BASE_WIDTH, Settings.BASE_HEIGHT, null);
	}
	
	private static void drawPlayerStats(Graphics2D g2) {
		drawStat(g2,	"SCORE",	pad(GameStats.score, BUFFER_ZERO),	740, 70);
		drawStat(g2,	"GRAZE",	pad(GameStats.graze, 6),			740, 145);
		drawStat(g2,	"POWER",	String.valueOf(GameStats.power),	740, 220);
		drawStat(g2,	"LIVES",	String.valueOf(GameStats.lives),	740, 295);
		drawStat(g2,	"BOMBS",	String.valueOf(GameStats.bombs),	740, 370);
	}
	
	private static String pad(long value, int digits) {
		return String.format("%0" + digits + "d", value);
	}
	
	private static void drawStat(Graphics2D g2, String label, String value, int x, int y) {
		TextDrawer.draw(g2, label, x, y);
		TextDrawer.draw(g2, value, x, y + LABEL_OFFSET);
	}
}