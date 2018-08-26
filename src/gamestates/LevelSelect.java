package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.TextButton;
import gui.ListView;
import main.BombshellGame;
import main.Level;
import main.LevelFactory;
import main.ResourceManager;
import tiles.TileMap;
import tiles.Tileset;

public class LevelSelect extends BasicGameState
{
	final float TILEMAP_LIST_WIDTH = 0.2f;
	final float TILESET_LIST_WIDTH = 0.2f;
	
	final float THUMBNAIL_RELATIVE_WIDTH = 0.4f;
	final float THUMBNAIL_RELATIVE_HEIGHT = 0.4f;
	
	private int thumbnailWidth;
	private int thumbnailHeight;
	
	private int thumbnailX;
	private int thumbnailY;
	
	private boolean thumbnailDirty = true;
	
	private TileMap selectedTileMap = null;
	private Tileset selectedTileset = null;
	
	private Level selectedLevel = null;
	
	private Image thumbnail;
	
	private TextButton startButton;
	
	ListView<TileMap> tileMapList;
	ListView<Tileset> tilesetList;
	
	TrueTypeFont font;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException 
	{
		int buttonWidth = gc.getWidth() / 5;
		int buttonHeight = gc.getHeight() / 12;
		
		startButton = new TextButton(gc,
										gc.getWidth() / 2 - buttonWidth / 2,
										gc.getHeight() - gc.getHeight() / 10,
										buttonWidth,
										buttonHeight,
										"Start");
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
					BombshellGame.level = selectedLevel;
					if(BombshellGame.versus)
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
		
		font = ResourceManager.getFont(ResourceManager.GUI_FONT);
		
		thumbnailWidth = (int)(gc.getWidth() * THUMBNAIL_RELATIVE_WIDTH);
		thumbnailHeight = (int)(gc.getHeight() * THUMBNAIL_RELATIVE_HEIGHT);
		
		thumbnailX = gc.getWidth() / 2 - thumbnailWidth / 2;
		thumbnailY = gc.getHeight() / 2 - thumbnailHeight / 2;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		LevelFactory.refreshLists();
		tileMapList.setList(LevelFactory.tileMaps);
		tilesetList.setList(LevelFactory.tilesets);
		
		selectedTileMap = tileMapList.getCurrentSelection();
		selectedTileset = tilesetList.getCurrentSelection();
		
		updateThumbnail();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		startButton.render(gc, g);
		tileMapList.render(gc, g);
		tilesetList.render(gc, g);
		
		if(thumbnail != null)
		{
			thumbnail.draw(thumbnailX, thumbnailY);
		}
		
		float textX = gc.getWidth() / 2 - font.getWidth("TileMap: " + selectedTileMap.name) / 2;
		float textY = gc.getHeight() - gc.getHeight() / 7;
		font.drawString(textX, textY, "TileMap: " + selectedTileMap.name);
		
		textX = gc.getWidth() / 2 - font.getWidth("Tileset: " + selectedTileset.name) / 2;
		textY -= font.getLineHeight();
		font.drawString(textX, textY, "Tileset: " + selectedTileset.name);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		//If a new tilemap or tileset has been selected, update the thumbnail
		if(selectedTileMap != tileMapList.getCurrentSelection())
		{
			selectedTileMap = tileMapList.getCurrentSelection();
			thumbnailDirty = true;
		}
		if(selectedTileset != tilesetList.getCurrentSelection())
		{
			selectedTileset = tilesetList.getCurrentSelection();
			thumbnailDirty = true;
		}
		
		if(thumbnailDirty)
		{
			updateThumbnail();
		}
		
		//If escape is pressed, return to the main menu
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(GameStates.MAIN_MENU);
		}
	}
	
	/**
	 * Try and generate a thumbnail using the current tilemap and tileset
	 * If it fails, load the default thumbnail
	 */
	private void updateThumbnail()
	{
		try
		{
			thumbnail = LevelFactory.buildThumbnail(selectedTileMap, selectedTileset, thumbnailWidth, thumbnailHeight);
		}
		catch(Exception e)
		{
			thumbnail = ResourceManager.getGUISprite("defaultThumbnail").getScaledCopy(thumbnailWidth, thumbnailHeight);
		}
		thumbnailDirty = false;
	}

	@Override
	public int getID() 
	{
		return GameStates.LEVEL_SELECT;
	}

}