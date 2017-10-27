package stealth;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;


/**
 * This state is active when the Game is over. In this state, the ball is
 * a gameover banner is displayed. A timer automatically transitions back to the Menu State.
 * 
 * Transitions From PlayState
 * 
 * Transitions To MenuState
 */
class GameOverState extends BasicGameState {
	

	private int timer;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		StealthGame sg = (StealthGame)game;
		FileStore.addHighScore(sg.getScore());
		sg.setLevel(1);  // reset the levels
		sg.setLife();
		timer = 4000;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		g.drawImage(ResourceManager.getImage(StealthGame.GAMEOVER_BANNER_RSC), 380,
				350);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
		
		timer -= delta;
		if (timer <= 0)
			game.enterState(StealthGame.MENU_STATE_ID, new EmptyTransition(), new HorizontalSplitTransition() );

	}

	@Override
	public int getID() {
		return StealthGame.GAMEOVERSTATE_ID;
	}
	
}