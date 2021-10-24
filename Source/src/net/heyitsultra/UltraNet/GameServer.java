package net.heyitsultra.UltraNet;

import java.net.DatagramSocket;
import java.net.SocketException;

public class GameServer {
	
	private int port;
	private DatagramSocket socket;
	
	public GameServer(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public DatagramSocket getSocket() {
		return socket;
	}
	
}
