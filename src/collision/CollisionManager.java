package collision;

import entity.Entity;
import entity.Player;
import entity.Bullet;
import game.BulletManager;

import game.GameStats;

public class CollisionManager {
	private Player player;
	private BulletManager bulletManager;
	
	public CollisionManager(Player player, BulletManager bulletManager){
		this.player = player;
		this.bulletManager = bulletManager;
	}
	
	public void update(){
		for(Bullet bullet : bulletManager.getBullets()){
			if(check(player, bullet)){
				System.out.println( "[Collision] "+ player.getName()+ " <-hit-> "+ bullet.getName() );
				GameStats.addDeath(); }
		}
	}
	
	public static boolean check( Entity a, Entity b ){
		for (Hitbox ha : a.getHitboxes()) {
			if(!ha.isEnabled()) continue;

			for (Hitbox hb : b.getHitboxes()) {
				if (!hb.isEnabled()) continue;

				if (ha.checkCollision(hb)) return true;
			}
		}

		return false;
	}
}