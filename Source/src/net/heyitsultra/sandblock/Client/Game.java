package net.heyitsultra.sandblock.Client;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import org.gamejolt.*;
import org.gamejolt.Trophy.*;

import net.heyitsultra.UltraNet.serialization.*;
import net.heyitsultra.sandblock.Client.Client;
import net.heyitsultra.sandblock.Client.Entity.Mob.*;
import net.heyitsultra.sandblock.Client.Graphics.*;
import net.heyitsultra.sandblock.Client.Graphics.Font;
import net.heyitsultra.sandblock.Client.Input.*;
import net.heyitsultra.sandblock.Client.UI.*;
import net.heyitsultra.sandblock.Client.UI.Menu;
import net.heyitsultra.sandblock.Client.World.*;
import net.heyitsultra.sandblock.Server.*;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	// private String[] args;
	private String os;
	public static final float version = 1f;
	public boolean running = false;
	public boolean ui = true;
	public boolean paused;
	public boolean debug = false;
	public boolean ingame;
	public int actualWidth = 1280; //
	public int actualHeight = 700;
	public int scale = 3, //
					width = actualWidth / scale, //
					height = actualHeight / scale; //
	public int centW = width / 2 - scale, //
					centH = height / 2 - scale;
	public int fps, tps;
	public int frames, ticks;
	public int urlcooldown;
	public int cooldown;
	public int xScroll;
	public int yScroll;
	public String dir;
	
	private final int gameid = 210606;
	private final String gamekey = "don't got one son!!!";
	public GameJoltAPI api;
	public String username;
	public ArrayList<Trophy> trophies = new ArrayList<Trophy>();
	public ArrayList<Achievement> achieveUI = new ArrayList<Achievement>();
	
	private Client client;
	
	private Thread thread;
	private JFrame frame;
	private BufferStrategy bs;
	private Graphics g;
	public static Random random = new Random();
	
	public Screen screen;
	public World world;
	public Keyboard key;
	public Mouse mouse;
	public Font font;
	private ClientPlayer player;
	
	private MainMenu mainmenu;
	private Sprite backdrop = Tile.stone.sprite;
	private PauseMenu menu;
	private UI debugui;
	private UI media;
	
	private Menu pausemenu = new Menu(new String[]{"Singleplayer", "Multiplayer", "Settings", "Quit Game"}, "Paused", UI.Type.BOX);
	
	protected BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	protected int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game(String[] args) {
		os = System.getProperty("os.name");
		info("Running on: " + os);
		
		if (os.startsWith("Windows"))
			dir = System.getProperty("user.home") + "\\Documents\\SandBlock\\";
		else if (os.startsWith("Mac") || os.startsWith("Linux"))
			dir = System.getProperty("user.home") + "/Documents/SandBlock/";
		else
			dir = null;
		File dir = new File(this.dir);
		if (!dir.exists()) {
			dir.mkdirs();
			dir = new File(this.dir + "\\worlds");
			dir.mkdirs();
		}
		
		info("Loading Configuration File...");
		loadConfig();
		saveConfig();
		
		info("Preparing Important Classes...");
		screen = new Screen(width, height, pixels);
		mouse = new Mouse();
		key = new Keyboard();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addMouseWheelListener(mouse);
		addKeyListener(key);
		font = new Font();
		
		// Generating Debug UI
		debugui = new UI(1, 18, 1, 1, false, UI.Type.TEXT, "debug");
		debugui.fit("123456789012345", 11);
		debugui.addText(3, 1, "", 0xFFFFFF, Font.ultra);
		
		// Generating Menus
		mainmenu = new MainMenu(this);
		menu = new PauseMenu(this);
		
		// Media Icons for Menu
		media = new UI(1, height - 43, 1, 1, false, UI.Type.INVIS, "media");
		for (int i = 0; i <= 2; i++)
			media.addSprite(i * 34, 0, Sprite.media[i], false, false);
		
		newWorld("World1", 2048, 2048);
		
		info("Preparing Window...");
		frame = new JFrame();
		Dimension size = new Dimension(actualWidth, actualHeight);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/icons/sandblock.png")));
		frame.setPreferredSize(size);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("SandBlock");
		
		running = true;
		thread = new Thread(this, "SandBlock");
		thread.start();
	}
	
	public void update() {
		ticks++;
		cooldown--;
		urlcooldown--;
		key.update();
		mouse.update();
		
		ingame = true;
		
		if (ingame) {
			world.update();
			world.paused = paused;
			if (key.minus) world.time--;
			if (key.plus) world.time++;

			if (!world.loading) {
				if (player == null) player = world.getClientPlayer();
				if (api != null) player.name = username;
				if (client != null && ticks % 10 == 0) client.send(player.getData());
				
				if (key.esc && cooldown <= 0) {
					paused = !paused;
					for (int i = 0; i < achieveUI.size(); i++)
						achieveUI.remove(i);
						cooldown = 10;
				}
				if (key.F1 && cooldown <= 0) {
					ui = !ui;
					cooldown = 10;
				}
				if (paused) menu.update(mouse);
			}

			player.update(key.w, key.s, key.a, key.d, key.space, key.ctrl, key.shift, mouse.getX() - width / 2, mouse.getY() - height / 2, mouse.getButton() == 3);
		} else {
			mainmenu.update(mouse);
		}
		
		media.visible = !ingame;
		
		if (key.F3 && cooldown <= 0) {
			debug = !debug;
			cooldown = 10;
		}
		
		if (!ingame && mainmenu.gamejolt.hover(mouse) && mouse.getButton() == 1 && cooldown <= 0) {
			if (api == null || !api.isVerified()) {
				new Login(this);
				cooldown = 10;				
			}
		}
		
		if (media.visible == true && mouse.getButton() == 1) {
			if (media.sprites.get(0).hover(mouse))
				open("https://www.youtube.com/mellodoot");
			if (media.sprites.get(1).hover(mouse))
				open("https://www.twitter.com/mellodoot");
			if (media.sprites.get(2).hover(mouse))
				open("https://github.com/mellodoot/ultracubed");
		}
		
		for (int i = 0; i < achieveUI.size(); i++) {
			achieveUI.get(i).update();
			if (achieveUI.get(i).removed)
				achieveUI.remove(i);
		}
		
		frame.setTitle("Sandblock");		
	}
	
	public void render() {
		frames++;
		/*
		 * Mandatory when drawing to screen
		 */
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(1);
			return;
		}
		
		/*
		 * Rendering the World/Menu
		 */
		
		double plx = 0, ply = 0;
		if (ingame) {
			if (player != null) {
				plx = player.x;
				ply = player.y;
			} else {
				player = world.getClientPlayer();
			}
			
			xScroll = (int) plx - screen.width / 2;
			yScroll = (int) ply - screen.height / 2 - 8;
			
			if (world.loading) {
				int offset = (ticks / 2 % backdrop.width);
				screen.renderTiled(backdrop, offset, offset);
				int total = world.tiles.length;
				int complete = world.loaded + 1;
				font.render(centW - 58, centH - 12, "Loading... " + (complete / (total / 100)) + "%", screen, false);
				font.render(8, height - 24, complete + "/" + total, screen, false);
			} else {
				world.render(xScroll, yScroll, screen, ui);
			}
			
			if (ui) font.render(1, 1, "SandBlock " + version, screen, false);
			if (paused) menu.render(screen);
		} else {
			int offset = (ticks / 2 % backdrop.width);
			screen.renderTiled(backdrop, offset, offset);
			//if (!dialog.active)
			media.render(screen);
			//mainmenu.render(screen);
			pausemenu.render(screen);
		}
		
		/*
		 * Various UI elements
		 */
		//dialog.render(screen);
		
		for (int i = 0; i < achieveUI.size(); i++) {
			if (ui) achieveUI.get(i).render(screen);
		}
		
		if (ui && debug) {
			debugui.visible = true;
			debugui.render(screen);
		}
		
		if (debug) {
			if (ingame) {
				int ent = world.entities.size() + world.players.size();
				debugui.text.get(0).replace("FPS: " + fps + "\n" + //
						"TPS: " + tps + "\n" + //
						"POS: " + ((int) plx >> 4) + "," + ((int) ply >> 4) + "\n" + //
						"MPO: " + mouse.getX() + "," + mouse.getY() + "\n" + //
						"ENT: " + ent + "\n" + //
						"UIO: " + world.uiobj.size() + "\n" + //
						"TCK: " + ticks + "\n" + //
						"LGT: " + world.time + "\n" + //
						"WHL: " + mouse.getWheelRotation(), 0xFFFFFF, Font.ultra);
			} else {
				debugui.text.get(0).replace("FPS: " + fps + "\n" + //
						"TPS: " + tps + "\n" + //
						"MPO: " + mouse.getX() + "," + mouse.getY() + "\n" + //
						"TCK: " + ticks + "\n" + //
						"WHL: " + mouse.getWheelRotation(), 0xFFFFFF, Font.ultra);
			}
			debugui.update();
		}
		
		/*
		 * Rendering Complete, draw to screen.
		 */
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, actualWidth, actualHeight, null);
		g.dispose();
		bs.show();
		screen.clear();
	}
	
	/*
	 * Creating a new world
	 */
	public void newWorld(String name, int width, int height) {
		newWorld(new World(name, width, height));
	}
	
	public void newWorld(World world) {
		this.world = world;
		String user = null;
		if (api != null)
			user = username;
		player = world.spawnClientPlayer(world.spawnX, world.spawnY, 100, SpriteSheet.ultra, user);
		player.init(world, this);
		ingame = true;
	}
	
	/*
	 * Loading / Saving worlds
	 */
	
	public void save(World world) {
		UNDatabase db = new UNDatabase(world.name);
		UNObject tilesObj = new UNObject("Tiles");
		UNObject worldObj = new UNObject("World");
		
		UNField width = UNField.Integer("Width", world.width);
		UNField height = UNField.Integer("Height", world.height);
		UNField spawnX = UNField.Integer("Width", world.spawnX);
		UNField spawnY = UNField.Integer("Height", world.spawnY);
		
		int[] tiles = new int[this.width * this.height];
		for (int i = 0; i < tiles.length; i++)
			tiles[i] = world.tiles[i].id;
		int[] data = new int[tiles.length];
		for (int i = 0; i < data.length; i++)
			data[i] = world.tiles[i].data;
		
		UNArray tileArray = UNArray.Integer("ID", tiles);
		UNArray dataArray = UNArray.Integer("Data", data);
		UNField time = UNField.Integer("Time", world.time);
		
		tilesObj.addField(width);
		tilesObj.addField(height);
		tilesObj.addField(spawnX);
		tilesObj.addField(spawnY);
		tilesObj.addArray(tileArray);
		tilesObj.addArray(dataArray);
		worldObj.addField(time);
		db.addObject(tilesObj);
		db.addObject(worldObj);
		
		db.serializeToFile(dir + "worlds/" + world.name + ".bin");
	}
	
	public World load(String name) {
		String dir = "worlds/" + name + ".bin";
		UNDatabase db = UNDatabase.deserializeFromFile(this.dir + dir);
		if (db == null) return null;
		UNObject tilesObj = db.findObject("Tiles");
		UNObject worldObj = db.findObject("World");
		int width = tilesObj.findField("Width").getInt();
		int height = tilesObj.findField("Height").getInt();
		int spawnX = tilesObj.findField("SpawnX").getInt();
		int spawnY = tilesObj.findField("SpawnY").getInt();
		int time = worldObj.findField("Time").getInt();
		
		Tile[] tiles = new Tile[width * height];
		for (int i = 0; i < tiles.length; i++) {
			int id = tilesObj.findArray("ID").getIntArray()[i];
			int data = tilesObj.findArray("Data").getIntArray()[i];
			tiles[i] = new Tile(Tile.getTile(id), data);			
		}
		
		return new World(db.getName(), width, height, time, spawnX, spawnY, tiles, null);
	}
	
	/*
	 * Opening web URLs
	 */
	public void open(String url) {
		if (Desktop.isDesktopSupported() && urlcooldown <= 0) {
			try {
				Desktop.getDesktop().browse(new URI(url));
				urlcooldown = 25;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Creating a GameJolt API.
	 */
	public void newApi(String user, String token) {
		api = new GameJoltAPI(gameid, gamekey);
		if (api.verifyUser(user, token)) {
			username = user;
			trophies = api.getTrophies(Achieved.TRUE);
			if (api.isVerified()) {
				info("User " + user + " registered!");
				Trophy trophy = api.getTrophy(69914);
				achieveUI.add(new Achievement(this, trophy));
				UNDatabase db = new UNDatabase("Connect");
				UNObject obj = new UNObject("Data");
				UNField version = UNField.Float("Version", Game.version);
				// UNString name = UNString.Create("Name", "null");
				obj.addField(version);
				db.addObject(obj);
				client = new Client("localhost", 2248);
				client.send(db);
			}
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double nano = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int ticks = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nano;
			lastTime = now;
			while (delta >= 1) {
				update();
				ticks++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames;
				tps = ticks;
				ticks = 0;
				frames = 0;
			}
		}
		if (!running) stop();
	}
	
	public void saveConfig() {
		UNDatabase db = new UNDatabase("Config");
		UNObject obj = new UNObject("Screen");
		obj.addField(UNField.Integer("width", actualWidth));
		obj.addField(UNField.Integer("height", actualHeight));
		obj.addField(UNField.Integer("scale", scale));
		db.addObject(obj);
		
		db.serializeToFile(this.dir + "config.bin");
	}
	
	public void loadConfig() {
		try {
			UNDatabase db = UNDatabase.deserializeFromFile(this.dir + "config.bin");
			UNObject obj = db.findObject("Screen");
			actualWidth = obj.findField("width").getInt();
			actualHeight = obj.findField("height").getInt();
			scale = obj.findField("scale").getInt();
			width = actualWidth / scale;
			height = actualHeight / scale;
			info("Config Loaded:");
			info("	Width: " + actualWidth + "px");
			info("	Height: " + actualHeight + "px");
			info("	Scale: " + scale);
		} catch (NullPointerException e) {
			e.printStackTrace();
			actualWidth = 1280;
			actualHeight = 700;
			scale = 3;
			width = actualWidth / scale;
			height = actualHeight / scale;
			saveConfig();
		}
	}
	
	public void stop() {
		info("Game closed with final FPS: " + fps);
		frame.dispose();
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void resize(int w, int h, int s) {
		actualWidth = w;
		actualHeight = h; 
		scale = s;
		width = actualWidth / scale;
		height = actualHeight / scale;
		centW = width / 2 - scale;
		centH = height / 2 - scale;
		
		Dimension size = new Dimension(actualWidth, actualHeight);
		frame.setSize(size);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		screen = new Screen(width, height, pixels);
	}
	
	public static void chat(String string) {
		System.out.println("[CHAT]    " + string);
	}
	
	public static void info(String string) {
		System.out.println("[INFO]    " + string);
	}
	
	public static void infoPart(String string) {
		System.out.print("[INFO]    " + string);
	}
	
	public static void fatal(String string) {
		System.err.println("[FATAL]   " + string);
	}
	
	public static void main(String[] args) {
		if (args.length > 0 && args[0] == "server") {
			int port = 2248;
			if (args.length >= 2) port = Integer.decode(args[1]);
			new Server(port);
		}
		new Game(args);
	}

}
