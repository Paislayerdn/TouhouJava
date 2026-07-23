package game;

import entity.Bullet;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class BulletManager {

    private final ArrayList<Bullet> bullets = new ArrayList<>();

    public void spawn(Bullet bullet) {
        bullets.add(bullet);
    }

    public void update() {
        for (Bullet bullet : bullets) {
            bullet.update();
        }
    }

    public void draw(Graphics2D g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }
	
}