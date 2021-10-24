package net.heyitsultra.sandblock.Client.Entity.Mob;

import java.util.List;

import net.heyitsultra.sandblock.Client.Graphics.SpriteSheet;
import net.heyitsultra.sandblock.Client.Util.Vector2i;
import net.heyitsultra.sandblock.Client.World.Node;

public class NPC extends Mob {
	
	private double speed;
	private double walkspeed = 1;
	private double runspeed = 2;
	
	// private String name;
	// private int anim;
	private boolean running;
	// private boolean colliding;
	private boolean crouching;
	private List<Node> path;
	
	public NPC(int x, int y, int health, SpriteSheet sheet, String name) {
		super(x, y, health, sheet, name);
	}
	
	public void update() {
		
		xa = 0;
		ya = 0;
		
		List<Player> players = world.getPlayers(this, 10 << 4);
		if (players.size() > 0) {
			Player player = players.get(0);
			double px = player.getX();
			double py = player.getY();
			Vector2i start = new Vector2i((int) x >> 4, (int) y >> 4);
			Vector2i dest = new Vector2i((int) px >> 4, (int) py >> 4);
			double dist = Vector2i.getDistance(start, dest);
			if (ticks % 2 == 0 && dist > 3)
				path = world.findPath(start, dest);
			if (player.z > 0 && z <= 0)
				za = 5;
			if (player.crouching)
				crouching = true;
			else
				crouching = false;
		}
		
		if (running)
			speed = runspeed;
		else
			speed = walkspeed;
		if (crouching) speed *= 0.5;
		if (z > 0) speed += 1;
		
		if (path != null) {
			if (path.size() > 0) {
				Vector2i vec = path.get(path.size() - 1).pos;
				if (x < vec.getX() + 8 / 16 << 4) xa = speed;
				if (x > vec.getX() - 8 / 16 << 4) xa = -speed;
				if (y < vec.getY() + 8 / 16 << 4) ya = speed;
				if (y > vec.getY() - 8 / 16 << 4) ya = -speed;
			}
		}	
		
		move();
		ticks++;
	}
	
}
