package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelSelect extends BasicGameState
{
	public static final int ID = 2;
	
	private Level selectedLevel = null;
	
	private Image startButtonImage;
	
	private MouseOverArea startButton;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException 
	{
		startButtonImage = new Image("/assets/gui/startButton.png");
		
		int buttonWidth = startButtonImage.getWidth();
		int buttonHeight = startButtonImage.getHeight();
		
		startButton = new MouseOverArea(gc,
										startButtonImage,
										gc.getWidth() / 2 - buttonWidth / 2,
										gc.getHeight() / 2,
										buttonWidth,
										buttonHeight);
		startButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				if(selectedLevel != null)
				{
					BomBoiGame.level = selectedLevel;
					if(BomBoiGame.multiplayer)
					{
						game.enterState(4);
					}
					else
					{
						game.enterState(3);
					}
				}
			}
		});
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		LevelFactory.refreshLists();
		try 
		{
			selectedLevel = LevelFactory.buildLevel("default", "default");
		} 
		catch (Exception e) 
		{
			game.enterState(1);
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		startButton.render(gc, g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException 
	{
		
	}

	@Override
	public int getID() 
	{
		return LevelSelect.ID;
	}

}