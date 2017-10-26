package stealth;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

import jig.Collision;
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
	private Dijkstra pathPlanner;
	private Node currentNode; 
	private int bullets; // the number of bullets the soldier has
	private String previousSide = "";  // get the side the soldier is/ was facing
	private Sound fireGun;
	private Sound emptyGun;
	private Vector startPos;
	
	public Soldier(Vector startPos, int bullets) {
		super(startPos.getX(), startPos.getY());
		this.startPos = startPos;
		velocity = new Vector(0f, 0f);
		this.bullets = bullets;
		
		SpriteSheet soldierSprites = ResourceManager.getSpriteSheet(StealthGame.SOLDIER_SRC, 60, 60);
		rightStanding = soldierSprites.getSubImage(13, 11, 40, 50);
		leftStanding = soldierSprites.getSubImage(67, 11, 40, 50);
		
		for (int i=0; i < 8; i++) {
			// right
			int increment = i * 51;
			rightDirection[i] = soldierSprites.getSubImage(13 + increment, 67, 40, 50);
			//left 
			leftDirection[i] = soldierSprites.getSubImage(13 + increment, 123, 40, 50);
		}
		
		// left side images
		addImageWithBoundingBox(rightStanding);
		
		// set the animation 
		soldierRightAnimation = new Animation(rightDirection, 100);
		soldierLeftAnimation = new Animation(leftDirection, 100);
		
		// track the node the soldier is on for path finding
		int startX = ((int)this.getY() - World.TOP_Y) / World.TILE_SIZE;
		int startY =  (int)this.getX() / World.TILE_SIZE;
		this.currentNode = new Node(startX, startY);	
		this.fireGun = ResourceManager.getSound(StealthGame.GUN_SHOT_SRC);
		this.emptyGun = ResourceManager.getSound(StealthGame.GUN_EMPTY_SRC);
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
	 * Generates the path to the soldier given a vector as the start node.
	 * @param startNode. The node to use as the start node.
	 * @return ArrayList containing the set of paths to follow
	 */
	public ArrayList<Node> getPathToMe(Vector startNode) {
		if (this.pathPlanner != null) {
			return pathPlanner.getPath(startNode);
		}
		return null;
	}
	
	/**
	 * Calculates the path to the soldier if the alarm is sounded.
	 * The path is constantly update if the soldier moves out of the node
	 * @param sg. Holds the game state
	 */
	private void setPathToMe(StealthGame sg) {
		if (sg.isAlarmOn()) {
			int startX = ((int)this.getY() - World.TOP_Y) / World.TILE_SIZE;
			int startY =  (int)this.getX() / World.TILE_SIZE;
			if (this.currentNode.getX() != startX || this.currentNode.getY() != startY) {
				pathPlanner = new Dijkstra(
						sg.world.getNodes(), new Vector(this.getX(), this.getY()));
				pathPlanner.computePath();
			}
		}
	}
	
	/**
	 * Shoots the gun if the soldier has bullets left.
	 * @param game. Hold the current state of the game
	 */
	private void weaponsFree(StealthGame game) {
		if (this.bullets > 0) {
			Bullet bullet;
			if (this.previousSide == "LEFT") {
				bullet = new Bullet(this.getX() - 10f, this.getY(), this.previousSide);
			} else {
				bullet = new Bullet(this.getX() + 10f, this.getY(), this.previousSide);
			}
			
			bullet.fire();
			this.fireGun.play();
			this.bullets -= 1;
			game.bullets.add(bullet);
		} else {
			this.emptyGun.play();
		}
	}
	
	
	/**
	 * Bullet getter
	 * @return int. The number of bullets the soldier has
	 */
	public int getBulletCount() {
		return this.bullets;
	}
	
	/**
	 * 
	 * @return false if it has reached the bounds of the game otherwise true
	 */
	private boolean checkCollision(String direction, StealthGame game) {
		// prevent from going into the walls 
		if (this.getCoarseGrainedMaxX() >= StealthGame.SCREEN_WIDTH && direction == "RIGHT") {
			this.setPosition(StealthGame.SCREEN_WIDTH - 15, this.getY());
			return false;
		}
		if (this.getCoarseGrainedMinX() <= 0 && direction == "LEFT") {
			this.setPosition(0 + 15, this.getY());
			return false;
		}
		if (this.getCoarseGrainedMinY() <= World.TOP_Y && direction == "UP") {
			this.setPosition(this.getX(), World.TOP_Y + 20);
			return false;
		}
		if (this.getCoarseGrainedMaxY() >= StealthGame.SCREEN_HEIGHT && direction == "DOWN") {
			this.setPosition(this.getX(), StealthGame.SCREEN_HEIGHT - 25);
			return false;
		}
		return true;
	}
	
	private void checkGuardCollision(StealthGame sg) {
		if (sg.guards !=null && sg.guards.size() > 0) {
			for (int i = 0; i < sg.guards.size(); i++) {
				Collision isPen = this.collides(sg.guards.get(i));
				if (isPen != null) {
					sg.reduceLives();
					sg.soundAlarm(false);
					this.setPosition(startPos);
					sg.resetCounter();
				}
			}
		}
	}
	
	/**
	 * Updates the position of the soldier based on the users input. 
	 * 
	 * @param container
	 * A generic game container that handles the game loop, fps recording and managing the input system 
	 * 
	 * @param container
	 * A generic game container that handles the game loop, fps recording and managing the input system 
	 * 
	 * @param game
	 * Holds the state of the game.
	 */
	public void update(GameContainer container, StealthGame game, int delta) {
		this.checkGuardCollision(game);
		Input input = container.getInput();
		this.setPathToMe(game);
		boolean moved = false;
		
		if ((input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) && this.checkCollision("LEFT", game)) {
			if (orientation != "LEFT") {
				resetEntity();
				addAnimation(soldierLeftAnimation);
				orientation = "LEFT";
				previousSide = "LEFT";
			}
			// check if the soldier was moving to the right/ up /down
			if (this.getVelocity().getX() > 0f || Math.abs(this.getVelocity().getY()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(-.2f, 0f);
			moved = true;
			stopped = false;
		}
		if ((input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) && this.checkCollision("RIGHT", game)){
			if (orientation != "RIGHT") {
				resetEntity();
				addAnimation(soldierRightAnimation);
				orientation = "RIGHT";
				previousSide = "RIGHT";
			}
			// check if the soldier was moving to the left/ up/ down
			if (this.getVelocity().getX() < 0f || Math.abs(this.getVelocity().getY()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(+.2f, 0f);
			moved = true;
			stopped = false;
		}
		if ((input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) && this.checkCollision("UP", game)) {
			// check if the soldier was moving down/ left/ right
			if (this.getVelocity().getY() > 0f || Math.abs(this.getVelocity().getX()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(0f, -.2f);
			moved = true;
			stopped = false;
		}
		if ((input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) && this.checkCollision("DOWN", game)){
			// check if the soldier was moving up/ left/ right
			if (this.getVelocity().getY() < 0f || Math.abs(this.getVelocity().getX()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(0f, +.2f);
			moved = true;
			stopped = false;
		}
		
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			this.weaponsFree(game);
		}
		
		if (input.isKeyPressed(Input.KEY_LSHIFT)) {
			if (game.powerups != null && game.powerups.size() > 0) {
				game.powerups.get(0).setInUse();
			}
		}
		
		translate(this.getVelocity());
		// reset velocity and image if no key is pressed
		if (!moved && !stopped) {
			resetEntity();
			if (orientation == "LEFT") {
				previousSide = "LEFT";
				addImageWithBoundingBox(leftStanding);
			} else if (orientation == "RIGHT") {
				addImageWithBoundingBox(rightStanding);
				previousSide = "RIGHT";
			} else {
				addImageWithBoundingBox(rightStanding);
			}
			orientation = "STANDING";
			this.setVelocity(0f, 0f);
			stopped = true;
		}
	}
}
