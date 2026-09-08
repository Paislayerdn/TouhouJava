package state.gameplay;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.Depict;
import graphics.TextDrawer;
import resource.ResourceLoader;
import main.Settings;

public final class HUD {
	private static final int LABEL_OFFSET = 22;
	private static final int BUFFER_ZERO = 9;

	private static BufferedImage foreground;

	private HUD() {}
	
	public static void init() {
		HUD.foreground = ResourceLoader.image("Foreground");
	}

	public static void update() {

	}

	public static void draw(Graphics2D g2) {
		drawForeground(g2);
		drawPlayerStats(g2);
	}

	private static void drawForeground(Graphics2D g2) {
		Depict.image(g2, foreground, 0, 0, 960, 720);
	}
	
	private static void drawPlayerStats(Graphics2D g2) {
		drawStat(g2,	"SCORE",	pad(PlayingStats.getScore(), BUFFER_ZERO),	260, 290);
		drawStat(g2,	"GRAZE",	pad(PlayingStats.getGraze(), 6),			260, 215);
		drawStat(g2,	"POWER",	String.valueOf(PlayingStats.getPower()),	260, 140);
		drawStat(g2,	"LIVES",	String.valueOf(PlayingStats.getLives()),	260, 65);
		drawStat(g2,	"BOMBS",	String.valueOf(PlayingStats.getBombs()),	260, -10);
	}
	
	private static String pad(long value, int digits) {
		return String.format("%0" + digits + "d", value);
	}
	
	private static void drawStat(Graphics2D g2, String label, String value, int x, int y) {
		TextDrawer.draw(g2, label, x, y);
		TextDrawer.draw(g2, value, x, y - LABEL_OFFSET);
	}
}