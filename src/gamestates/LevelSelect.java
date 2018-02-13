package gamestates;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
import tiles.Tileset;

public class LevelSelect extends BasicGameState
{
	public static final int ID = 2;
	
	final float TILEMAP_LIST_WIDTH = 0.2f;
	final float TILESET_LIST_WIDTH = 0.2f;
	
	private TileMap selectedTileMap = null;
	private Tileset selectedTileset = null;
	
	private Level selectedLevel = null;
	
	private Image startButtonImage;
	
	private MouseOverArea startButton;
	
	ListView<TileMap> tileMapList;
	ListView<Tileset> tilesetList;
	
	TrueTypeFont font;
	
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
					selectedLevel = LevelFactory.buildLevel(selectedTileMap, selectedTileset);
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
		
		int listWidth = (int)(gc.getWidth() * TILEMAP_LIST_WIDTH);
		tileMapList = new ListView<TileMap>(gc, 0, 0, listWidth, gc.getHeight(), 10);
		
		int listWidth2 = (int)(gc.getWidth() * TILESET_LIST_WIDTH);
		tilesetList = new ListView<Tileset>(gc, gc.getWidth() - listWidth2, 0, listWidth2, gc.getHeight(), 10);
		
		Font f = new Font("Verdana", Font.BOLD, 32);
		font = new TrueTypeFont(f, true);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		LevelFactory.refreshLists();
		tileMapList.setList(LevelFactory.tileMaps);
		tilesetList.setList(LevelFactory.tilesets);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		startButton.render(gc, g);
		tileMapList.render(g);
		tilesetList.render(g);
		
		int textX = gc.getWidth() / 2 - font.getWidth("TileMap: " + selectedTileMap.name) / 2;
		int textY = gc.getHeight() / 2 - 100;
		font.drawString(textX, textY, "TileMap: " + selectedTileMap.name);
		
		textX = gc.getWidth() / 2 - font.getWidth("Tileset: " + selectedTileset.name) / 2;
		textY -= font.getLineHeight() * 2;
		font.drawString(textX, textY, "Tileset: " + selectedTileset.name);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		selectedTileMap = tileMapList.getCurrentSelection();
		selectedTileset = tilesetList.getCurrentSelection();
		
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