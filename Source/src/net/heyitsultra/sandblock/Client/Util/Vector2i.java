package net.heyitsultra.sandblock.Client.Util;

public class Vector2i {
	
	private int x, y;
	
	public Vector2i() {
	}
	
	public Vector2i(int x, int y) {
		set(x, y);
	}
	
	public static boolean within(Vector2i vec, Vector2i vecStart, Vector2i vecEnd) {
		if (vec.x >= vecStart.x && vec.x <= vecEnd.x)
			if (vec.y >= vecStart.y && vec.y <= vecEnd.y)
				return true;
		return false;
	}
	
	public Vector2i(Vector2i vector) {
		set(vector.x, vector.y);
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(Vector2i vector) {
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}
	
	public Vector2i subtract(Vector2i vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Vector2i setX(int x) {
		this.x = x;
		return this;
	}
	
	public Vector2i setY(int y) {
		this.y = y;
		return this;
	}
	
	public static double getDistance(Vector2i v0, Vector2i v1) {
		double x = v0.getX() - v1.getX();
		double y = v0.getY() - v1.getY();
		return Math.sqrt(x * x + y * y);
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if (vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}
	
}