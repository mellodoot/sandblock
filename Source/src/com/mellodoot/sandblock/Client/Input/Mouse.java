package com.mellodoot.sandblock.Client.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.mellodoot.sandblock.Client.Util.Vector2i;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private int mouseX = -1;
	private int mouseY = -1;
	private int mouseB = -1;
	private int mouseS = -1;
	private int cooldown;
	public int scale = 3;
	
	public void update() {
		if (cooldown > 0) cooldown--;
		if (cooldown <= 0) mouseS = 0;
	}
	
	public int getX() {
		return mouseX / scale;
	}
	
	public int getY() {
		return mouseY / scale;
	}
	
	public boolean within(int x, int y, int width, int height) {
		int mouseX = getX();
		int mouseY = getY();
		
		Vector2i mouseVec = new Vector2i(mouseX, mouseY);
		Vector2i vecStart = new Vector2i(x, y);
		Vector2i vecEnd = new Vector2i(x + width, y + height);
		
		if (Vector2i.within(mouseVec, vecStart, vecEnd))
			return true;
		else
			return false;
	}
	
	public boolean within(Vector2i vec, int width, int height) {
		int mouseX = getX();
		int mouseY = getY();
		
		Vector2i mouseVec = new Vector2i(mouseX, mouseY);
		Vector2i vecStart = vec;
		Vector2i vecEnd = vec.add(new Vector2i(width, height));
		
		if (Vector2i.within(mouseVec, vecStart, vecEnd))
			return true;
		else
			return false;
	}
	
	public boolean withinFixed(int x, int y, int width, int height, int xScroll, int yScroll) {
		Vector2i mouseVec = new Vector2i(getX() - xScroll, getY() - yScroll);
		Vector2i vecStart = new Vector2i(x, y);
		Vector2i vecEnd = new Vector2i(x + width, y + height);
		
		if (Vector2i.within(mouseVec, vecStart, vecEnd))
			return true;
		else
			return false;
	}
	
	public int getButton() {
		return mouseB;
	}
	
	public int getWheelRotation() {
		return mouseS;
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
	}
	
	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseS = e.getWheelRotation();
		cooldown = 1;
	}
	
}