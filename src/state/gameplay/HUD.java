package state.gameplay;

import graphics.Renderer;

import java.awt.image.BufferedImage;

import resource.ResourceLoader;

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

	public static void draw(Renderer renderer) {
		drawForeground(renderer);
		drawPlayerStats(renderer);
	}

	private static void drawForeground(Renderer renderer) {
		renderer.image(foreground, 0, 0, 960, 720);
	}
	
	private static void drawPlayerStats(Renderer renderer) {
		drawStat(renderer,	"SCORE",	pad(PlayingStats.getScore(), BUFFER_ZERO),	260, 290);
		drawStat(renderer,	"GRAZE",	pad(PlayingStats.getGraze(), 6),			260, 215);
		drawStat(renderer,	"POWER",	String.valueOf(PlayingStats.getPower()),	260, 140);
		drawStat(renderer,	"LIVES",	String.valueOf(PlayingStats.getLives()),	260, 65);
		drawStat(renderer,	"BOMBS",	String.valueOf(PlayingStats.getBombs()),	260, -10);
	}
	
	private static String pad(long value, int digits) {
		return String.format("%0" + digits + "d", value);
	}
	
	private static void drawStat(Renderer renderer, String label, String value, int x, int y) {
		renderer.text(label, x, y);
		renderer.text(value, x, y - LABEL_OFFSET);
	}
}