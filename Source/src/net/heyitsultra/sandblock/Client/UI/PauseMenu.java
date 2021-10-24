package net.heyitsultra.sandblock.Client.UI;

import net.heyitsultra.sandblock.Client.Game;
import net.heyitsultra.sandblock.Client.Graphics.Font;
import net.heyitsultra.sandblock.Client.Graphics.Screen;
import net.heyitsultra.sandblock.Client.Input.Mouse;
import net.heyitsultra.sandblock.Client.Util.Vector2i;

public class PauseMenu {
	
	Game main;
	
	public UI ui = new UI(144, 48, 16, 16, false, UI.Type.BOX, "mainmenu");
	public UI.uiText title = ui.new uiText(new Vector2i(0, 0), new Vector2i(51, 16), "PAUSED", 0xFFFFFF, Font.ultra, false);
	private String ver = "Version " + Game.version;
	public UI.uiText version = ui.new uiText(new Vector2i(0, 0), new Vector2i(66 - ver.length() * 3, 26), ver, 0x757575, Font.ultra, false);
	public UI.uiText resume = ui.new uiText(new Vector2i(0, 0), new Vector2i(51, 64), "Resume", 0xFFFFFF, Font.ultra, false);
	public UI.uiText settings = ui.new uiText(new Vector2i(0, 0), new Vector2i(45, 80), "Settings", 0xFFFFFF, Font.ultra, false);
	public UI.uiText quit = ui.new uiText(new Vector2i(0, 0), new Vector2i(30, 96), "Save and Quit", 0xFFFFFF, Font.ultra, false);
	
	public PauseMenu(Game main) {
		ui.addText(title);
		ui.addText(resume);
		ui.addText(settings);
		ui.addText(quit);
		
		this.main = main;
	}
	
	public void update(Mouse mouse) {
		if (resume.hover(mouse))
			resume.colour(0xEEA000);
		else
			resume.colour(0xFFFFFF);
		if (settings.hover(mouse))
			settings.colour(0xEEA000);
		else
			settings.colour(0xFFFFFF);
		if (quit.hover(mouse))
			quit.colour(0xEEA000);
		else
			quit.colour(0xFFFFFF);
		
		if (mouse.getButton() == 1) {
			if (resume.hover(mouse)) {
				main.paused = false;
			}
			if (settings.hover(mouse)) {
				
			}
			if (quit.hover(mouse)) {
				main.cooldown = 10;
				main.save(main.world);
				main.ingame = false;
			}
		}
		
		ui.update();
	}
	
	public void render(Screen screen) {
		ui.render(screen);
	}
	
}
