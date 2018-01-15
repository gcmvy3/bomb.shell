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
	public TileMap tileMap;
	public Tileset tileset;
	
	private int tileSize;
	private int numRows;
	private int numColumns;
	
	private ArrayList<Player> players;
	
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
		
		initBoundaries();
		
		//Convert background and foreground data into Tiles
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				float x = (c * tileSize) + (tileSize / 2);
				float y = (r * tileSize) + (tileSize / 2);
				
				Tile bgTile = TileFactory.createTileById(x,
														y,
														r,
														c,
														tileSize,
														tileMap.background[c][r],
														this);
				backgroundArray[c][r] = bgTile;
				
				Tile fgTile = TileFactory.createTileById(x, 
										y,
										r,
										c,
										tileSize,
										tileMap.foreground[c][r],
										this);
				
				foregroundArray[c][r] = fgTile;
			}
		}
		
		//Add characters
		players = new ArrayList<Player>();
		
		Player player1 = new Player(100, 100, world);
		players.add(player1);
	}
	
	private void initBoundaries()
	{
		int thickness = 20;

		int worldWidth = tileSize * numColumns;
		int worldHeight = tileSize * numRows;
		
		int xMiddle = worldWidth / 2;
		int yMiddle = worldHeight / 2;
		
		LevelBoundary top = new LevelBoundary(xMiddle, - thickness / 2, worldWidth, thickness, world);
		LevelBoundary bottom = new LevelBoundary(xMiddle, worldHeight + thickness / 2, worldWidth, thickness, world);
		LevelBoundary left = new LevelBoundary(-thickness / 2, yMiddle, thickness, worldHeight, world);
		LevelBoundary right = new LevelBoundary(worldWidth + thickness / 2, yMiddle, thickness, worldHeight, world);
	}

	public void render(Graphics g, int x, int y)
	{
		for(int r = 0; r < numRows; r++)
		{
			for(int c = 0; c < numColumns; c++)
			{
				//Draw background
				int backgroundID = backgroundArray[c][r].id;
				
				if(backgroundID != 0)
				{
					backgroundArray[c][r].render(g);
				}
				
				//Draw foreground
				int foregroundID = foregroundArray[c][r].id;
				
				if(foregroundID != 0)
				{
					foregroundArray[c][r].render(g);
				}
			}	
		}
		
		for(Player p : players)
		{
			p.render();
		}
	}
	
	public void update(GameContainer gc)
	{
		world.step(TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION);
		
		for(Player p : players)
		{
			p.update(gc);
		}
	}
	
	public void setForegroundTile(int row, int column, Tile newTile)
	{
		foregroundArray[column][row] = newTile;
	}
}
