package main;

import org.newdawn.slick.Graphics;

public class World 
{
	public String name;
	private TileMap tileMap;
	private Tileset tileset;
	private int tileSize;
	
	public World(TileMap tileMap, Tileset tileset)
	{
		this.tileMap = tileMap;
		this.tileset = tileset;
		this.name = tileMap.name;
	}
	
	public void init() throws Exception
	{
		tileMap.init();
		tileset.init();
		
		if(tileMap.tileSize != tileset.tileSize)
		{
			System.err.println("Tile sizes do not match!");
			throw new Exception();
		}
		else
		{
			tileSize = tileMap.tileSize;
		}
	}

	public void render(Graphics g, int x, int y)
	{
		int numRows = tileMap.numRows;
		int numColumns = tileMap.numColumns;
		
		//Draw background
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				int tileID = tileMap.background[c][r];
				tileset.getTile(tileID).draw(x + tileSize * c, y + tileSize * r);
			}	
		}
		
		//Draw foreground
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				int tileID = tileMap.foreground[c][r];
				tileset.getTile(tileID).draw(x + tileSize * c, y + tileSize * r);
			}	
		}
	}
}
