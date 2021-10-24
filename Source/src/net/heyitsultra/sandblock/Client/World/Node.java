package net.heyitsultra.sandblock.Client.World;

import net.heyitsultra.sandblock.Client.Util.Vector2i;

public class Node {
	
	public Vector2i pos;
	public Node parent;
	public double fCost, gCost, hCost;
	
	public Node(Vector2i pos, Node parent, double gCost, double hCost) {
		this.pos = pos;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = gCost + hCost;
	}
	
}