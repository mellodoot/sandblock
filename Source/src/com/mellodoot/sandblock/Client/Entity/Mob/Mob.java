package com.mellodoot.sandblock.Client.Entity.Mob;

import java.util.Random;

import com.mellodoot.sandblock.Client.Entity.Entity;
import com.mellodoot.sandblock.Client.Entity.Projectile.Projectile;
import com.mellodoot.sandblock.Client.Graphics.AnimSprite;
import com.mellodoot.sandblock.Client.Graphics.Font;
import com.mellodoot.sandblock.Client.Graphics.Screen;
import com.mellodoot.sandblock.Client.Graphics.Sprite;
import com.mellodoot.sandblock.Client.Graphics.SpriteSheet;
import com.mellodoot.sandblock.Client.World.World;

public class Mob extends Entity {
	
	protected String name;
	protected boolean walking;
	protected boolean crouching;
	protected boolean running;
	protected int health, maxhealth;
	protected Random random = new Random();
	
	protected enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	protected Direction dir;
	
	public Sprite side0 = new Sprite();
	public Sprite side1 = new Sprite();
	public Sprite side2 = new Sprite();
	public Sprite side3 = new Sprite();
	public Sprite side4 = new Sprite();
	public Sprite[] sides = {side1, side2, side1, side0, side3, side4, side3, side0};
	public AnimSprite side;
	
	public Sprite front0 = new Sprite();
	public Sprite front1 = new Sprite();
	public Sprite front2 = new Sprite();
	public Sprite front3 = new Sprite();
	public Sprite front4 = new Sprite();
	public Sprite[] fronts = {front1, front2, front1, front0, front3, front4, front3, front0};
	public AnimSprite front;
	
	public Sprite back0 = new Sprite();
	public Sprite back1 = new Sprite();
	public Sprite back2 = new Sprite();
	public Sprite back3 = new Sprite();
	public Sprite back4 = new Sprite();
	public Sprite[] backs = {back1, back2, back1, back0, back3, back4, back3, back0};
	public AnimSprite back;
	
	public Sprite side5 = new Sprite();
	public Sprite front5 = new Sprite();
	public Sprite back5 = new Sprite();
	
	public Mob(double x, double y, int health, SpriteSheet sheet, String name) {
		this.x = x;
		this.y = y;
		this.health = health;
		this.name = name;
		this.maxhealth = health;
		setSheet(sheet);
	}
	
	public void init(World w) {
		world = w;
	}
	
	public String getName() {
		return name;
	}
	
	public void update() {
		ticks++;
	}
	
	protected void shoot(Projectile p) {
		world.spawn(p);
	}
	
	protected void move() {
		if (x + xa >= 0 && x + xa < world.width << 4) {
			if (!collision(xa, 0))
				x += xa;
		}
		if (y + ya >= 0 && y + ya < world.height << 4) {
			if (!collision(0, ya))
				y += ya;
		}
		
		walking = (Math.abs(xa) > 0.5 || Math.abs(ya) > 0.5);
		
		if (z > 0) this.za -= 0.5;
		z += za;
		if (z < 0) {
			z = 0;
			this.za = 0;
		}
		
		if (ya < 0) dir = Direction.UP;
		if (ya > 0) dir = Direction.DOWN;
		if (xa < 0) dir = Direction.LEFT;
		if (xa > 0) dir = Direction.RIGHT;
	}
	
	public void render(Screen screen) {
		boolean xflip = false;
		Sprite sprite = side0;
		
		if (dir == Direction.RIGHT || dir == Direction.LEFT) {
			sprite = side0;
			if (walking) {
				sprite = side.get(frames);
			}
			if (crouching) {
				sprite = side5;
			}
			if (dir == Direction.RIGHT) xflip = true;
		}
		if (dir == Direction.DOWN) {
			sprite = front0;
			if (walking) {
				sprite = front.get(frames);
			}
			if (crouching) {
				sprite = front5;
			}
		}
		if (dir == Direction.UP) {
			sprite = back0;
			if (walking) {
				sprite = back.get(frames);
			}
			if (crouching) {
				sprite = back5;
			}
		}
		
		int shadow = -25;
		shadow += z;
		if (shadow > 0) shadow = 0;
		screen.renderLight((int) x - 15, (int) y - 31, sprite, true, xflip, false, shadow - (int) za);
		screen.renderSprite((int) x - 16, (int) y - 32 - (int) z, sprite, 1, xflip, false, true);
		frames++;
	}
	
	public void postRender(Screen screen) {
		int x = (int) this.x;
		int y = (int) this.y;
		int z = (int) this.z;
		
		int healthX = -25;
		int healthY = -41;
		if (crouching) healthY += 9;
		screen.renderLight(x + healthX - 1, y + healthY - z + 1, Sprite.healthbar1, true, false, false, -25);
		screen.renderSprite(x + healthX - 2, y + healthY - z, Sprite.healthbar1, true);
		for (int i = 0; i < 24; i++) {
			screen.renderLight(x + healthX + i * 2 + 1, y + healthY - z + 1, Sprite.healthbar2, true, false, false, -25);
			screen.renderSprite(x + healthX + i * 2, y + healthY - z, Sprite.healthbar2, true);
		}
		screen.renderLight(x + healthX + 48 + 1, y + healthY - z + 1, Sprite.healthbar3, true, false, false, -25);
		screen.renderSprite(x + healthX + 48, y + healthY - z, Sprite.healthbar3, true);
		int healthCol = 0x00FF00;
		if (health < maxhealth * 0.75) healthCol = 0x88FF00;
		if (health < maxhealth * 0.5) healthCol = 0xFF8800;
		if (health < maxhealth * 0.25) healthCol = 0xFF0000;
		for (int i = 0; i < Math.abs(health) / 2; i++) {
			screen.renderTint(x + healthX + i - 1, y + healthY - z, Sprite.healthbar4, true, false, false, healthCol);
		}
		
		Font font = new Font();
		if (health < 100) healthX += 3;
		if (health < 10) healthX += 3;
		font.render(x + healthX + 15, y + healthY - z, "" + health, screen, true);
		int nameX = -name.length() * 6 / 2;
		int nameY = -51;
		if (crouching) nameY += 9;
		font.render(x + nameX, y + nameY - z, name, screen, true);
	}
	
	protected boolean collision(double xa, double ya) {
		int x = (int) (this.x + xa);
		int y = (int) (this.y + ya);
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = (x - c % 2 * 10 + 4) >> 4;
			int yt = (y - c / 2 * 4) >> 4;
			if (world.getTile(xt, yt).solid)
				solid = true;
		}
		return solid;
	}
	
	protected void setSheet(SpriteSheet sheet) {
		side0.set(0, 0, sheet);
		side1.set(1, 0, sheet);
		side2.set(2, 0, sheet);
		side3.set(3, 0, sheet);
		side4.set(4, 0, sheet);
		side = new AnimSprite(sides, 4);
		front0.set(0, 1, sheet);
		front1.set(1, 1, sheet);
		front2.set(2, 1, sheet);
		front3.set(3, 1, sheet);
		front4.set(4, 1, sheet);
		front = new AnimSprite(fronts, 4);
		back0.set(0, 2, sheet);
		back1.set(1, 2, sheet);
		back2.set(2, 2, sheet);
		back3.set(3, 2, sheet);
		back4.set(4, 2, sheet);
		back = new AnimSprite(backs, 4);
		side5.set(0, 3, sheet);
		front5.set(1, 3, sheet);
		back5.set(2, 3, sheet);
	}

}
