package stealth;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import jig.Vector;

/**
 * Implements the dijkstra's algorithm.
 * 
 * @author peculiaryak
 *
 */
public class Dijkstra {
	private Node[][] nodes;
	private Vector goal;
	private ArrayList<Node> graphNodes = new ArrayList<>();
	private int[][] pathCost;
	private int maxCost = 1000;
	private int minCost = 1;
	
	/**
	 * Constructor
	 * 
	 * @param nodes. The nodes that comprise the tiled map
	 * @param goal. The goal to get to.
	 */
	public Dijkstra(Node[][] nodes, Vector goal) {
		this.nodes = nodes;
		this.goal = goal;
		
	}
	
	/**
	 * creates two arrays. One to hold the nodes(ArrayList),
	 * a multi-dimensional array containing the scores. 
	 * With 0 being the start state and infinity to represent the other states.
	 * We use nodes to store the path i.e whom is the predecessor to whom 
	 */
	private void generateRequiredDS() {
		// get all vertices
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = 0; j < this.nodes[i].length; j++) {
				graphNodes.add(nodes[i][j]);
			}
		}
		
		// create path scores
		pathCost = new int[this.nodes.length][this.nodes[0].length];
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = 0; j < this.nodes[i].length; j++) {
				pathCost[i][j] = Integer.MAX_VALUE;
			}
		}
		int goalX = ((int)goal.getY() - World.TOP_Y) / World.TILE_SIZE;
		int goalY =  (int)goal.getX() / World.TILE_SIZE;
		pathCost[goalX][goalY] = 0;
	}
	
	/**
	 * Get the minimum node value from the set of graph nodes provided
	 * @return The min-node
	 */
	private Node extractMinNode() {
		Node minNode = null;
		Node currentNode = null;
		for (int i = 0; i < graphNodes.size(); i++) {
			currentNode = graphNodes.get(i);
			int cNodeX = currentNode.getX();
			int cNodeY = currentNode.getY();
			if (minNode == null) {
				minNode = graphNodes.get(i);
			} else if (pathCost[cNodeX][cNodeY] < pathCost[minNode.getX()][minNode.getY()]) {
				minNode = currentNode;
			}
		}
		
		return minNode;
	}
	
	/**
	 * Get the possible nodes one can travel to from the current node
	 * 
	 * @params n. The node to find children for
	 * @return An array containing child nodes.
	 */
	private ArrayList<Node> getAdjacentNode(Node n) {
		ArrayList<Node> childNodes = new ArrayList<Node>();
		int maxX = nodes.length;
		int maxY = nodes[0].length;
		if ((n.getX() - 1) >= 0) {
			childNodes.add(nodes[n.getX() - 1][n.getY()]);
		}
		if ((n.getX() + 1) < maxX) {
			childNodes.add(nodes[n.getX() + 1][n.getY()]);
		}
		
		if ((n.getY() - 1) >= 0) {
			childNodes.add(nodes[n.getX()][n.getY() - 1]);
		}
		if ((n.getY() + 1) < maxY) {
			childNodes.add(nodes[n.getX()][n.getY() + 1]);
		}
		
		return childNodes;
	}
	
	/**
	 * where all the magic happens.
	 * Use the goal state to calculate the weights for each node.
	 * 
	 * We make the assumption that you can move to any side i.e top, bottom, left and right
	 * We will not handle diagonal movement.
	 */
	private void relax() {
		
		while (graphNodes.size() > 0) {
			Node minNode = this.extractMinNode();
			ArrayList<Node> childNodes = this.getAdjacentNode(minNode);
			int minNodeX = minNode.getX();
			int minNodeY = minNode.getY();
			
			for (int i = 0; i < childNodes.size(); i++ ) {
				Node tempNode = childNodes.get(i);
				int tempNodeX = tempNode.getX();
				int tempNodeY = tempNode.getY();
				int weight = tempNode.isBlocked() ? this.maxCost : this.minCost;
				if (weight + pathCost[minNodeX][minNodeY] < pathCost[tempNodeX][tempNodeY]) {
					pathCost[tempNodeX][tempNodeY] = weight + pathCost[minNodeX][minNodeY];
					nodes[tempNodeX][tempNodeY].setPredecessor(minNode);
				}
			}
			graphNodes.remove(minNode);		
		}
		
	}
	
	/**
	 * runs all the required operations to create a multi-dimensional array that a path
	 * can be constructed from.
	 */
	public void computePath() {
		this.generateRequiredDS();
		this.relax();
	}
	
	
	/**
	 * Compute the path
	 * @return Array. A set of node when traversed leads to the goal state
	 */
	public ArrayList<Node> getPath(Vector start) {
		ArrayList<Node> route = new ArrayList<>();
		int startX = ((int)start.getY() - World.TOP_Y) / World.TILE_SIZE;
		int startY =  (int)start.getX() / World.TILE_SIZE;
		int goalX = ((int)goal.getY() - World.TOP_Y) / World.TILE_SIZE;
		int goalY =  (int)goal.getX() / World.TILE_SIZE;
		Node currentNode = nodes[startX][startY];
		route.add(currentNode);
		while (currentNode != nodes[goalX][goalY]) {
			currentNode = currentNode.getPredecessor();
			route.add(currentNode);
		}
		return route;
	}
	
	/**
	 * Renders square tiles on the selected path.
	 * 
	 * @param path. The set of nodes that constitute the path.
	 * @param g. A graphics context that can be used to render primitives to the accelerated canvas provided by LWJGL.
	 */
	public static void render(ArrayList<Node> path, Graphics g) {
		int tileSize = World.TILE_SIZE;
		for (int i = 0; i < path.size(); i++) {
			Node tempNode = path.get(i);
			int tNodeX = tempNode.getX();
			int tNodeY = tempNode.getY();
			g.fillRect(
				tNodeY * tileSize, (tNodeX * tileSize) + World.TOP_Y, tileSize, tileSize);
		}	
	}
	
}
