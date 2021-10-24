package net.heyitsultra.sandblock.Client.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	
	private boolean[] keys = new boolean[65536];
	public boolean num1, num2, num3, num4, num5, num6, num7, num8, num9, num0;
	public boolean F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12;
	public boolean a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;
	public boolean ctrl, esc, shift, enter, back, plus, minus, tab, space;
	public boolean slash, lbracket, rbracket, quote;
	public boolean up, down, left, right;
	
	public void update() {
		a = keys[KeyEvent.VK_A];
		b = keys[KeyEvent.VK_B];
		c = keys[KeyEvent.VK_C];
		d = keys[KeyEvent.VK_D];
		e = keys[KeyEvent.VK_E];
		f = keys[KeyEvent.VK_F];
		g = keys[KeyEvent.VK_G];
		h = keys[KeyEvent.VK_H];
		i = keys[KeyEvent.VK_I];
		j = keys[KeyEvent.VK_J];
		k = keys[KeyEvent.VK_K];
		l = keys[KeyEvent.VK_L];
		m = keys[KeyEvent.VK_M];
		n = keys[KeyEvent.VK_N];
		o = keys[KeyEvent.VK_O];
		p = keys[KeyEvent.VK_P];
		q = keys[KeyEvent.VK_Q];
		r = keys[KeyEvent.VK_R];
		s = keys[KeyEvent.VK_S];
		t = keys[KeyEvent.VK_T];
		u = keys[KeyEvent.VK_U];
		v = keys[KeyEvent.VK_V];
		w = keys[KeyEvent.VK_W];
		x = keys[KeyEvent.VK_X];
		y = keys[KeyEvent.VK_Y];
		z = keys[KeyEvent.VK_Z];
		
		num0 = keys[KeyEvent.VK_0] || keys[KeyEvent.VK_NUMPAD0];
		num1 = keys[KeyEvent.VK_1] || keys[KeyEvent.VK_NUMPAD1];
		num2 = keys[KeyEvent.VK_2] || keys[KeyEvent.VK_NUMPAD2];
		num3 = keys[KeyEvent.VK_3] || keys[KeyEvent.VK_NUMPAD3];
		num4 = keys[KeyEvent.VK_4] || keys[KeyEvent.VK_NUMPAD4];
		num5 = keys[KeyEvent.VK_5] || keys[KeyEvent.VK_NUMPAD5];
		num6 = keys[KeyEvent.VK_6] || keys[KeyEvent.VK_NUMPAD6];
		num7 = keys[KeyEvent.VK_7] || keys[KeyEvent.VK_NUMPAD7];
		num8 = keys[KeyEvent.VK_8] || keys[KeyEvent.VK_NUMPAD8];
		num9 = keys[KeyEvent.VK_9] || keys[KeyEvent.VK_NUMPAD9];
		
		F1 = keys[KeyEvent.VK_F1];
		F2 = keys[KeyEvent.VK_F2];
		F3 = keys[KeyEvent.VK_F3];
		F4 = keys[KeyEvent.VK_F4];
		F5 = keys[KeyEvent.VK_F5];
		F6 = keys[KeyEvent.VK_F6];
		F7 = keys[KeyEvent.VK_F7];
		F8 = keys[KeyEvent.VK_F8];
		F9 = keys[KeyEvent.VK_F9];
		F10 = keys[KeyEvent.VK_F10];
		F11 = keys[KeyEvent.VK_F11];
		F12 = keys[KeyEvent.VK_F12];

		ctrl = keys[KeyEvent.VK_CONTROL];
		shift = keys[KeyEvent.VK_SHIFT];
		tab = keys[KeyEvent.VK_TAB];
		space = keys[KeyEvent.VK_SPACE];
		enter = keys[KeyEvent.VK_ENTER];
		back = keys[KeyEvent.VK_BACK_SPACE];
		esc = keys[KeyEvent.VK_ESCAPE];
		plus = keys[KeyEvent.VK_EQUALS];
		minus = keys[KeyEvent.VK_MINUS];

		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e) {
		
	}	
}