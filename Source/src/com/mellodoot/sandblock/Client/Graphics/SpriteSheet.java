package com.mellodoot.sandblock.Client.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private String path;
	public int width, height;
	public int spriteWidth, spriteHeight;
	public int[] pixels;
	protected SpriteSheet sheet;
	
	private Sprite[] sprites;
	public static SpriteSheet tiles = new SpriteSheet("/textures/tiles.png", 16);
	public static SpriteSheet tiles2 = new SpriteSheet("/textures/tiles2.png", 16);
	public static SpriteSheet special = new SpriteSheet("/textures/special.png", 16);
	public static SpriteSheet projectiles = new SpriteSheet("/textures/projectiles.png", 8);	
	
	public static SpriteSheet ultra = new SpriteSheet("/characters/ultra.png", 32);
	public static SpriteSheet nick = new SpriteSheet("/characters/nick.png", 32);

	public static SpriteSheet health = new SpriteSheet("/ui/health.png", 2, 8);
	
	public static SpriteSheet trophies = new SpriteSheet("/trophies/trophies.png", 16);
	public static SpriteSheet media = new SpriteSheet("/ui/media.png", 32);
	
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		int w = width * spriteSize;
		int h = height * spriteSize;
		pixels = new int[w * h];
		for (int y0 = 0; y0 < h; y0++) {
			int yp = yy + y0;
			for (int x0 = 0; x0 < w; x0++) {
				int xp = xx + x0;
				pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.width];
			}
		}
		
		int frame = 0;
		for (int ya = 0; ya < height; ya++) {
			for (int xa = 0; xa < width; xa++) {
				frame++;
				int[] spritePixels = new int[spriteSize * spriteSize];
				for (int y0 = 0; y0 < h; y0++) {
					for (int x0 = 0; x0 < w; x0++) {
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize) + (y0 + ya * spriteSize) * width];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;
			}
		}
	}
	
	public SpriteSheet(String path, int spriteWidth, int spriteHeight) {
		this.path = path;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		load();
	}
	
	public SpriteSheet(String path, int spriteSize) {
		this.path = path;
		spriteWidth = spriteSize;
		spriteHeight = spriteSize;
		load();
	}
	
	public Sprite[] getSprites() {
		return sprites;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int spriteWidth() {
		return spriteWidth;
	}
	
	public int spriteHeight() {
		return spriteHeight;
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	private void load() {
		try {
			System.out.print("[INFO] Loading spritesheet \"" + path + "\"...");
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			System.out.println("Success!");
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			int[] srcpixels = new int[width * height];
			image.getRGB(0, 0, width, height, srcpixels, 0, width);
			for (int i = 0; i < srcpixels.length; i++) {
				if (srcpixels[i] != 0xFF00FF)
					pixels[i] = srcpixels[i];
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed!");
		} catch (Exception e) {
			System.err.println("Failed!");
		}
	}
}
