package stealth;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayState extends BasicGameState {
	
	World world;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		StealthGame sg = (StealthGame)game;
		world = new World(sg.getLevel());
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		world.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return StealthGame.PLAY_STATE_ID;
	}

}
