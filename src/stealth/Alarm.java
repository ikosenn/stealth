package stealth;

import org.newdawn.slick.Sound;

import jig.ResourceManager;

/**
 * Implements the alarm sounds
 * 
 * @author peculiaryak
 *
 */
public class Alarm {
	private Sound alarm;
	
	public Alarm () {
		this.alarm = ResourceManager.getSound(StealthGame.ALARM_SRC);
	}
	
	
	/**
	 * Checks if the alarm is already on. If its not already on it plays the alarm.
	 */
	public void update(StealthGame game) {
		if (game.isAlarmOn() && !this.alarm.playing()) {
			this.alarm.play();
		} else if (!game.isAlarmOn() && this.alarm.playing()) {
			this.alarm.stop();
		}
	}
}
