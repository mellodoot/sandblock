package net.heyitsultra.sandblock.Client.Entity.Particle;

import java.util.Random;

import net.heyitsultra.sandblock.Client.Entity.Entity;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;

public class Particle extends Entity {
	
	public double xa, ya;
	public int life;
	private Random random = new Random();
	
	public Particle(double x, double y, int life, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.life = life + random.nextInt(5) * 10;
		if (life >= 10000) life = 10000;
		this.sprite = new Sprite(2, 0xFF00FF);
		
		xa = random.nextGaussian();
		ya = random.nextGaussian();
	}
	
	public void update() {
		life--;
		if (life < 0 || collision(0, 0)) remove();
		if (collision(xa, 0))
			xa *= -0.1;
		if (collision(0, ya))
			ya *= -0.1;
		x += xa;
		y += ya;
	}
	
}
