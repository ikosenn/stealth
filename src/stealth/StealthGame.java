package stealth;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


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
	private int heat = 0;
	private int coolingDown = 0;
	private int spotted = 0;
	private int life = 3;
	private int score = 0;
	
	World world;
	ArrayList<Guard> guards = new ArrayList<>();
	Alarm alarm = new Alarm();
	Soldier soldier;
		
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
		if (heat <= 0 && coolingDown <= 0 && state) {
			this.isAlarmOn = true;
			heat = 15000;
			coolingDown = 5000;
			spotted += 1;
		} else if(!state) {
			this.isAlarmOn = false;
		}
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
		Vector[][] patrolRoutes = this.world.getPatrolRoutes();
		for (int i = 0; i < patrolRoutes.length; i++) {
			Guard tempGuard = new Guard(patrolRoutes[i]);
			tempGuard.generatePatrolPaths(this.world);
			guards.add(tempGuard);
		}
		this.soldier = new Soldier();
	}
	
	/**
	 * Heat setter
	 * @param delta. The amount of time elapsed since calling update
	 */
	public void setHeat(int delta) {
		this.heat -= delta;
	}
	
	/**
	 * Cooling Down setter
	 * @param delta. The amount of time elapsed since calling update
	 */
	public void setCoolingDown(int delta) {
		this.coolingDown -= delta;
	}
	
	/**
	 * Heat getter
	 * @param The heat time. How long the guards will follow you.
	 */
	public int getHeat() {
		return this.heat; 	
	}
	
	/**
	 * Cooling Down getter
	 * @return. The cooling down time before you can be spotted again
	 */
	public int getCoolingDown() {
		return this.coolingDown;
	}
	
	/**
	 * Spotted getter
	 * @return. The number of times the player has been spotted
	 */
	public int getSpottedCount() {
		return this.spotted;
	}
	
	/**
	 * Spotted setter
	 * @param times. The number of times the player has been spotted.
	 * This can be used to reset the count to zero
	 */
	public void setSpottedCount(int times) {
		this.spotted = times;
	}
	
	/**
	 * Increments the score if a collision happens
	 */
	public void incrementScore(int score) {
		this.score += score;
	}
	
	/**
	 * reduce the life if the ball goes below the screen
	 */
	public void reduceLives() {
		this.life -= 1;
	}
	
	/**
	 * Player life getter
	 * @return the total number of lives left
	 */
	public int getLife() {
		return this.life;
	}
	
	/**
	 * The Player's score getter
	 * @return an integer that represents the Player's current score
	 */
	public int getScore() {
		return this.score;
	}

	
	
}
