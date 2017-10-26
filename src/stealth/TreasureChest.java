package stealth;

import java.util.Random;

import org.newdawn.slick.Image;

import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * Implements the treasure chest. This contains the powerups
 * @author peculiaryak
 *
 */
public class TreasureChest extends Entity {
	private boolean isOpen = false;
	private Image openChest;
	private Image closedChest;
	
	public TreasureChest(Vector pos) {
		super(pos.getX(), pos.getY());
		this.openChest = ResourceManager.getImage(StealthGame.CHEST_OPEN_SRC);
		this.closedChest = ResourceManager.getImage(StealthGame.CHEST_CLOSED_SRC);
		addImageWithBoundingBox(this.closedChest);
	}
	
	
	/**
	 * Checks if the soldier collides with the chest. 
	 * if he does then we should open it
	 * @param game. The current game state
	 */
	private void checkCollisionSoldier(StealthGame game) {
		Soldier soldier = game.soldier;
		Collision isPen = this.collides(soldier);
		if (isPen != null) {
			this.isOpen = true;
			Random random = new Random();
			if (random.nextBoolean()) {
				game.powerups.add(new PowerUp());
			}
		}
	}
	
	/**
	 * Make updates with every frame
	 * @param game. The current game state.
	 */
	public void update(StealthGame game) {
	
		if (this.isOpen) {
			removeImage(this.closedChest);
			addImageWithBoundingBox(this.openChest);
		} else {
			this.checkCollisionSoldier(game);
		}
	}
	
}
