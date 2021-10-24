package net.heyitsultra.sandblock.Client.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// import net.heyitsultra.sandblock.Client.Game;
import net.heyitsultra.sandblock.Client.Graphics.Screen;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;

public class Tile {
	
	public int id, data;
	public Sprite sprite;
	public boolean solid;
	public boolean wall;
	private static List<Tile> tiles = new ArrayList<Tile>();
	private Random random = new Random();
	
	public static Tile nulltile = new Tile(0, Sprite.nullTile, false, false);
	public static Tile stone = new Tile(1, Sprite.stone, false, false);
	public static Tile grass = new Tile(2, Sprite.grass, false, false);
	public static Tile dirt = new Tile(3, Sprite.dirt, false, false);
	public static Tile oakwood = new Tile(4, Sprite.oakwood, false, false);
	public static Tile birchwood = new Tile(5, Sprite.birchwood, false, false);
	public static Tile junglewood = new Tile(6, Sprite.junglewood, false, false);
	public static Tile sprucewood = new Tile(7, Sprite.sprucewood, false, false);
	public static Tile cobblestone = new Tile(8, Sprite.cobblestone, false, false);
	public static Tile water = new Tile(9, Sprite.water, true, false);
	public static Tile oaklog = new Tile(10, Sprite.oaklog, true, true);
	public static Tile birchlog = new Tile(11, Sprite.birchlog, true, true);
	public static Tile junglelog = new Tile(12, Sprite.junglelog, true, true);
	public static Tile brick = new Tile(13, Sprite.brick, true, true);
	public static Tile tree = new Tile(14, Sprite.oaklog, true, false);
	public static Tile oakleaves = new Tile(15, Sprite.oakleaves, false, false);
	public static Tile birchleaves = new Tile(15, Sprite.birchleaves, false, false);
	public static Tile jungleleaves = new Tile(15, Sprite.jungleleaves, false, false);
	public static Tile stonepath = new Tile(16, Sprite.stonepath, false, false);
	public static Tile ironblock = new Tile(17, Sprite.ironblock, true, true);
	public static Tile goldblock = new Tile(18, Sprite.goldblock, true, true);
	public static Tile copperblock = new Tile(19, Sprite.copperblock, true, true);
	public static Tile diamondblock = new Tile(20, Sprite.diamondblock, true, true);
	public static Tile steelblock = new Tile(21, Sprite.steelblock, true, true);
	public static Tile spawn = new Tile(404, Sprite.spawn, false, false);
	
	private Tile(int id, Sprite sprite, boolean solid, boolean wall) {
		this.id = id;
		this.sprite = sprite;
		this.solid = solid;
		this.wall = wall;
		tiles.add(this);
	}
	
	public Tile(Tile tile, int data) {
		if (data == 0) {
			int limit = 0;
			if (tile == Tile.grass)
				limit = 16;
			if (tile == Tile.tree) {
				limit = 2;
			}
			
			if (limit == 0)	data = 0;
			else data = random.nextInt(limit);
		}
		
		this.id = tile.id;
		this.sprite = tile.sprite;
		this.solid = tile.solid;
		this.wall = tile.wall;
		this.data = data;
	}
	
	public static Tile getTile(double search) {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).id == search) {
				return tiles.get(i);
			}
		}
		return nulltile;
	}
	
	public static Tile random() {
		Random random = new Random();
		return tiles.get(random.nextInt(tiles.size() - 1) + 1);
	}
	
	public void render(int x, int y, Screen screen, World world) {
		if (id == 0) {
			screen.renderTile(x, y, sprite, true);
			return;
		}
		
		if (world.getTile(x, y - 1) != this) {
			if (!world.getTile(x, y - 1).wall) {
				if (this == grass) {
					screen.renderTile(x, y - 1, Sprite.grass2, true);;
				}
				if (this == dirt) {
					screen.renderTile(x, y - 1, Sprite.dirt2, true);;
				}
			}
		}
		
		screen.renderTile(x, y, sprite, true);
		
		if (id == grass.id) {
			if (data == 0)
				screen.renderTile(x, y, Sprite.pebbles1, true);
			if (data == 1)
				screen.renderTile(x, y, Sprite.pebbles2, true);
			if (data == 2)
				screen.renderTile(x, y, Sprite.wildgrass1, true);
			if (data == 3)
				screen.renderTile(x, y, Sprite.wildgrass2, true);
			return;
		}
		
		if (id == tree.id) {
			Tile log = oaklog;
			Tile leaves = oakleaves;
			if (data == 1) {
				log = birchlog;
				leaves = birchleaves;
			}
			if (data == 2) {
				log = junglelog;
				leaves = jungleleaves;
			}
			
			for (int i = 0; i < 3; i++) {
				world.setPost(x, y - 1 - i, log);
			}
			world.setPost(x, y - 4, leaves);
		}
	}
	
}
