package stealth;



import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class PlayState extends BasicGameState {
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		StealthGame sg = (StealthGame)game;
		sg.createWorld();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		StealthGame sg = (StealthGame)game;
		sg.world.render(g);
		sg.treasureChest.render(g);
		for (int i = 0; i < sg.guards.size(); i++) {
			sg.guards.get(i).render(g);
		}
		sg.soldier.render(g);
		g.drawString("Level: " + sg.getLevel(),  15, 10);
		g.drawString("Life: " + sg.getLife(), 15, 40);
		g.drawString("Run: " + sg.getHeat() / 1000 + " seconds",  340, 10);
		g.drawString("Cooling down: " + sg.getCoolingDown() / 1000 + " seconds", 340, 40);
		g.drawString("Bullets: " + sg.soldier.getBulletCount(), 700, 10);
		
		g.drawString("PowerUp: " + sg.powerups.size(), 700, 40);
		g.drawString("Score: " + sg.getScore(), 920, 10);
		if (sg.getBullet() != null) {
			ArrayList<Bullet> fired = sg.getBullet();
			for (int i = 0; i < fired.size(); i++) {
				Bullet tempBullet = fired.get(i);
				tempBullet.render(g);
			}
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		StealthGame sg = (StealthGame)game;
		Input input = container.getInput();	
		sg.soldier.update(container, sg, delta);
		sg.alarm.update(sg);
		sg.treasureChest.update(sg);
		for (int i = 0; i < sg.guards.size(); i++) {
			sg.guards.get(i).update(sg, container);
		}
		
		if (sg.getHeat() > 0 && sg.isAlarmOn()) {
			sg.setHeat(delta);
		}
		
		if (sg.getCoolingDown() > 0 && !sg.isAlarmOn()) {
			sg.setCoolingDown(delta);
		}
		
		if (sg.getHeat() <= 0 && sg.isAlarmOn()) {
			sg.soundAlarm(false);
		}
		
		if (sg.getBullet() != null) {
			ArrayList<Bullet> fired = sg.getBullet();
			ArrayList<Bullet> toRemove = new ArrayList<>();
			boolean removeBullet;
			for (int i = 0; i < fired.size(); i++) {
				Bullet tempBullet = fired.get(i);
				removeBullet = tempBullet.update(delta);
				if (removeBullet) {
					toRemove.add(fired.get(i));
				}
			}
			if (toRemove.size() > 0) {
				for (int i = 0; i < fired.size(); i++) {
					sg.removeBullet(fired.get(i));
				}
			}
		}
		
		if (sg.powerups != null && sg.powerups.size() > 0) {
			for (int i = 0; i < sg.powerups.size(); i++) {
				sg.powerups.get(i).update(delta);
			}
		}
		
		for (int i = 0; i < sg.walls.size(); i++) {
			sg.walls.get(i).update(sg);
		}
		
		for (Iterator<Bullet> i = sg.bullets.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		for (Iterator<Guard> i = sg.guards.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		
		for (Iterator<PowerUp> i = sg.powerups.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		if (sg.getLife() <= 0) {
			game.enterState(StealthGame.GAMEOVERSTATE_ID);
		}
		
		if (sg.soldier.atGoalState(sg) && sg.getLevel() == StealthGame.MAX_LEVELS) {
			game.enterState(StealthGame.GAMEOVERSTATE_ID);
		} else if(sg.soldier.atGoalState(sg)) {
			sg.setLevel(sg.getLevel() + 1);
			game.enterState(StealthGame.PLAY_STATE_ID);
		}
		
		if (input.isKeyDown(Input.KEY_Z)) {
			sg.setLevel(2);
			game.enterState(StealthGame.PLAY_STATE_ID);
		}
		
		if (input.isKeyDown(Input.KEY_X)) {
			sg.setLevel(3);
			game.enterState(StealthGame.PLAY_STATE_ID);
		}
	}

	@Override
	public int getID() {
		return StealthGame.PLAY_STATE_ID;
	}

}
