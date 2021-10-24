package net.heyitsultra.sandblock.Client.Graphics;

public class Sprite {
	
	private int x, y;
	public int width, height;
	public int[] pixels;
	protected SpriteSheet sheet;
	
	public static Sprite nullTile = new Sprite(16, 16, 0xFF00FF);
	public static Sprite spawn = new Sprite(0, 7, SpriteSheet.tiles);
	public static Sprite stone = new Sprite(0, 0, SpriteSheet.tiles);
	public static Sprite grass = new Sprite(1, 0, SpriteSheet.tiles);
	public static Sprite grass2 = new Sprite(0, 0, SpriteSheet.tiles2);
	public static Sprite dirt = new Sprite(2, 0, SpriteSheet.tiles);
	public static Sprite dirt2 = new Sprite(1, 0, SpriteSheet.tiles2);
	public static Sprite oakwood = new Sprite(3, 0, SpriteSheet.tiles);
	public static Sprite birchwood = new Sprite(3, 1, SpriteSheet.tiles);
	public static Sprite junglewood = new Sprite(3, 2, SpriteSheet.tiles);
	public static Sprite sprucewood = new Sprite(3, 3, SpriteSheet.tiles);
	public static Sprite cobblestone = new Sprite(4, 0, SpriteSheet.tiles);
	public static Sprite water = new Sprite(5, 0, SpriteSheet.tiles);
	public static Sprite oaklog = new Sprite(6, 0, SpriteSheet.tiles);
	public static Sprite birchlog = new Sprite(6, 1, SpriteSheet.tiles);
	public static Sprite junglelog = new Sprite(6, 2, SpriteSheet.tiles);
	public static Sprite brick = new Sprite(7, 0, SpriteSheet.tiles);
	public static Sprite oakleaves = new Sprite(8, 0, SpriteSheet.tiles);
	public static Sprite birchleaves = new Sprite(8, 1, SpriteSheet.tiles);
	public static Sprite jungleleaves = new Sprite(8, 2, SpriteSheet.tiles);
	public static Sprite stonepath = new Sprite(9, 0, SpriteSheet.tiles);
	public static Sprite ironblock = new Sprite(10, 0, SpriteSheet.tiles);
	public static Sprite goldblock = new Sprite(11, 0, SpriteSheet.tiles);
	public static Sprite copperblock = new Sprite(12, 0, SpriteSheet.tiles);
	public static Sprite diamondblock = new Sprite(13, 0, SpriteSheet.tiles);
	public static Sprite steelblock = new Sprite(14, 0, SpriteSheet.tiles);
	
	public static Sprite fireball = new Sprite(0, 0, SpriteSheet.projectiles);

	public static Sprite pebbles1 = new Sprite(0, 0, SpriteSheet.special);
	public static Sprite pebbles2 = new Sprite(1, 0, SpriteSheet.special);
	public static Sprite wildgrass1 = new Sprite(0, 1, SpriteSheet.special);
	public static Sprite wildgrass2 = new Sprite(1, 1, SpriteSheet.special);
	
	public static Sprite healthbar1 = new Sprite(0, 0, SpriteSheet.health);
	public static Sprite healthbar2 = new Sprite(1, 0, SpriteSheet.health);
	public static Sprite healthbar3 = new Sprite(2, 0, SpriteSheet.health);
	public static Sprite healthbar4 = new Sprite(3, 0, SpriteSheet.health);
	
	public static Sprite[] uibox = Sprite.split(new SpriteSheet("/ui/box.png", 8));
	public static Sprite[] uibutton = Sprite.split(new SpriteSheet("/ui/button.png", 4));
	public static Sprite[] uitextbox = Sprite.split(new SpriteSheet("/ui/text.png", 2));
	
	public static Sprite[] trophies = split(SpriteSheet.trophies);
	public static Sprite[] media = split(SpriteSheet.media);
	
	public Sprite() {
	}
	
	public Sprite(int width, int height, int colour) {
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			pixels[i] = colour;
		}
	}
	
	public Sprite(int x, int y, SpriteSheet sheet) {
		if (sheet != null) {
			width = sheet.spriteWidth;
			height = sheet.spriteHeight;
		}
		pixels = new int[width * height];
		this.x = x * width;
		this.y = y * height;
		this.sheet = sheet;
		load();
	}
	
	public static Sprite[] split(SpriteSheet sheet) {
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.spriteWidth() * sheet.spriteHeight());
		Sprite[] sprites = new Sprite[amount];
		int current = 0;
		
		int[] pixels = new int[sheet.spriteWidth() * sheet.spriteHeight()];
		
		for (int yp = 0; yp < sheet.getHeight() / sheet.spriteHeight(); yp++) {
			for (int xp = 0; xp < sheet.getWidth() / sheet.spriteWidth(); xp++) {
				for (int y = 0; y < sheet.spriteHeight(); y++) {
					for (int x = 0; x < sheet.spriteWidth(); x++) {
						int xo = x + xp * sheet.spriteWidth();
						int yo = y + yp * sheet.spriteHeight();
						pixels[x + y * sheet.spriteWidth()] = sheet.getPixels()[xo + yo * sheet.getWidth()];
					}
				}
				
				sprites[current++] = new Sprite(pixels, sheet.spriteWidth(), sheet.spriteHeight());
			}
		}
		
		return sprites;
	}
	
	public Sprite(int size, int colour) {
		this.width = size;
		this.height = size;
		pixels = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			pixels[i] = colour;
		}
	}
	
	public Sprite(int[] pixels, int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int [pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void set(int x, int y, SpriteSheet sheet) {
		width = sheet.spriteWidth;
		height = sheet.spriteHeight;
		pixels = new int[width * height];
		this.x = x * width;
		this.y = y * height;
		this.sheet = sheet;
		load();
	}
	
	public SpriteSheet getSheet() {
		return sheet;
	}
	
	private void load() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (sheet != null)
					pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.width];
				else
					pixels[x + y * width] = 0x00FFFFFF;					
			}
		}
	}	
}