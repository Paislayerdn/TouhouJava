package entity;

import java.awt.image.BufferedImage;
import graphics.java2d.AppearanceCache;

public final class Appearance {
	public BufferedImage costume;
	public float color;
	public float desaturation;
	public float pixelate;
	public float brightness;
	public float ghost;
	public float size;

	public Appearance() {
		color = 0;
		desaturation = 0;
		pixelate = 0;
		brightness = 0;
		ghost = 0;
		size = 100;
	}

	public BufferedImage getRenderedCostume() {
		return AppearanceCache.get(
			costume,
			color,
			desaturation,
			pixelate,
			brightness
		);
	}

	public void setCostume(BufferedImage costume) { this.costume = costume; }
	public void setColor(float color) { this.color = ((color % 256) + 256) % 256; }
	public void changeColor(float amount) { setColor(color + amount); }
	public void setDesaturation(float dst) { this.desaturation = Math.max(0, Math.min(100, dst)); }
	public void changeDesaturation(float desat) { setDesaturation(desaturation + desat); }
	public void setPixelate(float pxl) { this.pixelate = Math.max(0, pxl); }
	public void changePixelate(float amount) { setPixelate(pixelate + amount); }
	public void setBrightness(float brt) { this.brightness = Math.max(-100, Math.min(100, brt)); }
	public void changeBrightness(float amount) { setBrightness(brightness + amount); }
	// These do NOT require regenerating the image.
	public void setGhost(float ghost) { this.ghost = Math.max(0, Math.min(100, ghost)); }
	public void changeGhost(float amount) { setGhost(ghost + amount); }
	public void setSize(float size) { this.size = Math.max(0, size); }
	public void changeSize(float amount) { setSize(size + amount); }
}