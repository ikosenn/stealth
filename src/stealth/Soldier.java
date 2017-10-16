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
 * Implements the soldier who'll be sneaking behind enemy lines
 */

public class Soldier extends Entity {
	private Animation soldierLeftAnimation;
	private Animation soldierRightAnimation;
	private String orientation = "STANDING";  // can either be left, right or standing 
	private Image rightStanding;
	private Image leftStanding;
	private Image rightDirection[] = new Image[8];
	private Image leftDirection[] = new Image[8];
	private Vector max_velocity = new Vector(5f, 5f);
	private Vector velocity;
	private boolean stopped = true;
	
	public Soldier() {
		super(20, 770);
		velocity = new Vector(0f, 0f);
		
		
		SpriteSheet soldierSprites = ResourceManager.getSpriteSheet(StealthGame.SOLDIER_SRC, 60, 60);
		rightStanding = soldierSprites.getSubImage(8, 11, 50, 50);
		leftStanding = soldierSprites.getSubImage(59, 11, 50, 50);
		
		for (int i=0; i < 8; i++) {
			// right
			int increment = i * 51;
			rightDirection[i] = soldierSprites.getSubImage(8 + increment, 67, 50, 50);
			//left 
			leftDirection[i] = soldierSprites.getSubImage(8 + increment, 123, 50, 50);
		}
		
		// left side images
		addImageWithBoundingBox(rightStanding);
		
		// set the animation 
		soldierRightAnimation = new Animation(rightDirection, 100);
		soldierLeftAnimation = new Animation(leftDirection, 100);
				
	}
	
	/**
	 * Velocity setter
	 * @param x The value to set the vector's x value,
	 * @param y The value to set the vector's y value.
	 */
	public void setVelocity(float x, float y) {
		float currentX = this.getVelocity().getX();
		float currentY = this.getVelocity().getY();
		// restrict the max velocity of the soldier
		if (x == 0f && y == 0f) {
			velocity = new Vector(x, y);
		} else if (Math.abs(currentX) <= max_velocity.getX() && Math.abs(x) > 0f) {
			velocity = this.getVelocity().add(new Vector(x, y));
		} else if (Math.abs(currentY) <= max_velocity.getY() && Math.abs(y) > 0f) {
			velocity = this.getVelocity().add(new Vector(x, y));
		}
	}
	
	/**
	 * Velocity getter
	 * @return velocity. The current velocity
	 */

	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Resets the entity by removing all the images and animations
	 */
	public void resetEntity() {
		removeImage(leftStanding);
		removeImage(rightStanding);
		removeAnimation(soldierRightAnimation);
		removeAnimation(soldierLeftAnimation);
	}
	
	
	/**
	 * Updates the position of the soldier based on the users input. 
	 * 
	 * @param container
	 * A generic game container that handles the game loop, fps recording and managing the input system 
	 * 
	 * @param game
	 * Holds the state of the game.
	 */
	public void update(GameContainer container) {
		Input input = container.getInput();
		
		boolean moved = false;
		
		if ((input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT))) {
			if (orientation != "LEFT") {
				resetEntity();
				addAnimation(soldierLeftAnimation);
				orientation = "LEFT";
			}
			// check if the soldier was moving to the right
			if (this.getVelocity().getX() > 0f) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(-.2f, 0f);
			moved = true;
			stopped = false;
		}
		if ((input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT))){
			if (orientation != "RIGHT") {
				resetEntity();
				addAnimation(soldierRightAnimation);
				orientation = "RIGHT";
			}
			// check if the soldier was moving to the left
			if (this.getVelocity().getX() < 0f) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(+.2f, 0f);
			moved = true;
			stopped = false;
		}
		if ((input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP))) {
			// check if the soldier was moving down
			if (this.getVelocity().getY() > 0f) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(0f, -.2f);
			moved = true;
			stopped = false;
		}
		if ((input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN))){
			// check if the soldier was moving up
			if (this.getVelocity().getY() < 0f) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(0f, +.2f);
			moved = true;
			stopped = false;
		}
		
		translate(this.getVelocity());
		// reset velocity and image if no key is pressed
		if (!moved && !stopped) {
			resetEntity();
			if (orientation == "LEFT") {
				addImageWithBoundingBox(leftStanding);
			} else if (orientation == "RIGHT") {
				addImageWithBoundingBox(rightStanding);
			} else {
				addImageWithBoundingBox(rightStanding);
			}
			orientation = "STANDING";
			this.setVelocity(0f, 0f);
			stopped = true;
		}
	}
}
