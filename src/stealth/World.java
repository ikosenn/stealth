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
	
	private Vector[][] patrolRoutes;
	private static final String levelOneTile = "stealth/resources/levelone.tmx";
	private static final String levelTwoTile = "stealth/resources/leveltwo.tmx";
	private static final String levelThreeTile = "stealth/resources/levelthree.tmx";
	private TiledMap map;
	private Node[][] nodes;
	private int level;
	private Vector startPos; // where the soldier should start from
	private Vector goalPos;  // the level goal state.
	private Vector treasureChestPos; // where the treasure chest should be placed
	
	/**
	 * World constructor
	 * @param level. The level to create
	 */
	public World(int level) {
		this.level = level;
		this.generateLevel();
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
	}
	/**
	 * Determines which method to call. The method called is responsible 
	 * for initializing everything for that level.
	 * 
	 * @throws SlickException 
	 */
	private void generateLevel() {
		
		try {
			if (this.level == 1) {
				this.createLevelOne();
			} else if (this.level == 2) {
				this.createLevelTwo();
			} else if (this.level == 3) {
				this.createLevelThree();
			}
		} catch (SlickException e) {	
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the patrol routes for the guards based on the level.
	 */
	private void setPatrolRoutes() {
		if (this.level == 1) {
			Vector[][] levelPatrolRoutes = {
				{new Vector(20, 100), new Vector(416, 640)},
				{new Vector(1010, 540), new Vector(680, 760)},
				{new Vector(780, 94), new Vector(1010, 400)}
			};
			patrolRoutes = new Vector[3][2];
			patrolRoutes = levelPatrolRoutes;
		} else if (this.level == 2) {
			Vector[][] levelPatrolRoutes = {
				{new Vector(990, 90), new Vector(250, 150)},
				{new Vector(990, 770), new Vector(990, 400)}
			};
			patrolRoutes = new Vector[2][2];
			patrolRoutes = levelPatrolRoutes;
		} else if (this.level == 3) {
			Vector[][] levelPatrolRoutes = {
					{new Vector(990, 770), new Vector(790, 770)},
					{new Vector(570, 650), new Vector(20, 200)},
					{new Vector(840, 90), new Vector(300, 430)}
				};
				patrolRoutes = new Vector[3][2];
				patrolRoutes = levelPatrolRoutes;
			}
	}
	
	/**
	 * Responsible for bringing everything together that is required to create 
	 * the first level. 
	 * @throws SlickException 
	 */
	private void createLevelOne() throws SlickException {
		this.map = new TiledMap(World.levelOneTile); 
		this.setPatrolRoutes();
		this.startPos = new Vector(20, 770);
		this.goalPos = new Vector(990, 90);
		this.treasureChestPos = new Vector(932, 660);
	}
	
	/**
	 * Responsible for bringing everything together that is required to create 
	 * the second level. 
	 * @throws SlickException 
	 */
	private void createLevelTwo() throws SlickException {
		this.map = new TiledMap(World.levelTwoTile); 
		this.setPatrolRoutes();
		this.startPos = new Vector(20, 90);
		this.goalPos = new Vector(990, 770);
		this.treasureChestPos = new Vector(450, 450);
	}
	
	/**
	 * Responsible for bringing everything together that is required to create 
	 * the third level. 
	 * @throws SlickException 
	 */
	private void createLevelThree() throws SlickException {
		this.map = new TiledMap(World.levelThreeTile); 
		this.setPatrolRoutes();
		this.startPos = new Vector(20, 770);
		this.goalPos = new Vector(990, 770);
		this.treasureChestPos = new Vector(550, 750);
	}
	
	/**
	 * nodes getter
	 * @return the nodes that map to the current tile map
	 */
	public Node[][] getNodes() {
		this.createNode();
		return this.nodes;
	}
	
	/*
	 * Patrol routes getter
	 * 
	 * @returns Vector. The vectors to assign the guards.
	 */
	public Vector[][] getPatrolRoutes() {
		return this.patrolRoutes;
	}
	
	/**
	 * Get the soldier's start position for the level
	 * @return Vector. The soldiers start position
	 */
	public Vector getStartPos() {
		return this.startPos;
	}
	
	/**
	 * Get the world's goal position
	 * @return Vector. The soldiers target position
	 */
	public Vector getGoalPos() {
		return this.goalPos;
	}
	
	/**
	 * Where to draw the treasure chest.
	 * @return Vector. The area to draw the treasure chest
	 */
	public Vector getTreasureChestPos() {
		return this.treasureChestPos;
	}
	
	/**
	 * Handles the rendering of the world
	 * @param g. Allows us to draw on the screen
	 */
	public void render(Graphics g) {
		map.render(World.TOP_X, World.TOP_Y);
	}
}
