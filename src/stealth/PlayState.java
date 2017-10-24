package stealth;



import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayState extends BasicGameState {
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		StealthGame sg = (StealthGame)game;
		sg.createWorld();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		StealthGame sg = (StealthGame)game;
		sg.world.render(g);
		for (int i = 0; i < sg.guards.size(); i++) {
			sg.guards.get(i).render(g);
		}
		sg.soldier.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		StealthGame sg = (StealthGame)game;
		sg.soldier.update(container);
		sg.alarm.update(sg);
		for (int i = 0; i < sg.guards.size(); i++) {
			sg.guards.get(i).update(sg);
		}
	}

	@Override
	public int getID() {
		return StealthGame.PLAY_STATE_ID;
	}

}
