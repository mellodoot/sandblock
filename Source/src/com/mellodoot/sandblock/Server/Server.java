package com.mellodoot.sandblock.Server;

import java.io.*;
import java.net.*;
// import java.util.*;

import com.mellodoot.sandblock.Serialization.*;
import com.mellodoot.sandblock.Client.*;

public class Server {
	
	// private int port;
	private Thread listenThread;
	private boolean listening = false;
	private DatagramSocket socket;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	// private Set<ServerClient> clients = new HashSet<ServerClient>();
	
	public Server(int port) {
		// this.port = port;
		
		Game.info("Starting server on port " + port + ".");
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		listening = true;
		listenThread = new Thread(() -> listen(), "SandBlock Server - listenThread");
		listenThread.start();
	}
	
	private void listen() {
		while (listening) {
			DatagramPacket packet = new DatagramPacket(receivedDataBuffer, MAX_PACKET_SIZE);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			process(packet);
		}
	}
	
	private void process(DatagramPacket packet) {
		byte[] data = packet.getData();
		
		if (new String(data, 0, 4).equals("UNDB")) {
			SerialDatabase db = SerialDatabase.deserialize(data);
			dump(db);
		}
	}
	
	public void send(byte[] data, InetAddress address, int port) {
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void dump(SerialDatabase db) {
		System.out.println("\t== Recieved Database ==");
		System.out.println("Name: " + db.getName());
		System.out.println("Size: " + db.getSize());
		System.out.println("Objects: " + db.objects.size());
		for (SerialObject obj : db.objects) {
			System.out.println("\tName: " + obj.getName());
			System.out.println("\tSize: " + obj.getSize());
			System.out.println("\tFields: " + obj.fields.size());
			for (SerialField field : obj.fields) {
				System.out.println("\t\tName: " + field.getName());
				System.out.println("\t\tSize: " + field.getSize());
				System.out.println("\t\tType: " + Type.getType(field.type));
				System.out.println("\t\tContent: " + field.toString());
			}
		}
	}
}
