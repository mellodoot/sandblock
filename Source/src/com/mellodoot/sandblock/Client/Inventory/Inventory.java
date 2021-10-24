package com.mellodoot.sandblock.Client.Inventory;

import com.mellodoot.sandblock.Client.Game;
import com.mellodoot.sandblock.Client.Graphics.Font;
import com.mellodoot.sandblock.Client.Graphics.Screen;
import com.mellodoot.sandblock.Client.Graphics.Sprite;
import com.mellodoot.sandblock.Client.Graphics.SpriteSheet;
import com.mellodoot.sandblock.Client.Input.Keyboard;
import com.mellodoot.sandblock.Client.Input.Mouse;
import com.mellodoot.sandblock.Client.UI.UI;
import com.mellodoot.sandblock.Client.Util.Vector2i;
import com.mellodoot.sandblock.Client.World.Tile;
import com.mellodoot.sandblock.Client.World.World;

public class Inventory {
	
	public int width = 9, height = 3;
	public boolean open;
	public int selected = 1;
	public Tile tile = Tile.stone;
	public World world;
	public Mouse mouse;
	public Keyboard key;
	public int blockCooldown;
	public int cooldown;

	private Game game;
	
	public SpriteSheet tileSheet = new SpriteSheet("/ui/tile.png", 18);
	public Sprite tile1 = new Sprite(0, 0, tileSheet);
	public Sprite tile2 = new Sprite(1, 0, tileSheet);
	
	private Vector2i cPos = new Vector2i(0, 0);
	
	public Tile[] inventory;
	public Tile[] hotbar;
	public UI invui;
	public UI hotui;
	
	public Inventory() {
		inventory = new Tile[width * height];
		hotbar = new Tile[9];
		for (int i = 0; i < inventory.length; i++) {
			inventory[i] = Tile.getTile(i + 1);
		}
		
		hotbar[0] = Tile.grass;
		hotbar[1] = Tile.dirt;
		hotbar[2] = Tile.stone;
		hotbar[3] = Tile.oakwood;
		hotbar[4] = Tile.oaklog;
		hotbar[5] = Tile.brick;
		hotbar[6] = Tile.oakleaves;
		hotbar[7] = Tile.stonepath;
		hotbar[8] = Tile.water;
	}
	
	public void init(World world, Game game) {
		this.world = world;
		this.game = game;
		
		invui = new UI(64, 64, 16, 16, false, UI.Type.BOX, "inventory");
		world.add(invui);
		//invui = world.search("inventory");
		invui.visible = false;
		invui.addText(8, 8, "Inventory", 0xFFFFFF, Font.ultra);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int xa = x * 17 + 8;
				int ya = y * 17 + 22;
				Tile tile = inventory[x + y * width];
				if (tile != Tile.nulltile) invui.addSprite(xa, ya, tile.sprite, false, false);
			}
		}
		
		hotui = new UI(game.width / 2 - game.scale - 86, game.height - 36, 1, 1, false, UI.Type.INVIS, "hotbar");
		world.add(hotui);
		//hotui = world.search("hotbar");
		for (int i = 0; i <= 8; i++) {
			hotui.addText(19 * i + 6, -9, "" + (i + 1), 0xFFFFFF, Font.ultra);
			hotui.addSprite(19 * i, 0, tile1, false, false);
			hotui.addSprite(19 * i + 1, 1, Tile.stone.sprite, false, false);
		}
	}
	
	public void update() {
		Keyboard key = game.key;
		Mouse mouse = game.mouse;

		if (cooldown > 0) cooldown--;
		if (blockCooldown > 0) blockCooldown--;
		
		if (key.num1)
			selected = 1;
		if (key.num2)
			selected = 2;
		if (key.num3)
			selected = 3;
		if (key.num4)
			selected = 4;
		if (key.num5)
			selected = 5;
		if (key.num6)
			selected = 6;
		if (key.num7)
			selected = 7;
		if (key.num8)
			selected = 8;
		if (key.num9)
			selected = 9;
		
		if (mouse.getWheelRotation() > 0) {
			if (selected == 9)
				selected = 1;
			else
				selected++;
		}
		if (mouse.getWheelRotation() < 0) {
			if (selected == 1)
				selected = 9;
			else
				selected--;
		}
		
		if (key.e && cooldown <= 0) {
			open = !open;
			cooldown = 10;
		}
		
		tile = hotbar[selected - 1];
		
		cPos.set(game.xScroll + mouse.getX() >> 4, game.yScroll + mouse.getY() >> 4);
		if (!open) {
			if (mouse.getButton() == 1 && blockCooldown <= 0) {
				world.setTile(cPos.getX(), cPos.getY(), tile);
				blockCooldown = 5;
			}
		}
		
		invui.visible = open;
		invui.resize(20, 9);
		invui.move(game.width - 187, 16);
		
		if (open && mouse.getButton() == 1) {
			int xOffset = invui.x + 8;
			int yOffset = invui.y + 22;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int xa = x * 17 + xOffset;
					int ya = y * 17 + yOffset;
					if (mouse.within(xa, ya, xa + 16, ya + 16)) {
						hotbar[selected - 1] = inventory[x + y * width];
					}
				}
			}
		}
	}
	
	public void render(Screen screen) {
		hotui.move(game.centW - 86, game.height - 36);
		
		for (int i = 0; i < 9; i++) {
			hotui.text.get(i).move(19 * i + 1, 1);
			if (selected - 1 == i) hotui.sprites.get(i * 2).replace(tile2);
			else hotui.sprites.get(i * 2).replace(tile1);
			Tile tile = hotbar[i];
			if (tile != Tile.nulltile) hotui.sprites.get(i * 2 + 1).replace(tile.sprite);			
		}
		
		screen.renderLight(cPos.getX() << 4, cPos.getY() << 4, Tile.nulltile.sprite, true, false, false, 25);
	}
	
}
