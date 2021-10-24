package net.heyitsultra.sandblock.Client.Entity;

import net.heyitsultra.sandblock.Client.Entity.Particle.Particle;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;
import net.heyitsultra.sandblock.Client.World.World;

public class Spawner extends Entity {
	
	public enum Type {
		MOB, PARTICLE, PROJECTILE;
	}
	
	// private Type type;
	
	public Spawner(int x, int y, Type type, int amount, World world) {
		init(world);
		for (int i = 0; i < amount; i++) {
			if (type == Type.PARTICLE) {
				world.spawn(new Particle(x, y, 50, Sprite.dirt));
			}
		}
	}
	
}
