package net.heyitsultra.sandblock.Client.Entity.Mob;

import net.heyitsultra.UltraNet.serialization.*;
import net.heyitsultra.sandblock.Client.*;
import net.heyitsultra.sandblock.Client.Entity.Projectile.*;
import net.heyitsultra.sandblock.Client.Graphics.*;
import net.heyitsultra.sandblock.Client.Inventory.*;
import net.heyitsultra.sandblock.Client.World.*;

public class ClientPlayer extends Player {
	
	public double walkspeed = 1;
	public double runspeed = 2;
	private double speed;
	
	public String name;
	public boolean running;
	public boolean crouching;

	private Inventory inv;
	
	public ClientPlayer(int x, int y, int health, SpriteSheet sheet, String name) {
		super(x, y, health, sheet, name);
	}
	
	public void init(World w, Game g) {
		world = w;
		inv = new Inventory();
		inv.init(w, g);
	}
	
	public UNDatabase getData() {
		UNDatabase db = new UNDatabase("Player");
		UNObject obj = new UNObject("Pos");
		obj.addField(UNField.Double("x", x));
		obj.addField(UNField.Double("y", y));
		obj.addField(UNField.Double("z", z));
		obj.addField(UNField.Boolean("running", running));
		obj.addField(UNField.Boolean("crouching", crouching));
		return db;
	}
	
	public void update(boolean upKey, boolean downKey, boolean leftKey, boolean rightKey, boolean jumpKey, boolean crouchKey, boolean runKey, int mouseFromCenterX, int mouseFromCenterY, boolean fireKey) {
		if (ticks % 60 == 0 && health < 100)
			health++;

		if (runKey)
			speed = runspeed;
		else
			speed = walkspeed;
		if (crouchKey) speed *= 0.5;
		if (z > 0) speed += 1;
		
		if (jumpKey && z <= 0)
			za = 4;
		
		if (upKey) ya = -speed;
		if (downKey) ya = speed;
		if (!upKey && !downKey) ya = 0;
		
		if (leftKey) xa = -speed;
		if (rightKey) xa = speed;
		if (!leftKey && !rightKey) xa = 0;
		
		if (fireKey) {
			double dir = Math.atan2(mouseFromCenterY, mouseFromCenterX);
			Projectile p = new Fireball(x, y, dir);
			if (ticks % p.getRate() == 0)
				shoot(new Fireball(x - 8, y - 16, dir));
		}
		// if (key.c && ticks % 10 == 0)
		// 	world.spawn(new NPC(x >> 4, y >> 4, 100, SpriteSheet.nick, "NPC"));
		
		move();
		
		inv.update();
		ticks++;
	}
	
	public void render(Screen screen) {
		boolean xflip = false;
		Sprite sprite = side0;
		
		if (dir == Direction.RIGHT || dir == Direction.LEFT) {
			sprite = side0;
			if (walking) {
				sprite = side.get(ticks);
			}
			if (crouching) {
				sprite = side5;
			}
			if (dir == Direction.RIGHT) xflip = true;
		}
		if (dir == Direction.DOWN) {
			sprite = front0;
			if (walking) {
				sprite = front.get(ticks);
			}
			if (crouching) {
				sprite = front5;
			}
		}
		if (dir == Direction.UP) {
			sprite = back0;
			if (walking) {
				sprite = back.get(ticks);
			}
			if (crouching) {
				sprite = back5;
			}
		}
		
		inv.render(screen);
		
		int shadow = -25;
		shadow += z;
		if (shadow > 0) shadow = 0;
		screen.renderLight((int) x - 15, (int) y - 31, sprite, true, xflip, false, shadow - (int) za);
		screen.renderSprite((int) x - 16, (int) y - 32 - (int) z, sprite, 1, xflip, false, true);
		frames++;
	}
	
}
