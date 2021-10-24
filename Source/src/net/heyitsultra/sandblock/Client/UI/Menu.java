package net.heyitsultra.sandblock.Client.UI;

import net.heyitsultra.sandblock.Client.Graphics.*;
import net.heyitsultra.sandblock.Client.UI.UI.*;

public class Menu {
	
	private String[] content;
	private String title;
	private Type type;
	private UI ui;
	private Font font = new Font();
	
	public Menu(String[] content, String title, Type type) {
		this.content = content;
		this.title = title;
		this.type = type;
		
		// Using this.toString() as the UI name means that every UI entity is identified by reference to the Menu class.
		// It would be extremely unlikely for ui.search(String) to fail in this way, as the Menu class would have to exist (including the UI) to be used.
		ui = new UI(0, 0, 0, 0, false, type, this.toString());
		String longest = "";
		for (int i = 0; i < content.length; i++) {
			if (content[i].length() > longest.length())
				longest = content[i];
			ui.addText(4, 4 + 10 * i, content[i], 0xFFFFFF, Font.ultra);
		}
		ui.fit(longest, content.length);
	}
	
	/*
	 *  public Menu(String content, Type type) {
	 *  	this.content = new String[]{content};
	 *  	this.type = type;
	 *  }
	 */
	
	public void setContent(String[] content) {
		this.content = content;
	}
	public String[] getContent() {
		return this.content;
	}

	public void setType(Type type) {
		this.type = type;
	}
	public Type getType() {
		return this.type;
	}

	public void update() {
		ui.center();
		ui.update();
	}
	
	public void render(Screen screen) {
		ui.render(screen);
		int x = ui.x + (ui.width / 2) * ui.tilesize;
		font.render(x - title.length() * 5 / 2, ui.y - 4, title, screen, false);
	}
	
}
