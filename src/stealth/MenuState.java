package stealth;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

/**
 * Implements the menu screen. The menu screen will have the option to 
 * play the game, view the high scores, and exit the game
 * 
 * Menu buttons resource courtesy of verique
 * https://opengameart.org/content/fantasy-buttons-0
 * 
 * @author peculiaryak
 *
 */


public class MenuState extends BasicGameState {
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		g.drawImage(ResourceManager.getImage(StealthGame.GAME_TITLE_SRC), 340, 10);
		g.drawImage(
			ResourceManager.getSpriteSheet(StealthGame.MENU_BUTTONS_SRC, 1000, 707).getSubImage(
					100, 400, 260, 300), 380, 100);
		g.drawImage(
				ResourceManager.getSpriteSheet(StealthGame.MENU_BUTTONS_SRC, 1000, 707).getSubImage(
						100, 400, 260, 300), 380, 300);
		g.drawImage(
				ResourceManager.getSpriteSheet(StealthGame.MENU_BUTTONS_SRC, 1000, 707).getSubImage(
						100, 400, 260, 300), 380, 500);
		g.drawString("Play", 490, 225);
		g.drawString("High Scores", 465, 425);
		g.drawString("Exit", 490, 625);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			int mouseX = input.getMouseX();
			int mouseY = input.getMouseY();
			
			// play button
			if ((mouseX > 424 && mouseX < 604) && (mouseY > 197 && mouseY < 283)) {
				game.enterState(StealthGame.PLAY_STATE_ID);
			}
			// high score
			if ((mouseX > 424 && mouseX < 604) && (mouseY > 397 && mouseY < 486)) {
				game.enterState(StealthGame.SCORE_STATE_ID);
			}
			//  click on exit button
			if ((mouseX > 424 && mouseX < 604) && (mouseY > 595 && mouseY < 685)) {
				System.exit(0);
			}
		}
		
	}

	@Override
	public int getID() {
		return StealthGame.MENU_STATE_ID;
	}

}
