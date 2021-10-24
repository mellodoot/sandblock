package com.mellodoot.sandblock.Server;

import java.awt.*;
import java.net.*;

import javax.swing.*;

// import net.heyitsultra.sandblock.Client.UI.*;
// import org.eclipse.wb.swing.FocusTraversalOnArray;

public class GameServer {
	
	private JFrame frame;
	private JTextField textField;
	private JTextArea txtrTest_1;
	
	public GameServer(String[] args) {
		Server server = new Server(2248);
		
		frame = new JFrame("SandBlock Server");
		// Dimension size = new Dimension(640, 320);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(GameServer.class.getResource("/icons/sandblock.png")));
		frame.setPreferredSize(new Dimension(900, 480));
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		textField = new JTextField();
		frame.getContentPane().add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JTextArea txtrTest = new JTextArea();
		txtrTest.setText("Test");
		txtrTest.setBackground(Color.BLACK);
		txtrTest.setForeground(Color.GREEN);
		txtrTest.setEditable(false);
		splitPane.setRightComponent(txtrTest);
		
		txtrTest_1 = new JTextArea();
		txtrTest_1.setText("Players:        ");
		txtrTest_1.setDropMode(DropMode.INSERT_COLS);
		splitPane.setLeftComponent(txtrTest_1);
		// frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textField}));
		frame.setVisible(true);
		
		InetAddress address = null;
		try {
			address = InetAddress.getByName("heyitsultra.ddns.net");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		int port = 2248;
		server.send(new byte[] {0, 1, 2}, address, port);		
	}
	
	public static void main(String[] args) {
		new GameServer(args);
	}
	
}
