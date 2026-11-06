package entity;

import action.ScriptProperty;
import action.ScriptPropertySetter;
import java.awt.image.BufferedImage;
import graphics.java2d.AppearanceCache;

public final class Appearance implements ScriptPropertySetter {
	private BufferedImage costume;
	@ScriptProperty private float
			color, desaturation, pixelate,
			brightness, ghost, size;
	
	private long costumeRevision;

	public Appearance() {
		color = 0;
		desaturation = 0;
		pixelate = 0;
		brightness = 0;
		ghost = 0;
		size = 100;
	}

	public BufferedImage getRenderedCostume() { return AppearanceCache.get(costume, color, desaturation, pixelate, brightness); }

	public BufferedImage getCostume() { return costume; }
	public float getColor() { return color; }
	public float getDesaturation() { return desaturation; }
	public float getPixelate() { return pixelate; }
	public float getBrightness() { return brightness; }
	public float getGhost() { return ghost; }
	public float getSize() { return size; }

	public void setCostume(BufferedImage costume) {
		this.costume = costume;
		costumeRevision++;
	}
	public long getCostumeRevision() { return costumeRevision; }
	public void setColor(float color) { this.color = ((color % 256) + 256) % 256; }
	public void setDesaturation(float dst) { this.desaturation = Math.max(0, Math.min(100, dst)); }
	public void setPixelate(float pxl) { this.pixelate = Math.max(0, pxl); }
	public void setBrightness(float brt) { this.brightness = Math.max(-100, Math.min(100, brt)); }
	// These do NOT require regenerating the image.
	public void setGhost(float ghost) { this.ghost = Math.max(0, Math.min(100, ghost)); }
	public void setSize(float size) { this.size = Math.max(0, size); }

	@Override
	public boolean setScriptProperty(String name, Object value) {
		float amount = ((Number) value).floatValue();
		switch (name) {
			case "color" -> setColor(amount);
			case "desaturation" -> setDesaturation(amount);
			case "pixelate" -> setPixelate(amount);
			case "brightness" -> setBrightness(amount);
			case "ghost" -> setGhost(amount);
			case "size" -> setSize(amount);
			default -> { return false; }
		}
		return true;
	}
}
