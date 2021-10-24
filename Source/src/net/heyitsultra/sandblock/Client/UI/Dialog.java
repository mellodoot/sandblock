package net.heyitsultra.sandblock.Client.UI;

import net.heyitsultra.sandblock.Client.Graphics.Font;
import net.heyitsultra.sandblock.Client.Graphics.Screen;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;
import net.heyitsultra.sandblock.Client.Graphics.SpriteSheet;
import net.heyitsultra.sandblock.Client.UI.UI.uiText;
import net.heyitsultra.sandblock.Client.Util.Vector2i;

public class Dialog {
	
	public static SpriteSheet ultra = new SpriteSheet("/dialog/ultra.png", 64);
	public static SpriteSheet datboi = new SpriteSheet("/dialog/datboi.png", 64);
	public SpriteSheet sheet;
	public Sprite normal = new Sprite(0, 0, sheet);
	public Sprite uninterested = new Sprite(1, 0, sheet);
	
	private UI ui = new UI(10, 10, 25, 6, false, UI.Type.BUTTON, "Dialog");
	private String[] dialog;
	private String author;
	private uiText textobj = ui.new uiText(new Vector2i(), new Vector2i(), "", 0xFFFFFF, Font.ultra, false);
	private uiText authorobj = ui.new uiText(new Vector2i(), new Vector2i(), "", 0xFFFF00, Font.ultra, false);
	private int time;
	private int len;
	private int current;
	private int cooldown;
	public boolean active;
	
	public enum Dir {
		Left, Right
	};
	public Dir dir;
	
	public Dialog(String[] text, String author, SpriteSheet sheet, Dir dir) {
		for (int i = 0; i < text.length; i++) {
			text[i] = process(text[i]);
		}
		this.dialog = text;
		this.author = author;
		this.dir = dir;
		this.sheet = sheet;
		if (sheet != null) {
			normal.set(0, 0, sheet);
			uninterested.set(1, 0, sheet);
		}
		ui.addText(textobj);
		ui.addText(authorobj);
		reset();
	}
	
	public void update(boolean proceedKey) {
		if (active) {
			if (cooldown > 0) cooldown--;
			
			String text = dialog[current];
			if (time > 0)
				time++;
			if (time % 60 == 0 && len < text.length()) {
				len++;
			}
			
			textobj.text(text.substring(0, len));
			textobj.move(15, 10);
			authorobj.text(author);
			authorobj.move(20, -4);
			ui.move(10, 150);
			ui.resize(100, 15);
			ui.update();
			
			if (proceedKey && cooldown <= 0) {
				if (len < text.length()) len = text.length() - 1;
				else next();
				cooldown = 10;
			}
		}
	}
	
	public void render(Screen screen) {
		if (active) {
			String text = dialog[current];
			int x = 15;
			boolean xflip = false;
			if (dir == Dir.Right) {
				x = screen.width - 15 - 128;
				xflip = true;
			}
			
			
			if (sheet != null) {
				Sprite sprite = normal;
				screen.renderSprite(x, 11, sprite, 2, xflip, false, false);
			}
			ui.render(screen);
			Font font = new Font();
			if (len >= text.length()) font.render(250, 209, "[Press SPACE to Continue]", screen, 0xFFFFFF, 1, Font.ultra, false);
		}
	}
	
	public String process(String text) {
		if (text.length() >= 62) {
			String src = text;
			String result = "";
			int lines = 0;
			int length = src.length();
			for (int i = 0; i < length; i++) {
				if (i % 62 >= 56 && i > lines * 62) {
					String alt = "";
					for (int o = 0; o < 26; o++) {
						alt = result.substring(result.length() - o);
						if (alt.startsWith(" ")) {
							result = result.substring(0, result.length() - o);
							i++;
							break;
						}
						i--;
					}
					result += "\n";
					lines++;
				}
				result += src.charAt(i);
			}
			text = result;
		}
		return text;
	}
	
	public void next() {
		if (current < dialog.length) {
			current++;
			len = -1;
		}
		if (current >= dialog.length) active = false;
	}
	
	public void reset() {
		active = true;
		len = -1;
		current = 0;
	}
	
}
