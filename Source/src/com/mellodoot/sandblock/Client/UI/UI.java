package com.mellodoot.sandblock.Client.UI;

import java.util.ArrayList;
import java.util.List;

import com.mellodoot.sandblock.Client.Graphics.Font;
import com.mellodoot.sandblock.Client.Graphics.Screen;
import com.mellodoot.sandblock.Client.Graphics.Sprite;
import com.mellodoot.sandblock.Client.Input.Mouse;
import com.mellodoot.sandblock.Client.Util.Vector2i;

public class UI {
	
	public int x, y;
	public int width, height;
	public boolean fixed;
	public String name;
	public boolean removed = false;
	public int xdest, ydest;
	public boolean visible = true;

	public int gameWidth = 1280;
	public int gameHeight = 720;
	public int frames;
	
	public enum Type {
		BOX, BUTTON, TEXT, INVIS
	}
	public Type type;
	
	public List<uiText> text = new ArrayList<uiText>();
	public List<uiSprite> sprites = new ArrayList<uiSprite>();
	
	public int tilesize;
	public Sprite[] spritesheet = Sprite.uibox;

	public UI(int x, int y, int width, int height, boolean fixed, Type type, String name) {
		this.x = x;
		this.y = y;
		xdest = x;
		ydest = y;
		this.width = width;
		this.height = height;
		this.fixed = fixed;
		this.type = type;
		this.name = name;
		tilesize = 8;
		spritesheet = Sprite.uibox;
		
		if (type == Type.BUTTON) {
			tilesize = 4;
			spritesheet = Sprite.uibutton;
		}
		
		if (type == Type.TEXT) {
			tilesize = 2;
			spritesheet = Sprite.uitextbox;
		}
	}
	
	public void fit(String string, int lines) {
		int text = string.length() * 6;
		width = (text + 3) / tilesize;
		height = (8 * lines + 4) / tilesize;
	}
	
	public void animate(int x, int y) {
		xdest = x;
		ydest = y;
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
		xdest = x;
		ydest = y;
	}
	
	public void center() {
		this.x = gameWidth / 2 - width * tilesize / 2;
		this.y = gameHeight / 2 - height * tilesize / 2;
	}
	
	public void resize(int w, int h) {
		width = w;
		height = h;
	}
	
	public void type(Type type) {
		this.type = type;
		
		if (type == Type.BOX) {
			tilesize = 8;
			spritesheet = Sprite.uibox;
		}
		if (type == Type.BUTTON) {
			tilesize = 4;
			spritesheet = Sprite.uibutton;
		}
		if (type == Type.TEXT) {
			tilesize = 2;
			spritesheet = Sprite.uitextbox;
		}
	}
	
	public void update() {
		if (visible) {
			if (frames % 4 == 0) {				
				if (x != xdest) {
					if (x < xdest)
						x++;
					if (x > xdest)
						x--;
				}
				if (y != ydest) {
					if (y < ydest)
						y++;
					if (y > ydest)
						y--;
				}
			}
			
			for (int i = 0; i < this.sprites.size(); i++) {
				this.sprites.get(i).repos(x, y);
				this.sprites.get(i).update();
			}
			for (int i = 0; i < text.size(); i++) {
				text.get(i).repos(x, y);
				text.get(i).update();
			}
		}
	}
	
	public void render(Screen screen) {
		if (visible) {
			if (type != Type.INVIS) {
				for (int xa = 0; xa <= width; xa++) {
					for (int ya = 0; ya <= height; ya++) {
						Sprite sprite = spritesheet[0];
						boolean xflip = false;
						boolean yflip = false;
						if (xa == 0) {
							if (ya > 0) {
								sprite = spritesheet[2];
								if (ya == height) {
									sprite = spritesheet[0];
									yflip = true;
								}
							}	
						}
						if (xa > 0) {
							sprite = spritesheet[1];
							if (ya > 0) {
								sprite = spritesheet[3];
								if (ya == height) {
									sprite = spritesheet[1];
									yflip = true;
								}
							}	
						}
						if (xa == width) {
							sprite = spritesheet[0];
							xflip = true;
							if (ya > 0) {
								sprite = spritesheet[2];
								if (ya == height) {
									sprite = spritesheet[0];
									yflip = true;
								}
							}	
						}
						int xb = x + (xa * tilesize);
						int yb = y + (ya * tilesize);
						screen.renderLight(xb + 1, yb + 1, sprite, fixed, xflip, yflip, -25);
						screen.renderSprite(xb, yb, sprite, 1, xflip, yflip, fixed);
					}
				}
			}
			
			for (int i = 0; i < this.sprites.size(); i++)
				this.sprites.get(i).render(screen);
			for (int i = 0; i < text.size(); i++)
				text.get(i).render(screen);
		}
		frames++;
	}
	
	public class uiText {
		
		Vector2i center;
		Vector2i pos;
		int x, y;
		String text;
		int colour;
		boolean fixed;
		Sprite[] sheet;
		
		Font font = new Font();
		
		public uiText(Vector2i center, Vector2i pos, String text, int color, Sprite[] sheet, boolean fixed) {
			this.center = center;
			this.pos = pos;
			this.text = text;
			this.colour = color;
			this.fixed = fixed;
			this.sheet = sheet;
		}
		
		public void update() {
			x = center.getX() + pos.getX();
			y = center.getY() + pos.getY();
		}
		
		public void render(Screen screen) {
			font.render(x, y, text, screen, colour, 1, sheet, false);
		}
		
		public void repos(int x, int y) {
			center.set(x, y);
		}
		
		public void move(int x, int y) {
			pos.set(x, y);
		}
		
		public void replace(String string, int color, Sprite[] sheet) {
			this.text = string;
			this.colour = color;
			this.sheet = sheet;
		}
		
		public void text(String text) {
			this.text = text;
		}
		
		public void colour(int colour) {
			this.colour = colour;
		}
		
		public void font(Sprite[] sheet) {
			this.sheet = sheet;
		}
		
		public boolean hover(Mouse mouse) {
			if (mouse.within(center.getX() + pos.getX(), center.getY() + pos.getY(), text.length() * 6, 8))
				return true;
			else
				return false;
		}
		
	}
	
	public void addText(int x, int y, String text, int color, Sprite[] sheet) {
		this.text.add(new uiText(new Vector2i(this.x, this.y), new Vector2i(x, y), text, color, sheet, fixed));
	}
	
	public void addText(uiText obj) {
		text.add(obj);
	}
	
	public class uiSprite {
		
		Vector2i center;
		Vector2i pos;
		int x, y;
		Sprite sprite;
		boolean xflip, yflip;
		boolean fixed;
		
		public uiSprite(Vector2i center, Vector2i pos, Sprite sprite, boolean xflip, boolean yflip, boolean fixed) {
			this.center = center;
			this.pos = pos;
			this.sprite = sprite;
			this.xflip = xflip;
			this.yflip = yflip;
			this.fixed = fixed;
			x = center.getX() + pos.getX();
			y = center.getY() + pos.getY();
		}
		
		public void update() {
			x = center.getX() + pos.getX();
			y = center.getY() + pos.getY();
		}
		
		public void render(Screen screen) {
			screen.renderLight(x + 1, y + 1, sprite, false, xflip, yflip, -25);
			screen.renderSprite(x, y, sprite, 1, xflip, yflip, false);
			// if (hover(Game.mouse))
			// 	screen.renderLight(x, y, sprite, false, xflip, yflip, 25);
		}
		
		public void repos(int x, int y) {
			center.set(x, y);
		}
		
		public void move(int x, int y) {
			pos.set(x, y);
		}
		
		public void replace(Sprite sprite) {
			this.sprite = sprite;
		}
		
		public boolean hover(Mouse mouse) {
			if (mouse.within(center.getX() + pos.getX(), center.getY() + pos.getY(), sprite.width, sprite.height))
				return true;
			else
				return false;
		}
		
	}
	
	public void addSprite(int x, int y, Sprite sprite, boolean xflip, boolean yflip) {
		this.sprites.add(new uiSprite(new Vector2i(this.x, this.y), new Vector2i(x, y), sprite, xflip, yflip, fixed));
	}
	
	public void addSprite(uiSprite obj) {
		sprites.add(obj);
	}
	
	public uiText searchString(String string) {
		uiText result = null;
		for (int i = 0; i < text.size(); i++) {
			if (text.get(i).text == string) {
				result = text.get(i);
				break;
			}
		}
		return result;
	}
	
	public uiText searchString(int i) {
		return text.get(i);
	}
	
	public uiSprite searchSprite(Sprite sprite) {
		uiSprite result = null;
		for (int i = 0; i < text.size(); i++) {
			if (sprites.get(i).sprite == sprite) {
				result = sprites.get(i);
				break;
			}
		}
		return result;
	}
	
	public uiSprite searchSprite(int i) {
		return sprites.get(i);
	}
	
	public void remove() {
		removed = true;
	}
	
}
