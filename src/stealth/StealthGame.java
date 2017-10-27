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
 * Gun shot sound courtesy of Xenonn
 * http://freesound.org/people/Xenonn/sounds/128301/
 * 
 * Empty gun shot sound courtesy of PhreaKsAccount
 * http://freesound.org/people/PhreaKsAccount/sounds/46265/
 * 
 * Powerup sound courtesty of unfa
 * http://freesound.org/people/unfa/sounds/256332/
 * 
 * @author peculiaryak
 *
 */

public class StealthGame extends StateBasedGame {
	
	public final static int MENU_STATE_ID = 0;
	public final static int SCORE_STATE_ID = 1;
	public final static int PLAY_STATE_ID = 2;
	public static final int GAMEOVERSTATE_ID = 3;
	
	public final static String MENU_BUTTONS_SRC = "stealth/resources/menu_buttons.png";
	public final static String GAME_TITLE_SRC = "stealth/resources/stealth.png";
	public final static String ALARM_SRC = "stealth/resources/alarm.wav";
	public final static String SOLDIER_SRC = "stealth/resources/soldiers.png";
	public final static String GUN_SHOT_SRC = "stealth/resources/gunshot.wav";
	public final static String GUN_EMPTY_SRC = "stealth/resources/blanks.wav";
	public final static String CHEST_OPEN_SRC = "stealth/resources/chest_open.png";
	public final static String CHEST_CLOSED_SRC = "stealth/resources/chest_closed.png";
	public static final String GAMEOVER_BANNER_RSC = "stealth/resources/gameover.png";
	public final static String HORROR_PULSATING_SRC = "stealth/resources/horror_p.wav";
	
	public final static int SCREEN_WIDTH = 1024;
	public final static int SCREEN_HEIGHT = 800;
	public static final int MAX_LEVELS = 2;
	public static final int MAX_LIVES = 3;
	
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
	ArrayList<Bullet> bullets = new ArrayList<>();
	ArrayList<Wall> walls = new ArrayList<>();
	ArrayList<PowerUp> powerups = new ArrayList<>();
	TreasureChest treasureChest;
		
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
		addState(new GameOverState());
		
		// preload resources 
		ResourceManager.loadImage(MENU_BUTTONS_SRC);
		ResourceManager.loadImage(CHEST_CLOSED_SRC);
		ResourceManager.loadImage(CHEST_OPEN_SRC);
		ResourceManager.loadImage(GAME_TITLE_SRC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(SOLDIER_SRC);
		ResourceManager.loadSound(ALARM_SRC);
		ResourceManager.loadSound(GUN_SHOT_SRC);
		ResourceManager.loadSound(GUN_EMPTY_SRC);
		ResourceManager.loadSound(HORROR_PULSATING_SRC);
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
	
	
	/**
	 * Checks for an active powerup
	 * @return true if there is an active powerup
	 */
	private boolean activePowerUp( ) {
		if (this.powerups != null && this.powerups.size() > 0) {
			for (int i = 0; i < this.powerups.size(); i++) {
				if (this.powerups.get(i).isActive()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * set the state of the alarm 
	 * 
	 * @param state. The state to set the alarm
	 */
	public void soundAlarm(boolean state) {
		if (heat <= 0 && coolingDown <= 0 && state && !this.activePowerUp()) {
			this.isAlarmOn = true;
			heat = 10000;
			coolingDown = 5000;
			spotted += 1;
		} else if(!state) {
			this.isAlarmOn = false;
		}
	}
	/**
	 * reset the counters to zero. Useful when the soldier is killed
	 */
	public void resetCounter() {
		this.heat = 0;
		this.coolingDown = 0;
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
		guards = new ArrayList<>();
		world = new World(this.getLevel());
		walls = new ArrayList<>();
		Vector[][] patrolRoutes = this.world.getPatrolRoutes();
		for (int i = 0; i < patrolRoutes.length; i++) {
			Guard tempGuard = new Guard(patrolRoutes[i]);
			tempGuard.generatePatrolPaths(this.world);
			guards.add(tempGuard);
		}
		this.soldier = new Soldier(
			world.getStartPos(), world.getGoalPos(), patrolRoutes.length - 1);
		// create walls
		Node[][] tileNodes = world.getNodes();
		for (int i = 0; i < tileNodes.length; i++) {
			for (int j = 0; j < tileNodes[0].length; j++) {
				if (tileNodes[i][j].isBlocked()) {
					walls.add(new Wall(tileNodes[i][j].getCenter()));
				}
			}
		}
		this.treasureChest = new TreasureChest(this.world.getTreasureChestPos());
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
	 * 
	 * Life setter
	 */
	public void setLife() {
		this.life = MAX_LIVES;
	}
	
	/**
	 * The Player's score getter
	 * @return an integer that represents the Player's current score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Contains the bullets fired by the soldier
	 * @return ArrayList containing the bullets otherwise return null.
	 */
	public ArrayList<Bullet> getBullet() {
		if (this.bullets != null && this.bullets.size() > 0) {
			return this.bullets;
		}
		return null;
	}

	/**
	 * removes the bullet from the bullet arraylist
	 * @param bullet. The bullet to remove
	 */
	public void removeBullet(Bullet bullet) {
		this.bullets.remove(bullet);
	}
	
}
