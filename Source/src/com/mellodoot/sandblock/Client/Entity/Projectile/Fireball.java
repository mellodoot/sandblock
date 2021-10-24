package com.mellodoot.sandblock.Client.Entity.Projectile;

import com.mellodoot.sandblock.Client.Graphics.Sprite;

public class Fireball extends Projectile {
		
	public Fireball(double x, double y, double angle) {
		super(x, y, angle);
		speed = 5;
		range = 200;
		damage = 5;
		rate = 10;
		sprite = Sprite.fireball;
		
		xa = Math.cos(angle) * speed;
		ya = Math.sin(angle) * speed;
	}

}
