package net.heyitsultra.sandblock.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import net.heyitsultra.UltraNet.serialization.*;
import net.heyitsultra.sandblock.Client.Util.*;

public class Client {
	
	private final static byte[] PACKET_HEADER = new byte[] { 0x40, 0x40 };
	private final static byte PACKETTYPE_CONNECT = 0x01;
	
	public enum Error {
		UNKNOWN, INVALID_HOST, TIMED_OUT, SOCKET_EXCEPTION
	}
	public Error error;
	
	private String ipAddress;
	private int port;
	private InetAddress serverAddress;
	private DatagramSocket socket;
	
	private Random random = new Random();
	
	/*
	 * Format: 127.0.0.1:2248
	 */
	public Client(String host) {
		String[] params = host.split(":");
		if (params.length != 2) {
			error = Error.INVALID_HOST;
			return;
		}
		this.ipAddress = params[0];
		try {
			this.port = Integer.parseInt(params[1]);			
		} catch (NumberFormatException e) {
			error = Error.INVALID_HOST;
			return;
		}
	}
	
	public Client(String host, int port) {
		ipAddress = host;
		this.port = port;
	}
	
	public boolean connect() {
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			error = Error.INVALID_HOST;
			return false;
		}
		
		try {
			socket = new DatagramSocket(random.nextInt(25565));
		} catch (SocketException e) {
			e.printStackTrace();
			error = Error.SOCKET_EXCEPTION;
			return false;
		}
		
		sendConnectionPacket();
		return true;
	}
	
	private void sendConnectionPacket() {
		BinaryWriter writer = new BinaryWriter();
		writer.write(PACKET_HEADER);
		writer.write(PACKETTYPE_CONNECT);
		send(writer.getBuffer());
	}
	
	public void send(byte[] data) {
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(UNDatabase db) {
		byte[] data = new byte[db.getSize()];
		db.getBytes(data, 0);
		send(data);
	}
	
}