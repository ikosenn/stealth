package stealth;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This is an arcade game called Stealth. The idea behind the game 
 * is to be able to sneak behind enemy lines without being spotted.
 * 
 * @author peculiaryak
 *
 */

public class StealthGame extends StateBasedGame {
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public StealthGame(String title, int width, int height) {
		
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;				
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {

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
