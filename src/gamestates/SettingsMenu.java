package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.TextButton;
import gui.DropDown;
import main.Resolution;
import main.ResourceManager;
import main.SettingsManager;
import self.totality.Totality;

public class SettingsMenu extends BasicGameState
{
	private final float topOffset = 0.2f;
	private final float REL_BUTTON_WIDTH = 0.16f;
	private final float REL_BUTTON_HEIGHT = 0.08f;
	private final float REL_CENTER_GAP = 0.01f;
	
	String totalityIP;
	
	TextButton fullscreenOn;
	TextButton fullscreenOff;
	TextButton backButton;
	TextButton applyButton;
	
	DropDown<Resolution> resolutionMenu;
	
	TrueTypeFont labelFont;
	
	int buttonWidth = 0;
	int buttonHeight = 0;
	int rowHeight = 0;
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
		buttonHeight = (int) (gc.getHeight() * REL_BUTTON_HEIGHT);
		rowHeight = labelFont.getHeight();
		centerGap = (int) (gc.getWidth() * REL_CENTER_GAP);
		
		fullscreenOn = new TextButton(gc, gc.getWidth() / 2, gc.getHeight() / 2, buttonWidth / 4, rowHeight);
		fullscreenOn.setLabel("On");
		fullscreenOn.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				fullscreenOn.disable();
				fullscreenOff.enable();
				SettingsManager.setFullscreen(true);
			}
		});
		
		fullscreenOff = new TextButton(gc, gc.getWidth() / 2, gc.getHeight() / 2, buttonWidth / 4, rowHeight);
		fullscreenOff.setLabel("Off");
		fullscreenOff.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				fullscreenOn.enable();
				fullscreenOff.disable();
				SettingsManager.setFullscreen(false);
			}
		});
		
		if(SettingsManager.isFullscreen())
		{
			fullscreenOn.disable();
		}
		else
		{
			fullscreenOff.disable();
		}
		
		resolutionMenu = new DropDown<Resolution>(gc, 1,1, buttonWidth, rowHeight);
		resolutionMenu.addItem(new Resolution(1920, 1080));
		
		backButton = new TextButton(gc, gc.getWidth() / 2, gc.getHeight() / 2, buttonWidth, buttonHeight);
		backButton.setLabel("Back");
		backButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				game.enterState(GameStates.MAIN_MENU);
			}
		});
		
		applyButton = new TextButton(gc, gc.getWidth() / 2, gc.getHeight() / 2, buttonWidth, buttonHeight);
		applyButton.setLabel("Apply");
		applyButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				SettingsManager.applySettings();
			}
		});
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException 
	{
		//The menu elements are positioned at render time
		//So that the menu doesn't break if the resolution is changed
		
		//Top of settings menu
		int rowY = (int) (gc.getHeight() * topOffset);
		
		//Fullscreen setting
		int labelWidth = labelFont.getWidth("Fullscreen:");
		labelFont.drawString(gc.getWidth() / 2 - labelWidth - centerGap, rowY, "Fullscreen:");
		fullscreenOn.setX(gc.getWidth() / 2 + centerGap);
		fullscreenOn.setY(rowY);
		fullscreenOn.render(gc, g);
		fullscreenOff.setX(gc.getWidth() / 2 + centerGap + fullscreenOn.getWidth());
		fullscreenOff.setY(rowY);
		fullscreenOff.render(gc, g);
		
		//Resolution settings
		rowY += labelFont.getHeight() * 2;
		labelWidth = labelFont.getWidth("Resolution:");
		labelFont.drawString(gc.getWidth() / 2 - labelWidth - centerGap, rowY, "Resolution:");
		resolutionMenu.setLocation(gc.getWidth() / 2 + centerGap, rowY);
		resolutionMenu.setDimensions(buttonWidth, rowHeight);
		resolutionMenu.render(gc, g);
		
		//Totality IP
		rowY += labelFont.getHeight() * 2;
		labelWidth = labelFont.getWidth("Totality IP:");
		labelFont.drawString(gc.getWidth() / 2 - labelWidth - centerGap, rowY, "Totality IP:");
		labelWidth = labelFont.getWidth(totalityIP);
		labelFont.drawString(gc.getWidth() / 2 + centerGap,  rowY, totalityIP);
		
		//Cancel and apply buttons
		rowY += buttonHeight;
		backButton.setX(gc.getWidth() / 2 - backButton.getWidth() - centerGap);
		backButton.setY(rowY);
		backButton.render(gc, g);
		applyButton.setX(gc.getWidth() / 2 + centerGap);
		applyButton.setY(rowY);
		applyButton.render(gc, g);
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

