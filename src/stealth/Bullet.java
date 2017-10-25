package stealth;

import org.newdawn.slick.SpriteSheet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * when a soldier fires they shoot bullets
 * @author peculiaryak
 *
 */
public class Bullet extends Entity {
	private String direction;  // the direction the bullet should travel
	private Vector velocity;
	
	public Bullet(float x, float y, String direction) {
		super(x, y);
		this.direction = direction;
		this.velocity = new Vector(0f, 0f);
		SpriteSheet bulletSprites = ResourceManager.getSpriteSheet(StealthGame.SOLDIER_SRC, 60, 60);
		addImageWithBoundingBox(bulletSprites.getSubImage(188, 248, 10, 8));
	}
	
	
	/**
	 * set the velocity of the bullet
	 * @param v. The velocity to set.
	 */
	public void setVelocity(Vector v) {
		velocity = v;
	}
	
	/**
	 * velocity getter
	 * 
	 * @return Vector the velocity
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	
	/**
	 * shoots the bullet
	 */
	public void fire() {
		if (this.direction == "LEFT") {
			this.setVelocity(new Vector(-0.2f, 0f));
		} else {
			this.setVelocity(new Vector(0.2f, 0f));
		}
	}
	
	/**
	 * Constantly update the bullet with every frame
	 * @param delta
	 * @return True if the bullet collides with anything false otherwise
	 */
	public boolean update(int delta) {
		translate(velocity.scale(delta));
		
		return false;
	}
	
}
