package stealth;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

public class HighScoreState extends BasicGameState {
	private ArrayList<Integer> scores;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		FileStore.addHighScore(5000);
 		scores = FileStore.getHighScores();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString("High Scores", 490, 10);
		
		if (scores.size() == 0) {
			g.drawString("There are no high scores available currently.", 50, 50);
		} else {
			int yHeight = 20;
			int yInitial = 50;
			int loopSize = scores.size() > FileStore.TRACK_X_SCORES ? FileStore.TRACK_X_SCORES : scores.size();
			for (int i=0; i < loopSize; i++) {
				g.drawString((i + 1) + ". " + scores.get(i), 50, yInitial);
				yInitial += yHeight;
			}
		}
		
		g.drawImage(
				ResourceManager.getSpriteSheet(StealthGame.MENU_BUTTONS_SRC, 1000, 707).getSubImage(
						100, 400, 260, 300), 380, 500);
		g.drawString("Back", 490, 625);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			int mouseX = input.getMouseX();
			int mouseY = input.getMouseY();
			//  click on back button
			if ((mouseX > 424 && mouseX < 604) && (mouseY > 595 && mouseY < 685)) {
				game.enterState(StealthGame.MENU_STATE_ID);
			}
		}
	}

	@Override
	public int getID() {
		return StealthGame.SCORE_STATE_ID;
	}

}
