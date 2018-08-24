package gamestates;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.CustomButton;
import main.BombshellGame;
import main.ResourceManager;

public class MainMenu extends BasicGameState
{
	private int buttonWidth;
	private int buttonHeight;
	
	private int buttonSpacing;
	
	private int titleWidth;
	private int titleHeight;
	
	private double relTitleWidth = 0.6;

	private CustomButton singleplayerButton;
	private CustomButton multiplayerButton;
	private CustomButton settingsButton;
	private CustomButton exitButton;
	
	Image titleArt;
	
	Animation titleAnimation;
	
	@Override
	public void init(GameContainer gt, StateBasedGame game) throws SlickException 
	{
		titleArt = ResourceManager.getGUISprite("titleArt");
		
		initButtons(gt, game);
		
		titleAnimation = ResourceManager.getAnimation("title");
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
												"Co-op");
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
												"Versus");
		multiplayerButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				BombshellGame.versus = true;
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
		//Scale title animation while maintaining aspect ratio
		titleWidth = (int)(gc.getWidth() * relTitleWidth);
		titleHeight = (int)(titleAnimation.getHeight() * titleWidth / titleAnimation.getWidth());
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException 
	{
		int titleY = gc.getHeight() / 2 - titleHeight;
		int titleX = gc.getWidth() / 2 - titleWidth / 2;
		
		titleAnimation.draw(titleX, titleY, titleWidth, titleHeight);
		
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