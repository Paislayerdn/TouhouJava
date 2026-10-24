package entity;

import action.ScriptProperty;

public class Position2D {
	@ScriptProperty private float x;
	@ScriptProperty private float y;

	public Position2D() { this(0, 0); }
	public Position2D(float x, float y) { set(x, y); }
	public float getX() { return x; }
	public float getY() { return y; }
	public void setX(float value) { x = value; }
	public void setY(float value) { y = value; }
	public void set(float x, float y) { this.x = x; this.y = y; }
	public void move(float dx, float dy) { x += dx; y += dy; }
}
