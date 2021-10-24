package net.heyitsultra.sandblock.Client.Entity;

import net.heyitsultra.sandblock.Client.Graphics.Screen;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;
import net.heyitsultra.sandblock.Client.World.Tile;
import net.heyitsultra.sandblock.Client.World.World;

public class Entity {
	
	public double x, y, z;
	public double xa, ya, za;
	public Sprite sprite;
	public boolean removed = false;
	public World world;
	public int ticks;
	public int frames;
	
	public Entity() {
		
	}
	
	public Entity(int x, int y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void update() {
		move();
		ticks++;
	}
	
	protected void move() {
		x += xa;
		y += ya;
		z += za;
	}
	
	public void render(Screen screen) {
		int x = (int) (this.x + (sprite.width / 2));
		int y = (int) (this.y + (sprite.height / 2));
		int z = (int) this.z;
		screen.renderLight(x + 1, y + 1, sprite, true, false, false, -25);
		screen.renderSprite(x, y - z, sprite, true);
		frames++;
	}
	
	public void postRender(Screen screen) {
	}
	
	public void init(World w) {
		world = w;
	}
	
	public Tile standingOn(int xa, int ya) {
		return world.getTile((int) ((x / 16) + xa), (int) ((y / 16) + ya));
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	protected boolean collision(double xa, double ya) {
		int x = (int) (this.x + xa + sprite.getWidth() / 2);
		int y = (int) (this.y + ya + sprite.getHeight() / 2);
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = (x + c % 2 * sprite.getWidth()) >> 4;
			int yt = (y + c / 2 * sprite.getHeight()) >> 4;
			if (world.getTile(xt, yt).solid)
				solid = true;
		}
		return solid;
	}
	
	public void remove() {
		removed = true;
	}
	
}
