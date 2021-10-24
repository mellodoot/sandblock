package com.mellodoot.sandblock.Client.Entity.Projectile;

import com.mellodoot.sandblock.Client.Entity.Entity;
import com.mellodoot.sandblock.Client.Entity.Spawner;
import com.mellodoot.sandblock.Client.Util.Vector2i;

public abstract class Projectile extends Entity {
	
	protected double angle;
	protected double xo, yo;
	protected double speed, rate, range, damage;
	
	public Projectile(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		xo = x;
		yo = y;
		this.angle = angle;
		
		xa = Math.cos(angle) * speed;
		ya = Math.sin(angle) * speed;
	}
	
	public void update() {
		Vector2i pos = new Vector2i((int) x, (int) y);
		Vector2i origin = new Vector2i((int) xo, (int) yo);
		if (Vector2i.getDistance(pos, origin) > range) remove();
		
		if (collision(xa, ya)) {
			world.spawn(new Spawner((int) x + sprite.width / 2, (int) y + sprite.width / 2, Spawner.Type.PARTICLE, 10, world));
			remove();
		};
		move();
		
		x += xa;
		y += ya;
	}
	
	public double getRate() {
		return rate;
	}

	public double getDamage() {
		return damage;
	}
	
}
