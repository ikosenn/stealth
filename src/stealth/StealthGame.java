package stealth;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
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
 * Soldiers/ Guards resource courtesy of Master484
 * https://opengameart.org/content/space-soldier-m484-games
 * 
 * 
 * @author peculiaryak
 *
 */

public class StealthGame extends StateBasedGame {
	
	public final static int MENU_STATE_ID = 0;
	public final static int SCORE_STATE_ID = 1;
	public final static int PLAY_STATE_ID = 2;
	
	public final static String MENU_BUTTONS_SRC = "stealth/resources/menu_buttons.png";
	public final static String GAME_TITLE_SRC = "stealth/resources/stealth.png";
	public final static String ALARM_SRC = "stealth/resources/alarm.wav";
	public final static String SOLDIER_SRC = "stealth/resources/soldiers.png";
	
	public final static int SCREEN_WIDTH = 1024;
	public final static int SCREEN_HEIGHT = 800;
	
	private boolean isAlarmOn = false;
	private int level = 1;
	
	World world;
		
	public StealthGame(String title) {
		
		super(title);
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// add game states
		addState(new MenuState());
		addState(new HighScoreState());
		addState(new PlayState());
		
		// preload resources 
		ResourceManager.loadImage(MENU_BUTTONS_SRC);
		ResourceManager.loadImage(GAME_TITLE_SRC);
		ResourceManager.loadImage(SOLDIER_SRC);
		ResourceManager.loadSound(ALARM_SRC);
	}

	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new StealthGame("Stealth"));
			app.setDisplayMode(StealthGame.SCREEN_WIDTH, StealthGame.SCREEN_HEIGHT, false);
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
	
	/**
	 * 
	 * Level setter
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 
	 * Level getter
	 */
	public int getLevel() {
		return this.level;
	}
	
	/**
	 * create the world based on the current level
	 */
	public void createWorld() {
		world = new World(this.getLevel());
	}
	
}
