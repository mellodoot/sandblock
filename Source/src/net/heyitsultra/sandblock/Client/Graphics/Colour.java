package net.heyitsultra.sandblock.Client.Graphics;

public class Colour {
	
	public static int adjustBrightness(int col, int amount) {
		int r = (col & 0xff0000) >> 16;
		int g = (col & 0xff00) >> 8;
		int b = (col & 0xff);
		
		r += amount;
		g += amount;
		b += amount;
		
		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		if (b > 255) b = 255;
		
		return r << 16 | g << 8 | b;
	}
	
	public static int tint(int col, double red, double green, double blue) {
		int r = getRed(col);
		int g = getGreen(col);
		int b = getBlue(col);
		
		r += red;
		g += green;
		b += blue;
		
		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		if (b > 255) b = 255;
		
		return r << 16 | g << 8 | b;
	}
	
	public static int tint(int col, int overcol) {
		int r = getRed(overcol);
		int g = getGreen(overcol);
		int b = getBlue(overcol);
		
		return tint(col, r, g, b);
	}
	
	public static int getRed(int col) {
		return (col & 0xff0000) >> 16;
	}
	
	public static int getGreen(int col) {
		return (col & 0xff00) >> 8;
	}

	public static int getBlue(int col) {
		return (col & 0xff);
	}
	
}