package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.BomBoiGame;
import main.SpriteManager;

public class MainMenu extends BasicGameState
{
	public static final int ID = 1;
	
	private int buttonWidth;
	private int buttonHeight;
	
	private Image singleplayerButtonImage;
	private Image multiplayerButtonImage;
	
	private MouseOverArea singleplayerButton;
	private MouseOverArea multiplayerButton;
	
	@Override
	public void init(GameContainer gt, StateBasedGame game) throws SlickException 
	{	
		initButtons(gt, game);
	}

	private void initButtons(GameContainer gt, StateBasedGame game) throws SlickException
	{
		singleplayerButtonImage = SpriteManager.getGUISprite("singleplayerButton");
		if(singleplayerButtonImage == null)
		{
			System.err.println("Could not load button sprite!");
		}
		
		multiplayerButtonImage = SpriteManager.getGUISprite("multiplayerButton");
		if(multiplayerButtonImage == null)
		{
			System.err.println("Could not load button sprite!");
		}
		
		buttonWidth = multiplayerButtonImage.getWidth();
		buttonHeight = multiplayerButtonImage.getHeight();
		
		singleplayerButton = new MouseOverArea(gt, 
												singleplayerButtonImage, 
												gt.getWidth() / 2 - buttonWidth / 2,
												gt.getHeight() / 2,
												buttonWidth,
												buttonHeight);
		singleplayerButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				game.enterState(2);
			}
		});
		
		
		multiplayerButton = new MouseOverArea(gt, 
												multiplayerButtonImage, 
												gt.getWidth() / 2 - buttonWidth / 2, 
												gt.getHeight() / 2 + (2 * buttonHeight), 
												buttonWidth, 
												buttonHeight);
		multiplayerButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				BomBoiGame.multiplayer = true;
				game.enterState(2);
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
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame game, int arg2) throws SlickException 
	{
	}

	@Override
	public int getID() 
	{
		return MainMenu.ID;
	}

}