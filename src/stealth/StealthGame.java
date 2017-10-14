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
 * @author peculiaryak
 *
 */

public class StealthGame extends StateBasedGame {
	
	public final static int MENU_STATE_ID = 1;
	public static final String MENU_BUTTONS_SRC = "stealth/resources/menu_buttons.png";
	public static final String GAME_TITLE_SRC = "stealth/resources/stealth.png";
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public StealthGame(String title, int width, int height) {
		
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;				
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// add game states
		addState(new MenuState());
		
		// preload resources 
		ResourceManager.loadImage(MENU_BUTTONS_SRC);
		ResourceManager.loadImage(GAME_TITLE_SRC);
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

}
