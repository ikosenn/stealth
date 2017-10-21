package stealth;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import jig.Vector;

/**
 * This is what draws the different levels. 
 * It also stores information about a level such as the patrol routes,
 * where the treasure chest should be place among other things.
 * 
 * @author peculiaryak
 *
 */
public class World {
	public static final int TOP_Y = 64;
	public static final int TOP_X = 0;
	public static final int TILE_SIZE = 32;
	
	private static final String levelOneTile = "stealth/resources/levelone.tmx";
	private TiledMap map;
	private Node[][] nodes;
	/**
	 * World constructor
	 * @param level. The level to create
	 */
	public World(int level) {
		this.generateLevel(level);
		this.createNode();
	}
	
	/**
	 * Populate nodes
	 */
	private void createNode() {
		int nodeWidth = StealthGame.SCREEN_WIDTH / World.TILE_SIZE;
		int nodeHeight = (StealthGame.SCREEN_HEIGHT - World.TOP_Y) / World.TILE_SIZE;
		nodes = new Node[nodeHeight][nodeWidth];
		int objectLayer = map.getLayerIndex("walls");
		System.out.println("Height: " + this.map.getHeight());
		System.out.println("Width: " + this.map.getWidth());
		// create nodes object 
		for (int i=0; i < nodes.length; i++) {
			for (int j=0; j < nodes[i].length; j++) {
				Node tempNode = new Node(i, j);
				if (this.map.getTileId(j, i, objectLayer) != 0) {
					tempNode.setBlocked(true);
				}
				nodes[i][j] = tempNode;
			}
		}
		System.out.println("Node 0:0 " + nodes[0][0].isBlocked() + " " + nodes[0][0].getCenter());
		System.out.println("Node 0:1 " + nodes[1][0].isBlocked() + " " + nodes[1][0].getCenter());
		System.out.println("Node 0:2 " + nodes[2][0].isBlocked() + " " + nodes[2][0].getCenter());
		System.out.println("Node 0:3 " + nodes[19][0].isBlocked() + " " + nodes[19][0].getCenter());
	}
	/**
	 * Determines which method to call. The method called is responsible 
	 * for initializing everything for that level.
	 * 
	 * @param level. The level to generate
	 * @throws SlickException 
	 */
	private void generateLevel(int level) {
		
		try {
			if (level == 1) {
				this.createLevelOne();
			}
		} catch (SlickException e) {	
			e.printStackTrace();
		}
	}
	
	/**
	 * Responsible for bringing everything together that is required to create 
	 * the first level. 
	 * @throws SlickException 
	 */
	private void createLevelOne() throws SlickException {
		this.map = new TiledMap(World.levelOneTile); 
	}
	
	/**
	 * Handles the rendering of the world
	 * @param g. Allows us to draw on the screen
	 */
	public void render(Graphics g) {
		map.render(World.TOP_X, World.TOP_Y);
	}
}
