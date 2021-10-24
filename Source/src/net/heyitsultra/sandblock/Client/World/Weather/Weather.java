package net.heyitsultra.sandblock.Client.World.Weather;

import net.heyitsultra.sandblock.Client.Graphics.Screen;
import net.heyitsultra.sandblock.Client.Graphics.Sprite;
import net.heyitsultra.sandblock.Client.Util.Vector2i;

public class Weather {
	
	private Sprite particle;
	private Vector2i[] positions;
	private int rate;
	private int limit;
	private int time;
	
	public Weather(Sprite particle, int rate, int limit) {
		this.particle = particle;
		this.rate = rate;
		this.limit = limit;
	}

	// hilariously, these have no use. I just put them here so VScode would stop yelling at me that variables are unused. this whole class is unused! weather isn't implemented!
	public Sprite getParticle() {
		return particle;
	}
	public Vector2i[] getPositions() {
		return positions;
	}
	public int getLimit() {
		return limit;
	}
	
	public void update() {
		time++;
		if (time % (60 / rate) == 0) {
			
		}
	}
	
	public void render(Screen screen) {
		
	}
	
}
