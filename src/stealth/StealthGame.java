package stealth;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;


/**
 * This is an arcade game called Stealth. The idea behind the game 
 * is to be able to sneak behind enemy lines without being spotted.
 * 
 * 
 * Alarm sound resource courtesy of jovaiaudio
 * http://freesound.org/people/jovaiaudio/sounds/255242/
 * 
 * Menu buttons resource courtesy of verique
 * https://opengameart.org/content/fantasy-buttons-0
 * 
 * 
 * @author peculiaryak
 *
 */

public class StealthGame extends StateBasedGame {
	
	public final static int MENU_STATE_ID = 0;
	public final static int SCORE_STATE_ID = 1;
	public static final String MENU_BUTTONS_SRC = "stealth/resources/menu_buttons.png";
	public static final String GAME_TITLE_SRC = "stealth/resources/stealth.png";
	public static final String ALARM_SRC = "stealth/resources/alarm.wav";
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	private boolean isAlarmOn = false;
		
	public StealthGame(String title, int width, int height) {
		
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;		
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// add game states
		addState(new MenuState());
		addState(new HighScoreState());
		
		// preload resources 
		ResourceManager.loadImage(MENU_BUTTONS_SRC);
		ResourceManager.loadImage(GAME_TITLE_SRC);
		ResourceManager.loadSound(ALARM_SRC);
	}

	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new StealthGame("Stealth", 1024, 800));
			app.setDisplayMode(1024, 800, false);
			app.setShowFPS(false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether the alarm is on
	 * @return true if alarm is on otherwise false
	 */
	public boolean isAlarmOn() {
		return isAlarmOn;
	}

	
	/*
	 * set the state of the alarm 
	 * 
	 * @param state. The state to set the alarm
	 */
	public void soundAlarm(boolean state) {
		this.isAlarmOn = state;
	}
	
}
