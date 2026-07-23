package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Settings;

public class Bullet extends Entity {

    private double vx;
    private double vy;

    public Bullet(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;

        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public void update() {
        x += vx;
        y += vy;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)x - 4, (int)y - 4, 8, 12);
    }

}