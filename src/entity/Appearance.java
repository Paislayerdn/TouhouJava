package entity;

import java.awt.image.BufferedImage;
import graphics.AppearanceCache;

public class Appearance {
	public BufferedImage costume;
	public double color;
	public double pixelate;
	public double brightness;
	public double ghost;
	public double size;

	public Appearance() {
		color = 0;
		pixelate = 0;
		brightness = 0;
		ghost = 0;
		size = 100;
	}

	public BufferedImage getRenderedCostume() {
		return AppearanceCache.get(
			costume,
			color,
			pixelate,
			brightness
		);
	}

	public void setCostume(BufferedImage costume) { this.costume = costume; }
	public void setColor(double color) { this.color = ((color % 256) + 256) % 256; }
	public void changeColor(double amount) { setColor(color + amount); }
	public void setPixelate(double pixelate) { this.pixelate = Math.max(0, pixelate); }
	public void changePixelate(double amount) { setPixelate(pixelate + amount); }
	public void setBrightness(double brightness) { this.brightness = Math.max(-100, Math.min(100, brightness)); }
	public void changeBrightness(double amount) { setBrightness(brightness + amount); }
	// These do NOT require regenerating the image.
	public void setGhost(double ghost) { this.ghost = Math.max(0, Math.min(100, ghost)); }
	public void changeGhost(double amount) { setGhost(ghost + amount); }
	public void setSize(double size) { this.size = Math.max(0, size); }
	public void changeSize(double amount) { setSize(size + amount); }
}