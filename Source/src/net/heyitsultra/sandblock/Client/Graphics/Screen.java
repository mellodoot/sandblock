package net.heyitsultra.sandblock.Client.Graphics;

public class Screen {
	
	public int width, height;
	public int[] pixels;
	public int xOffset, yOffset;
	public final int ALPHA_COL1 = 0x00000000;
	public final int ALPHA_COL2 = 0x00FFFFFF;
	
	public Screen(int w, int h, int[] px) {
		pixels = px;
		width = w;
		height = h;
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x000000;
		}
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void renderSprite(int xp, int yp, Sprite sprite, int scale, boolean xflip, boolean yflip, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		if (sprite == null) sprite = Sprite.nullTile;
		for (int y = 0; y < sprite.height; y++) {
			int ya = y + yp;
			int yb = y;
			if (yflip) yb = sprite.height - y - 1;
			for (int x = 0; x < sprite.width; x++) {
				int xa = x + xp;
				if (xa < -sprite.width || xa >= width || ya < -sprite.height || ya >= height) break;
				int xb = x;
				if (xflip) xb = sprite.width - x - 1;
				int col = sprite.pixels[xb + yb * sprite.width];
				if (scale > 1) {
					int xc = xa * scale;
					int yc = ya * scale;
					for (int xd = 0; xd < scale; xd++) {
						for (int yd = 0; yd < scale; yd++) {
							setPixel(xc + xd, yc + yd, col);
						}
					}
				} else {
					setPixel(xa, ya, col);
				}
			}
		}
	}
	public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed) {
		renderSprite(xp, yp, sprite, 1, false, false, fixed);
	}
	
	public void renderTile(int xp, int yp, Sprite sprite, boolean fixed) {
		xp = xp << 4;
		yp = yp << 4;
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		for (int y = 0; y < sprite.height; y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.width; x++) {
				int xa = x + xp;
				if (xa < -sprite.width || xa >= width || ya < -sprite.height || ya >= height) break;
				int col = sprite.pixels[x + y * sprite.width];
				setPixel(xa, ya, col);
			}
		}
	}
	
	public void renderTextCharacter(int xp, int yp, Sprite sprite, int color, boolean fixed) {
		if (fixed) {
			xp -= xOffset; 
			yp -= yOffset;
		}
		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if (xa < -sprite.width || xa >= width || ya < -sprite.height || ya >= height) break;
				int col = sprite.pixels[x + y * sprite.width];
				if (col != ALPHA_COL1 && col != ALPHA_COL2) {
					if (col == 0xffffffff) {
						col = color;
						if (color == 0xffffff) col = 0xfefefe;
					}
				}
				setPixel(xa, ya, col);
			}
		}
	}
	
	public void renderLight(int xp, int yp, Sprite sprite, boolean fixed, boolean xflip, boolean yflip, int brightness) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		
		for (int y = 0; y < sprite.height; y++) {
			int ya = y + yp;
			int yb = y;
			if (yflip) yb = sprite.height - y - 1;
			for (int x = 0; x < sprite.width; x++) {
				int xa = x + xp;
				if (xa < -sprite.width || xa >= width || ya < -sprite.height || ya >= height) break;
				int xb = x;
				if (xflip) xb = sprite.width - x - 1;
				int col = sprite.pixels[xb + yb * sprite.width];
				if (col != ALPHA_COL1 && col != ALPHA_COL2)
					setPixel(xa, ya, Colour.adjustBrightness(getPixel(xa, ya), brightness));
			}
		}
	}
	
	public void renderTint(int xp, int yp, Sprite sprite, boolean fixed, boolean xflip, boolean yflip, int colour) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		
		for (int y = 0; y < sprite.height; y++) {
			int ya = y + yp;
			int yb = y;
			if (yflip) yb = sprite.height - y - 1;
			for (int x = 0; x < sprite.width; x++) {
				int xa = x + xp;
				if (xa < -sprite.width || xa >= width || ya < -sprite.height || ya >= height) break;
				int xb = x;
				if (xflip) xb = sprite.width - x - 1;
				int col = sprite.pixels[xb + yb * sprite.width];
				if (col != ALPHA_COL1 && col != ALPHA_COL2)
					setPixel(xa, ya, Colour.tint(getPixel(xa, ya), colour));
			}
		}
	}
	
	public void renderTiled(Sprite sprite, int xOffset, int yOffset) {
		int width = sprite.getWidth();
		int height = sprite.getHeight();
		for (int x = -width; x < this.width; x += width) {
			for (int y = -height; y < this.height; y += height) {
				renderSprite(x + xOffset, y + yOffset, sprite, false);
			}
		}
	}
	
	public void fillRect(int x, int y, int width, int height, int col, int brightness, boolean fixed) {
		if (fixed) {
			x -= xOffset; 
			y -= yOffset;
		}
		for (int ya = y; ya < y + height; ya++) {
			for (int xa = x; xa < x + width; xa++) {
				if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height) break;
				setPixel(xa, ya, Colour.adjustBrightness(getPixel(xa, ya), brightness));
			}
		}
	}
	
	public void fillRect(int x, int y, int width, int height, int col, boolean fixed) {
		if (fixed) {
			x -= xOffset; 
			y -= yOffset;
		}
		for (int ya = y; ya < y + height; ya++) {
			for (int xa = x; xa < x + width; xa++) {
				if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height) break;
				setPixel(xa, ya, col);
			}
		}
	}
	
	public void drawRect(int x, int y, int width, int height, int col, boolean fixed) {
		if (fixed) {
			x -= xOffset; 
			y -= yOffset;
		}
		for (int ya = y; ya < y + height; ya++) {
			if (ya == y || ya == y + height) {
				for (int xa = x; xa < x + width; xa++) {
					if (xa == x || xa == x + width) {
						if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height) break;
						setPixel(xa, ya, col);
					}
				}
			}
		}
	}
	
	public int getPixel(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) return pixels[x + y * width];
		else return 0x000000;
	}
	
	public void setPixel(int x, int y, int col) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			if (col != ALPHA_COL1 && col != ALPHA_COL2)
				pixels[x + y * width] = col;
		}
	}
	
	public void fixedPixel(int x, int y, int col) {
		x += xOffset;
		y += yOffset;
		if (x >= 0 && x < width && y >= 0 && y < height) {
			if (col != ALPHA_COL1 && col != ALPHA_COL2)
				pixels[x + y * width] = col;
		}
	}
}
