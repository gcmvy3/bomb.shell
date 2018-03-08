package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.ResourceManager;

public class SettingsMenu extends BasicGameState
{
	private final float topOffset = 0.2f;
	
	TrueTypeFont labelFont;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException 
	{
		labelFont = ResourceManager.getFont(ResourceManager.GUI_FONT);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics arg2) throws SlickException 
	{
		int fullscreenWidth = labelFont.getWidth("Fullscreen:");
		labelFont.drawString(gc.getWidth() / 2 - fullscreenWidth, gc.getWidth() * topOffset, "Fullscreen:");
		
		labelFont.drawString(gc.getWidth() / 2, gc.getHeight() / 2, "Totality IP: ");
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		//If escape is pressed, return to the main menu
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(GameStates.MAIN_MENU);
		}
	}

	@Override
	public int getID() 
	{
		return GameStates.SETTINGS_MENU;
	}

}

