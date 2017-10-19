package stealth;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/*
 * Implements the guard class. Guards will be responsible to keep the base safe
 */

public class Guard extends Entity {
	private Animation guardLeftAnimation;
	private Animation guardRightAnimation;
	private String orientation = "STANDING";  // can either be left, right or standing 
	private Image rightStanding;
	private Image leftStanding;
	private Image rightDirection[] = new Image[8];
	private Image leftDirection[] = new Image[8];
	private Vector max_velocity = new Vector(5f, 5f);
	private Vector velocity;
	private boolean stopped = true;
	private int[][] patrolRoutes;
	
	public Guard(int[][] patrolRoutes) {
		super(20, 100);
		velocity = new Vector(0f, 0f);
		this.patrolRoutes = patrolRoutes;
		
		SpriteSheet guardSprites = ResourceManager.getSpriteSheet(StealthGame.SOLDIER_SRC, 60, 60);
		rightStanding = guardSprites.getSubImage(8, 400, 50, 50);
		leftStanding = guardSprites.getSubImage(59, 456, 50, 50);
		
		for (int i=0; i < 8; i++) {
			// right
			int increment = i * 51;
			rightDirection[i] = guardSprites.getSubImage(8 + increment, 456, 50, 50);
			//left 
			leftDirection[i] = guardSprites.getSubImage(8 + increment, 512, 50, 50);
		}
		
		// left side images
		addImageWithBoundingBox(leftStanding);
		
		// set the animation 
		guardRightAnimation = new Animation(rightDirection, 100);
		guardLeftAnimation = new Animation(leftDirection, 100);
				
	}	
}