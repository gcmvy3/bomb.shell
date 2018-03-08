package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.CustomButton;
import main.BomBoiGame;

public class MainMenu extends BasicGameState
{
	private int buttonWidth;
	private int buttonHeight;
	
	private int buttonSpacing;
	
	private CustomButton singleplayerButton;
	private CustomButton multiplayerButton;
	private CustomButton settingsButton;
	private CustomButton exitButton;
	
	@Override
	public void init(GameContainer gt, StateBasedGame game) throws SlickException 
	{			
		initButtons(gt, game);
	}

	private void initButtons(GameContainer gc, StateBasedGame game) throws SlickException
	{
		buttonWidth = gc.getWidth() / 6;
		buttonHeight = gc.getHeight() / 12;
		buttonSpacing = (int)(buttonHeight * 1.5);
		
		singleplayerButton = new CustomButton(gc,  
												gc.getWidth() / 2 - buttonWidth / 2,
												gc.getHeight() / 2,
												buttonWidth,
												buttonHeight,
												"Singleplayer");
		singleplayerButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				game.enterState(GameStates.LEVEL_SELECT);
			}
		});
		
		
		multiplayerButton = new CustomButton(gc, 
												gc.getWidth() / 2 - buttonWidth / 2, 
												gc.getHeight() / 2 + buttonSpacing, 
												buttonWidth, 
												buttonHeight,
												"Multiplayer");
		multiplayerButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				BomBoiGame.multiplayer = true;
				game.enterState(GameStates.LEVEL_SELECT);
			}
		});
		
		settingsButton = new CustomButton(gc, 
											gc.getWidth() / 2 - buttonWidth / 2,
											gc.getHeight() / 2 + (2 * buttonSpacing),
											buttonWidth,
											buttonHeight,
											"Settings");
		
		settingsButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				game.enterState(GameStates.SETTINGS_MENU);
			}
		});
		
		exitButton = new CustomButton(gc, 
										gc.getWidth() / 2 - buttonWidth / 2,
										gc.getHeight() / 2 + (3 * buttonSpacing),
										buttonWidth,
										buttonHeight,
										"Exit");

		exitButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				gc.exit();
			}
		});
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException 
	{
		singleplayerButton.render(gc, g);
		multiplayerButton.render(gc, g);
		settingsButton.render(gc, g);
		exitButton.render(gc, g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame game, int arg2) throws SlickException 
	{
	}

	@Override
	public int getID() 
	{
		return GameStates.MAIN_MENU;
	}

}