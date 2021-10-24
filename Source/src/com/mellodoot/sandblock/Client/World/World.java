package com.mellodoot.sandblock.Client.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mellodoot.sandblock.Serialization.SerialArray;
import com.mellodoot.sandblock.Serialization.SerialDatabase;
import com.mellodoot.sandblock.Serialization.SerialObject;
import com.mellodoot.sandblock.Client.Game;
import com.mellodoot.sandblock.Client.Entity.Entity;
import com.mellodoot.sandblock.Client.Entity.Spawner;
import com.mellodoot.sandblock.Client.Entity.Mob.ClientPlayer;
import com.mellodoot.sandblock.Client.Entity.Mob.Player;
import com.mellodoot.sandblock.Client.Entity.Particle.Particle;
import com.mellodoot.sandblock.Client.Entity.Projectile.Projectile;
import com.mellodoot.sandblock.Client.Graphics.Screen;
import com.mellodoot.sandblock.Client.Graphics.SpriteSheet;
import com.mellodoot.sandblock.Client.UI.UI;
import com.mellodoot.sandblock.Client.Util.Vector2i;

public class World {
	
	public String name;
	public int width, height;
	public int spawnX, spawnY;
	public int time;
	public int ticks;
	public boolean day;
	public int loaded;
	public boolean loading;
	public boolean paused;

	public Tile[] tiles;
	public Tile[] postTiles;
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Player> players = new ArrayList<Player>();
	public List<Particle> particles = new ArrayList<Particle>();
	public List<Projectile> projectiles = new ArrayList<Projectile>();
	public List<UI> uiobj = new ArrayList<UI>();
	
	private Comparator<Node> nodeSorter = new Comparator<Node> () {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return 1;
			else return -1;
		}
	};
	
	public World(String name, int width, int height, int time, int spawnX, int spawnY, Tile[] tiles, List<Entity> entities) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.time = time;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		this.tiles = tiles;
		if (entities != null)
			for (int i = 0; i < entities.size(); i++)
				spawn(entities.get(i));
	}
	
	public World(String n, int w, int h) {
		Game.info("Now generating world [" + n + "] with size [" + w + "x" + h + "].");
		name = n;
		width = w;
		height = h;
		spawnX = w / 2;
		spawnY = h / 2;
		tiles = new Tile[w * h];
		postTiles = new Tile[w * h];
		loading = true;
		setTile(spawnX, spawnY, Tile.spawn);
	}
	
	public void update() {
		/*
		 * World generation, handled only when world size does not meet capacity.
		 */
		if (loaded < tiles.length) {
			int rate = tiles.length >> 4;
			for (int i = 0; i < rate; i++) {
				tiles[loaded + i] = new Tile(Tile.grass, 0);
				if (Game.random.nextInt(50) == 0) tiles[loaded + i] = new Tile(Tile.tree, 0);
				postTiles[loaded + i] = new Tile(Tile.nulltile, 0);
			}
			loaded += rate;
		} else {
			loading = false;
			if (!paused) {
				// Day/Night cycle
				if (ticks % 100 == 0) {
					if (day) {
						time--;
					} else {
						time++;
					}
				}
				
				// Time control, must move elsewhere once admin commands are implemented.
				
				
				if (time <= -40) day = false;
				if (time >= 0) day = true;
				
				for (int i = 0; i < entities.size(); i++) {
					if (entities.get(i) instanceof ClientPlayer) continue;
					entities.get(i).update();
				}
			}
			for (int i = 0; i < uiobj.size(); i++)
				uiobj.get(i).update();
			
			cleanup();
		}
		ticks++;
	}
	
	public void render(int xScroll, int yScroll, Screen screen, boolean uiEnabled) {
		screen.setOffset(xScroll, yScroll);
		
		int x0 = xScroll - (16 * 5) >> 4;
		int x1 = (xScroll + screen.width + (16 * 5)) >> 4;
		int y0 = yScroll - (16 * 5) >> 4;
		int y1 = (yScroll + screen.height + (16 * 8)) >> 4;
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen, this);
			}
		}
		
		for (int y = 0; y < height; y++) {
			for (int i = 0; i < entities.size(); i++) {
				int ya = (int) entities.get(i).getY();
				if (ya >> 4 == y && !(entities.get(i) instanceof Spawner))
					entities.get(i).render(screen);
			}
		}
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				if (getPost(x, y).id != Tile.nulltile.id) {
					getPost(x, y).render(x, y, screen, this);
					setPost(x, y, Tile.nulltile);
				}
			}
		}
		
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				screen.renderLight(x << 4, y << 4, Tile.nulltile.sprite, true, false, false, time);
			}
		}
		
		if (uiEnabled) {
			for (int i = 0; i < uiobj.size(); i++)
				continue; // if (!Game.paused || uiobj.get(i).name == "menu") uiobj.get(i).render(screen);
			for (int i = 0; i < entities.size(); i++)
				entities.get(i).postRender(screen);
		}
		
		// if (Game.paused) {
		// 	for (int y = y0; y < y1; y++) {
		// 		for (int x = x0; x < x1; x++) {
		// 			screen.renderLight(x << 4, y << 4, Tile.nulltile.sprite, true, false, false, -25);
		// 		}
		// 	}
		// }
	}
	
	public void save() {
		SerialDatabase db = new SerialDatabase("World");
		
		SerialObject obj = new SerialObject("Tiles");
		int[] tiles = new int[width * height];
		for (int i = 0; i < tiles.length; i++)
			tiles[i] = this.tiles[i].id;
		SerialArray tileArray = SerialArray.Integer("ID", tiles);
		int[] data = new int[tiles.length];
		for (int i = 0; i < data.length; i++)
			data[i] = this.tiles[i].data;
		SerialArray dataArray = SerialArray.Integer("Data", data);
		obj.addArray(tileArray);
		obj.addArray(dataArray);
		
		db.addObject(obj);
	}
	
	public ClientPlayer spawnClientPlayer(int x, int y, int health, SpriteSheet sheet, String name) {
		if (name == null) name = "Player" + players.size() + 1;
		ClientPlayer player = new ClientPlayer(x, y, health, sheet, name);
		spawn(player);
		return player;
	}
	
	public ClientPlayer getClientPlayer() {
		return (ClientPlayer) getPlayer(0);
	}
	
	public void spawnPlayer(int x, int y, int health, SpriteSheet sheet, String name) {
		if (name == null) name = "Player" + players.size() + 1;
		//add(new Player(x, y, health, sheet, name));
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Player> getPlayers(Entity e, int radius) {
		List<Entity> entities = getEntitiesRelativeToEntity(e, radius);
		List<Player> result = new ArrayList<Player>();
		for (Entity entity : entities) {
			if (entity instanceof Player)
				result.add((Player) entity);
		}
		return result;
	}
	
	public Player getPlayer(int id) {
		if (id < players.size())
			return players.get(id);
		else
			return null;
	}
	
	public Player getPlayer(String name) {
		Player player = null;
		for (int i = 0; i < players.size(); i++) {
			if (getPlayer(i).getName().equals(name))
				player = getPlayer(i);
		}
		return player;
	}
	
	public List<Node> findPath(Vector2i start, Vector2i goal) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, Vector2i.getDistance(start, goal));
		Tile tile = getTile(goal.getX(), goal.getY());
		if (tile.solid || tile.wall) return null;
		openList.add(current);
		while (openList.size() > 0) {
			if (openList.size() > 100) return null;
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.pos.equals(goal)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				int x = current.pos.getX();
				int y = current.pos.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null) continue;
				if (at.solid || at.wall) continue;
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost + Vector2i.getDistance(current.pos, a);
				double hCost = Vector2i.getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= node.gCost) continue;
				if (!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}
	
	private boolean vecInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.pos.equals(vector)) return true;
		}
		return false;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public List<Entity> getEntitiesRelativeToEntity(Entity e, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		double ex = e.getX();
		double ey = e.getY();
		for (Entity entity : entities) {
			double x = entity.getX();
			double y = entity.getY();
			
			double dx = Math.abs(x - ex);
			double dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			
			if (distance <= radius && entity != e)
				result.add(entity);
		}
		return result;
	}
	
	public void spawn(Entity e) {
		e.init(this);
		if (e instanceof ClientPlayer)
			players.add((ClientPlayer) e);
		if (e instanceof Particle)
			particles.add((Particle) e);
		if (e instanceof Projectile)
			projectiles.add((Projectile) e);
		entities.add(e);
	}
	
	public void add(UI ui) {
		uiobj.add(ui);
	}
	
	public UI search(String name) {
		UI result = null;
		for (int i = 0; i < uiobj.size(); i++) {
			if (uiobj.get(i).name == name) {
				result = uiobj.get(i);
				break;
			}
		}
		return result;
	}
	
	public void cleanup() {
		for (int i = 0; i < entities.size(); i++)
			if (entities.get(i).removed) entities.remove(i);
		for (int i = 0; i < players.size(); i++)
			if (players.get(i).removed) players.remove(i);
		for (int i = 0; i < particles.size(); i++)
			if (particles.get(i).removed) particles.remove(i);
		for (int i = 0; i < projectiles.size(); i++)
			if (projectiles.get(i).removed) projectiles.remove(i);
		for (int i = 0; i < uiobj.size(); i++)
			if (uiobj.get(i).removed) uiobj.remove(i);
	}
	
	public void setTile(int x, int y, Tile tile) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			if (getTile(x, y) != Tile.spawn)
				tiles[x + y * width] = new Tile(tile, 0);
		}
	}
	
	public Tile getTile(int x, int y) {
		Tile tile = new Tile(Tile.nulltile, 0);
		if (x >= 0 && x < width && y >= 0 && y < height) tile = tiles[x + y * width];
		else tile = new Tile(Tile.water, 0);
		return tile;
	}
	
	public void setPost(int x, int y, Tile tile) {
		if (x >= 0 && x < width && y >= 0 && y < height)
			postTiles[x + y * width] = new Tile(tile, 0);
	}
	
	public Tile getPost(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) 
			return postTiles[x + y * width];
		
		return null;
	}
	
}
