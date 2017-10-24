package stealth;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
	private Vector max_velocity = new Vector(4f, 4f);
	private Vector velocity;
	private Vector[] patrolPoints;
	private int nextPath = 1;  // we store patrol paths. Tracks the next on to follow
	private boolean isGoalState = false; // checks whether the guard is at the goal state based on the patrol route
	private ArrayList<Node> currentPath; // track the path we are currently following
	private Dijkstra pathPlanners[]; // store the patrol route paths
	private boolean stopped = true;
	private boolean debugPath = false;
	private String previousKeyPressed = "";
	
	
	public Guard(Vector[] patrolPoints) {
		super(patrolPoints[0].getX(), patrolPoints[0].getY());
		velocity = new Vector(0f, 0f);
		this.patrolPoints = new Vector[patrolPoints.length];
		this.patrolPoints = patrolPoints;
		
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
	
	/**
	 * Pre-compute and store the patrol paths.
	 * @param world. Defines what the level is built up of.
	 */
	public void generatePatrolPaths(World world) {
		pathPlanners = new Dijkstra[this.patrolPoints.length];
		for (int i=0; i < this.patrolPoints.length; i++) {
			Dijkstra pathPlanner = new Dijkstra(world.getNodes(), this.patrolPoints[i]);
			pathPlanner.computePath();
			pathPlanners[i] = pathPlanner;
		}
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
		removeAnimation(guardRightAnimation);
		removeAnimation(guardLeftAnimation);
	}
	
	/**
	 * Determines the path to follow currently. This changes based on the patrol route the guard is currently on
	 * and also if the alarm has been sounded
	 * @param sg
	 */
	private void determineCurrentPath(StealthGame sg ) {
		if (isGoalState && ((this.nextPath + 1) < this.pathPlanners.length)) {
			this.isGoalState = false;
			this.nextPath += 1;
		} else if (isGoalState) {
			this.nextPath = 0;
			this.isGoalState = false;
		}
		
		if (sg.isAlarmOn()) {
			this.currentPath = sg.soldier.getPathToMe(new Vector(this.getX(), this.getY()));
		} else {
			this.currentPath = this.pathPlanners[this.nextPath].getPath(
			new Vector(this.getX(), this.getY()));
			Vector goalNode = this.pathPlanners[this.nextPath].getGoal();
			if (this.currentPath.size() == 1 && goalNode.getX() == (int)this.getX() && 
					goalNode.getY() == (int)this.getY() ) {
				this.isGoalState = true;
			}
		}
	}
	/**
	 * tries to mimic the movement as though it was being input from the 
	 * keyboard;
	 * @param destination. The node to go to
	 * @return The key to press
	 */
	private String getKeyPressed(Vector destination) {
		String keyPressed = "";
		 // travel to the center of the node

		// always prefer moving down if the 
		if (previousKeyPressed == "S" && destination.getY() > (int)this.getY()) {
			keyPressed = "S";
		} else if(destination.getX() > (int)this.getX()) {
			keyPressed = "D";
		} else if(destination.getX() < (int)this.getX()) {
			keyPressed = "A";
		} else if(destination.getY() < (int)this.getY()) {
			keyPressed = "W";
		} else if(destination.getY() > (int)this.getY()) {
			keyPressed = "S";
		} 
		previousKeyPressed = keyPressed;
		return keyPressed;
	}
	
	/**
	 * Update the position of the guard
	 * @param sg. Contains the current game state
	 */
	public void update(StealthGame sg) {
		this.determineCurrentPath(sg);
		Vector destination;
		if (currentPath.size() > 1)  {
			destination = currentPath.get(1).getCenter(); 
		} else {
			destination = this.pathPlanners[this.nextPath].getGoal();
		}
		String keyPressed = this.getKeyPressed(destination);
		boolean moved = false;
		if (keyPressed == "A") {
			if (orientation != "LEFT") {
				resetEntity();
				addAnimation(guardLeftAnimation);
				orientation = "LEFT";
			}
			// check if the guard was moving to the right/ up and down
			if (this.getVelocity().getX() > 0f || Math.abs(this.getVelocity().getY()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(-.2f, 0f);
			moved = true;
			stopped = false;
		}
		if (keyPressed == "D"){
			if (orientation != "RIGHT") {
				resetEntity();
				addAnimation(guardRightAnimation);
				orientation = "RIGHT";
			}
			// check if the soldier was moving to the left/ up and down
			if (this.getVelocity().getX() < 0f || Math.abs(this.getVelocity().getY()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(+.2f, 0f);
			moved = true;
			stopped = false;
		}
		if (keyPressed == "W") {
			if (orientation != "STANDING") {
				resetEntity();
				addImageWithBoundingBox(rightStanding);
				orientation = "STANDING";
			}
			// check if the soldier was moving down/ left and right
			if (this.getVelocity().getY() > 0f || Math.abs(this.getVelocity().getX()) > 0) {
				// reset it
				this.setVelocity(0f, 0f);
			}
			this.setVelocity(0f, -.2f);
			moved = true;
			stopped = false;
		}
		if (keyPressed == "S"){
			if (orientation != "STANDING") {
				resetEntity();
				addImageWithBoundingBox(rightStanding);
				orientation = "STANDING";
			}
			// check if the soldier was moving up/ left / right
			if (this.getVelocity().getY() < 0f || Math.abs(this.getVelocity().getX()) > 0) {
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
	/**
	 * Draws all boundaries and images associated with the entity at their 
	 * designated offset values. We override this so we can be able to debug the paths 
	 * @param g The current graphics context
	 */
	@Override
	public void render(final Graphics g) {
		super.render(g);
		
		if (this.debugPath) {
			Dijkstra.render(currentPath, g);
		}
	}
	
}