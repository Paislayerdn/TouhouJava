package entity;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Entity {

	protected double x;
	protected double y;

	public abstract void update();

	public abstract void draw(Graphics2D g);

}