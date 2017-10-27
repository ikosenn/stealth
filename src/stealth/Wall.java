package stealth;
import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.Vector;

/**
 * Allows us to check for collisions easily with the wall structure
 * @author peculiaryak
 *
 */
public class Wall extends Entity {
	public Wall(Vector center) {
		super(center.getX(), center.getY());
		addShape(new ConvexPolygon(32f, 32f));
	}
	
	/**
	 *  Check for collision between the entity and the wall
	 *  
	 *  @param entity. The entity to check collision with
	 *  @return boolean. True if a collision occurred. This is used to remove bullets
	 */
	public boolean checkCollision(Entity entity) {
		Collision isPen = this.collides(entity);
		if (isPen != null) {
			Vector penetration = isPen.getMinPenetration();			
			entity.setX(entity.getX() - (penetration.getX() * 5));
			entity.setY(entity.getY() - (penetration.getY() * 5));
			return true;
		} 
		return false;
	}
	
	public void update(StealthGame game) {
		boolean isCollide;
		// bullet collision
		if (game.bullets != null && game.bullets.size() > 0) {
			for (int i = 0; i < game.bullets.size(); i++) {
				isCollide = this.checkCollision(game.bullets.get(i));
				if (isCollide) {
					game.bullets.get(i).setActive(false);
				}
			}
		}
		
		// soldier
		this.checkCollision(game.soldier);
	}
}
