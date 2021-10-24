package net.heyitsultra.sandblock.Client.World;

import net.heyitsultra.sandblock.Client.Graphics.*;

public class Chunk {
	
	public int x, y;
	private Tile[] tiles;
	private int size = 8;
	private World world;
	
	public Chunk(World world) {
		generate(1);
	}
	
	public void generate(int scale) {
		tiles = new Tile[scale * scale];
		
		if (scale <= 0) scale = 1;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				// double sampleX = x / scale;
				// double sampleY = y / scale;
				double random = Math.random();
				
				tiles[x + y * size] = Tile.getTile((int) random);
			}
		}
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	
	public void update() {
		
	}
	
	public void render(Screen screen) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				tiles[x + y * size].render(this.x + x, this.y + y, screen, world);
			}
		}
	}
	
}
