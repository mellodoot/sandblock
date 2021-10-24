package net.heyitsultra.sandblock.Client.UI;

import java.util.ArrayList;

import org.gamejolt.Trophy;

import net.heyitsultra.sandblock.Client.Game;
import net.heyitsultra.sandblock.Client.Graphics.Font;
import net.heyitsultra.sandblock.Client.Graphics.Screen;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;

public class Achievement {
	
	public int x, y;
	Game main;
	public UI ui;
	public int time;
	public boolean removed;
	
	public Achievement(Game main, Trophy trophy) {
		this.main = main;
		
		time = 1000;
		ui = new UI(main.width - 124, 2, 29, 5, false, UI.Type.BUTTON, "Achievement");
		ui.visible = false;
		
		String diff = trophy.getProperty("difficulty");
		
		Sprite sprite = Sprite.trophies[0];
		if (diff.equals("Silver")) sprite = Sprite.trophies[1];
		if (diff.equals("Gold")) sprite = Sprite.trophies[2];
		ui.addSprite(4, 4, sprite, false, false);
		
		ui.addText(24, 3, "Achievement Get!", 0xFFFFFF, Font.ultra);
		
		int colour = 0xa58144;
		if (diff.equals("Silver")) colour = 0xf1f1f1;
		if (diff.equals("Gold")) colour = 0xf6e03d;
		ui.addText(24, 12, trophy.getTitle(), colour, Font.ultra);
		
		ArrayList<Trophy> trophies = main.trophies;
		if (!trophies.contains(trophy)) {
			achieve(trophy);
		}
	}
	
	public void achieve(Trophy trophy) {
		main.api.achieveTrophy(trophy);
		ui.visible = true;
	}
	
	public void update() {
		time--;
		if (time < 0) remove();
		ui.update();
	}
	
	public void render(Screen screen) {
		ui.visible = true;
		ui.render(screen);
	}
	
	public void remove() {
		removed = true;
	}
	
}
