package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.CustomButton;
import main.ResourceManager;
import self.totality.Totality;

public class SettingsMenu extends BasicGameState
{
	private final float topOffset = 0.2f;
	private final float REL_BUTTON_WIDTH = 0.05f;
	private final float REL_CENTER_GAP = 0.01f;
	
	String totalityIP;
	
	CustomButton fullscreenToggle;
	
	TrueTypeFont labelFont;
	
	int buttonWidth = 0;
	int centerGap = 0;
	
	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException 
	{
		labelFont = ResourceManager.getFont(ResourceManager.GUI_FONT);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game) 
	{
		totalityIP = Totality.localIp;
		
		buttonWidth = (int) (gc.getWidth() * REL_BUTTON_WIDTH);
		centerGap = (int) (gc.getWidth() * REL_CENTER_GAP);
		
		fullscreenToggle = new CustomButton(gc, gc.getWidth() / 2, gc.getHeight() / 2, buttonWidth, labelFont.getHeight());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException 
	{
		//Top of settings menu
		int rowY = (int) (gc.getHeight() * topOffset);
		
		//Fullscreen setting
		int labelWidth = labelFont.getWidth("Fullscreen:");
		labelFont.drawString(gc.getWidth() / 2 - labelWidth - centerGap, rowY, "Fullscreen:");
		fullscreenToggle.setX(gc.getWidth() / 2 + centerGap);
		fullscreenToggle.setY(rowY);
		fullscreenToggle.render(gc, g);
		
		//Resolution settings
		rowY += labelFont.getHeight() * 2;
		labelWidth = labelFont.getWidth("Resolution:");
		labelFont.drawString(gc.getWidth() / 2 - labelWidth - centerGap, rowY, "Resolution:");
		
		//Totality IP
		rowY += labelFont.getHeight() * 2;
		labelWidth = labelFont.getWidth("Totality IP:");
		labelFont.drawString(gc.getWidth() / 2 - labelWidth - centerGap, rowY, "Totality IP:");
		labelWidth = labelFont.getWidth(totalityIP);
		labelFont.drawString(gc.getWidth() / 2 + centerGap,  rowY, totalityIP);
		
		
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

