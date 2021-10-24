package net.heyitsultra.sandblock.Client.UI;

import net.heyitsultra.sandblock.Client.Game;
import net.heyitsultra.sandblock.Client.Graphics.*;
import net.heyitsultra.sandblock.Client.Input.Mouse;
import net.heyitsultra.sandblock.Client.Util.Vector2i;

public class MainMenu {
	
	Game main;
	
	public UI ui = new UI(144, 48, 16, 16, false, UI.Type.BOX, "mainmenu");
	public UI.uiText title = ui.new uiText(new Vector2i(0, 0), new Vector2i(39, 16), "SandBlock", 0xFFFFFF, Font.ultra, false);
	private String ver = "Version " + Game.version;
	public UI.uiText version = ui.new uiText(new Vector2i(0, 0), new Vector2i(66 - ver.length() * 3, 26), ver, 0x757575, Font.ultra, false);
	public UI.uiText singleplayer = ui.new uiText(new Vector2i(0, 0), new Vector2i(32, 64), "Singleplayer", 0xFFFFFF, Font.ultra, false);
	public UI.uiText settings = ui.new uiText(new Vector2i(0, 0), new Vector2i(43, 80), "Settings", 0xFFFFFF, Font.ultra, false);
	public UI.uiText gamejolt = ui.new uiText(new Vector2i(0, 0), new Vector2i(27, 96), "GameJolt Login", 0xFFFFFF, Font.ultra, false);
	public UI.uiText achievements = ui.new uiText(new Vector2i(0, 0), new Vector2i(39, 96), "Achievements", 0xFFFFFF, Font.ultra, false);
	public UI.uiText quit = ui.new uiText(new Vector2i(0, 0), new Vector2i(40, 112), "Quit Game", 0xFFFFFF, Font.ultra, false);
	
	public MainMenu(Game main) {
		ui.addText(title);
		ui.addText(version);
		ui.addText(singleplayer);
		ui.addText(settings);
		ui.addText(gamejolt);
		ui.addText(quit);
		
		this.main = main;
	}
	
	public void update(Mouse mouse) {
		if (singleplayer.hover(mouse))
			singleplayer.colour(0xEEA000);
		else
			singleplayer.colour(0xFFFFFF);
		if (settings.hover(mouse))
			settings.colour(0xEEA000);
		else
			settings.colour(0xFFFFFF);
		if (gamejolt.hover(mouse))
			gamejolt.colour(0xEEA000);
		else
			gamejolt.colour(0xFFFFFF);
		if (main.api != null)
			if (main.api.isVerified())
				gamejolt.colour(0x757575);
		if (quit.hover(mouse))
			quit.colour(0xEEA000);
		else
			quit.colour(0xFFFFFF);
		
		if (main.cooldown <= 0) {
			if (mouse.getButton() == 1) {
				if (singleplayer.hover(mouse)) {
					if (main.world == null) main.newWorld("World1", 2048, 2048);
					else main.ingame = true;
				}
				if (settings.hover(mouse)) {
					
				}
				if (quit.hover(mouse)) {
					main.running = false;
				}
			}
		}
		
		ui.update();
	}
	
	public void render(Screen screen) {
		ui.render(screen);
	}
	
}
