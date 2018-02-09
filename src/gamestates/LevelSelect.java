package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.ListView;
import main.BomBoiGame;
import main.Level;
import main.LevelFactory;
import tiles.TileMap;

public class LevelSelect extends BasicGameState
{
	public static final int ID = 2;
	
	private Level selectedLevel = null;
	
	private Image startButtonImage;
	
	private MouseOverArea startButton;
	
	ListView<TileMap> tileMapList;
	
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
				try 
				{
					selectedLevel = LevelFactory.buildLevel(tileMapList.getCurrentSelection(), LevelFactory.tilesets.get(0));
				} 
				catch (Exception e) 
				{
					game.enterState(1);
					e.printStackTrace();
				}
				
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
		
		tileMapList = new ListView<TileMap>(gc, 0, 0, gc.getWidth() / 8, gc.getHeight(), 10);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		LevelFactory.refreshLists();
		tileMapList.setList(LevelFactory.tileMaps);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		startButton.render(gc, g);
		tileMapList.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		//If escape is pressed, return to the main menu
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(1);
		}
	}

	@Override
	public int getID() 
	{
		return LevelSelect.ID;
	}

}