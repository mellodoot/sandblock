package net.heyitsultra.sandblock.Client.Graphics;

public class Font {
	
	public static Sprite[] ultra = Sprite.split(new SpriteSheet("/font/ultra.png", 8));
	public static Sprite[] cubed = Sprite.split(new SpriteSheet("/font/cubed.png", 8));
	public static Sprite[] emulogic = Sprite.split(new SpriteSheet("/font/emulogic.png", 8));
	
	private static String charIndex = 
			"ABCDEFGHIJKLM" + //
			"NOPQRSTUVWXYZ" + //
			"abcdefghijklm" + //
			"nopqrstuvwxyz" + //
			"0123456789.,!" + //
			"?:;'()*¬@³-+&" + //
			"<>/%=^~#[]{}_" + //
			"             " + //
			"             " + //
			"             " + //
			"             " + //
			"             " + //
			"             ";

	public Font() {
	}

	public void render(int x, int y, String text, Screen screen, boolean fixed) {
		render(x, y, text, screen, 0xFFFFFF, 1, ultra, fixed);
	}
	
	public void render(int x, int y, String text, Screen screen, int color, int scale, Sprite[] font, boolean fixed) {
		int xOffset = 0;
		int line = 0;
		int spacing = -2;
		
		if (font == cubed) spacing = -1;
		if (font == emulogic) spacing = 0;
		
		if (text != null) {
			for (int i = 0; i < text.length(); i++) {
				int yOffset = 0;
				char currentChar = text.charAt(i);
				if (i > 0)
					xOffset += (8 + spacing) * scale;
				
				if (currentChar == '\r') color = 0xFFFFFF;
				if (currentChar == '\b') color = 0xFF0000;
				if (currentChar == '\t') color = 0x808080;
				
				// Character / Line Adjustments
				if (currentChar == '\n') {
					line++;
					xOffset = -(8 + spacing) * scale;
					continue;
				}
				
				if (currentChar == ' ') continue;
				
				if (currentChar == 'i') xOffset += 0 * scale;
				if (currentChar == 'g') yOffset += 2 * scale;
				if (currentChar == 'p' || currentChar == 'q' || currentChar == 'y' || currentChar == ',') yOffset += 1 * scale;
				if (currentChar == '\b' || currentChar == '\r' || currentChar == '\t') {
					xOffset -= (8 + spacing) * scale;
					continue;
				}
				
				int index = charIndex.indexOf(currentChar);
				if (index == -1) index = 168;
				if (currentChar == '<') index = 78;
				int xa = x + xOffset;
				int ya = y + yOffset + (line * (10 * scale));
				screen.renderLight(xa + 1, ya + 1, font[index], fixed, false, false, -25);
				screen.renderTextCharacter(xa, ya, font[index], color, fixed);
				
				if (currentChar == 'w' || currentChar == '%') xOffset += 2 * scale;
				if (currentChar == 'i') xOffset -= 1 * scale;
			}
		}
	}
	
}