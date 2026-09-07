package entity;

import java.awt.image.BufferedImage;

public class Appearance {
	public BufferedImage costume;
	public int color;
	public int pixelate;
	public int brightness;
	public int ghost;
	public double size;

	public Appearance() {
		color = 0;
		pixelate = 0;
		brightness = 0;
		ghost = 0;
		size = 100;
	}

	public void setCostume(BufferedImage costume) { this.costume = costume; }
	public void setColor(int color) { this.color = Math.max(0, Math.min(255, color)); }
	public void changeColor(int amount) { setColor(color + amount); }
	public void setPixelate(int pixelate) { this.pixelate = Math.max(0, pixelate); }
	public void changePixelate(int amount) { setPixelate(pixelate + amount); }
	public void setBrightness(int brightness) { this.brightness = Math.max(-100, Math.min(100, brightness)); }
	public void changeBrightness(int amount) { setBrightness(brightness + amount); }
	public void setGhost(int ghost) { this.ghost = Math.max(0, Math.min(100, ghost)); }
	public void changeGhost(int amount) { setGhost(ghost + amount); }
	public void setSize(double size) { this.size = Math.max(0, size); }
	public void changeSize(double amount) { setSize(size + amount); }
}