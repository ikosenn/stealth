package stealth;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * This is what draws the different levels. 
 * It also stores information about a level such as the patrol routes,
 * where the treasure chest should be place among other things.
 * 
 * @author peculiaryak
 *
 */
public class World {
	private static final String levelOneTile = "stealth/resources/levelone.tmx";
	private static final int topY = 64;
	private static final int topX = 0;
	
	private TiledMap map;
	/**
	 * World constructor
	 * @param level. The level to create
	 */
	public World(int level) {
		this.generateLevel(level);
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
		map.render(topX, topY);
	}
}
