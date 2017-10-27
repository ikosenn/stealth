package stealth;

import org.newdawn.slick.Sound;

import jig.ResourceManager;

/**
 * implements the invisibility powerup
 * @author peculiaryak
 *
 */
public class PowerUp {
	private int duration = 5000;
	private boolean inUse = false;
	private Sound horror_sound = ResourceManager.getSound(StealthGame.HORROR_PULSATING_SRC);
	
	/**
	 * Use this to set if the power up is in use.
	 */
	public void setInUse() {
		this.inUse = true;
	}

	/**
	 * Checks of the powerup is still active 
	 * @return. The current state of the power up
	 */
	public boolean isActive() {
		if (this.duration <= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * update the value of the powerup
	 * @param delta. Time elapsed since last update
	 */
	public void update(int delta) {
		if (this.inUse) {
			duration -= delta;
		}
		
		// play sound while its in use
		if (this.inUse && !this.horror_sound.playing()) {
			this.horror_sound.play();
		} else if (!this.isActive() && this.horror_sound.playing()) {
			this.horror_sound.stop();
		}
	}
}
