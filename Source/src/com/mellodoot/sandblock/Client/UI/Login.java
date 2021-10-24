package com.mellodoot.sandblock.Client.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.mellodoot.sandblock.Client.Game;

public class Login extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtUsername;
	private JTextField txtUserToken;
	private JPanel contentPane;
	private JLabel lblLoginText;
	private JButton btnAccept;
	
	public Login(Game main) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/icons/gamejolt.png")));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("GameJolt Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 160);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		lblLoginText = new JLabel("Login to your GameJolt Account");
		contentPane.add(lblLoginText);
		
		txtUsername = new JTextField();
		txtUsername.setToolTipText("Username");
		contentPane.add(txtUsername);
		txtUsername.setColumns(25);
		
		txtUserToken = new JTextField();
		txtUserToken.setToolTipText("User Token");
		contentPane.add(txtUserToken);
		txtUserToken.setColumns(25);
		
		btnAccept = new JButton("Login");
		Dimension size = new Dimension(100, 25);
		btnAccept.setPreferredSize(size);
		contentPane.add(btnAccept);
		
		setVisible(true);
		
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				api(main);
			}
		});
		
		KeyAdapter key = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					api(main);					
				}
			}
		};
		txtUsername.addKeyListener(key);
		txtUserToken.addKeyListener(key);
		
	}
	
	private void api(Game main) {
		dispose();
		main.newApi(txtUsername.getText(), txtUserToken.getText());
	}
	
}
