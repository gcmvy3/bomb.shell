package main;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Level 
{
	final float TIME_STEP = 1.0f / 60.0f;
	final int POSITION_ITERATION = 8;
	final int VELOCITY_ITERATION = 8;
	
	public String name;
	private TileMap tileMap;
	private Tileset tileset;
	
	private int tileSize;
	private int numRows;
	private int numColumns;
	
	private ArrayList<Character> characters;
	
	private Tile[][] backgroundArray;
	private Tile[][] foregroundArray;
	
	public World world; //Box2d world for physics
	
	public Level(TileMap tileMap, Tileset tileset)
	{
		this.tileMap = tileMap;
		this.tileset = tileset;
		this.name = tileMap.name;
		
		//Create the physics world
		Vec2 gravity = new Vec2(0, 0);
		world = new World(gravity);
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
		
		tileSize = tileMap.tileSize;
		numColumns = tileMap.numColumns;
		numRows = tileMap.numRows;
		backgroundArray = new Tile[numColumns][numRows];
		foregroundArray = new Tile[numColumns][numRows];
		
		//Convert background and foreground data into Tiles
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				float x = (c * tileSize) + (tileSize / 2);
				float y = (r * tileSize) + (tileSize / 2);
				
				Tile bgTile = TileFactory.createTileById(x,
														y, 
														tileSize,
														tileMap.background[c][r],
														world);
				backgroundArray[c][r] = bgTile;
				
				Tile fgTile = TileFactory.createTileById(x, 
										y, 
										tileSize,
										tileMap.foreground[c][r],
										world);
				
				foregroundArray[c][r] = fgTile;
			}
		}
		
		//Add characters
		characters = new ArrayList<Character>();
		
		Character character1 = new Character(100, 100, world);
		characters.add(character1);
	}

	public void render(Graphics g, int x, int y)
	{
		//Draw background
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				int tileID = backgroundArray[c][r].id;
				
				tileset.getTile(tileID).draw(x + tileSize * c, y + tileSize * r);
			}	
		}
		
		//Draw foreground
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				int tileID = foregroundArray[c][r].id;
				tileset.getTile(tileID).draw(x + tileSize * c, y + tileSize * r);
			}	
		}
		
		for(Character c : characters)
		{
			c.render();
		}
	}
	
	public void update(GameContainer gc)
	{
		world.step(TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION);
		
		for(Character c : characters)
		{
			c.update(gc);
		}
	}
}
