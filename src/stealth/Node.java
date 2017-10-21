package stealth;
import jig.Vector;

public class Node {
	private int x; // position in worlds multi-dimensional Array
	private int y; // position in worlds multi-dimensional Array
	private Node predecessor = null; // for path finding. Which node to move to when planning a path.
	private boolean blocked; // associate moving to blocked nodes with a higher cost
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.blocked = false;
	}
	
	/**
	 * Returns the center of the tile. 
	 * @return A vector with the center of the tile
	 */
	public Vector getCenter() {
		int tileSize = World.TILE_SIZE;
		int tilesetTopY = World.TOP_Y;
		int tileX = (y * tileSize) + (tileSize/ 2);
		int tileY = (x * tileSize) + (tileSize/ 2) + tilesetTopY;
		return new Vector(tileX, tileY);
	}
	
	
	/**
	 * Checks if the tile is blocked. i.e restricted areas
	 * 
	 * @return true if the tile is blocked. False otherwise
	 */
	public boolean isBlocked() {
		return blocked;
	}
	
	/**
	 * Blocked setter
	 * @param the new state to set blocked to.
	 */
	public void setBlocked(boolean state) {
		blocked = state;
	}
	
	/**
	 * Get's the next node to travel to.
	 * @return The predecessor node. Null if none exists e.g for the start node
	 */
	public Node getPredecessor() {
		if (predecessor != null) {
			return predecessor;
		}
		return null;
	}
	
	/**
	 * Predecessor setter
	 * 
	 * @param predecessor. The predecessor to this node.
	 */
	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}
	
	/*
	 * x getter
	 */
	public int getX() {
		return this.x;
	}
	
	/*
	 * y getter
	 */
	public int getY() {
		return this.y;
	}
}
