package net.heyitsultra.sandblock.Client.Graphics;

import java.util.ArrayList;

// import net.heyitsultra.sandblock.Client.Game;

public class AnimSprite {
	
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private int rate;
	
	public AnimSprite(Sprite[] sprites, int rate) {
		if (sprites != null)
			for (int i = 0; i < sprites.length; i++)
				this.sprites.add(sprites[i]);
		this.rate = rate;
	}
	
	public void add(Sprite sprite) {
		sprites.add(sprite);
	}
	
	public Sprite get(int time) {
		int size = sprites.size();
		Sprite sprite = sprites.get(0);
		for (int i = 0; i < size; i++) {
			if (time % (size * rate) > i * rate) sprite = sprites.get(i);
		}
		return sprite;
	}
	
}
