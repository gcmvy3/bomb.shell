package main;

import java.util.ArrayList;
import java.util.Iterator;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Level 
{
	final float TIME_STEP = 1.0f / 60.0f;
	final int POSITION_ITERATION = 10;
	final int VELOCITY_ITERATION = 10;
	
	float pixelsPerMeter;
	
	public String name;
	public TileMap tileMap;
	public Tileset tileset;
	
	public float tileSizeInPixels;
	public float tileSizeInMeters;
	
	public int numRows;
	public int numColumns;
	
	public Tile[][] backgroundArray;
	public Tile[][] foregroundArray;
	
	public ArrayList<Player> players;
	
	public ArrayList<Bomb> bombs;
	
	public ArrayList<Explosion> explosions;
	
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
		
		tileSizeInPixels = tileMap.tileSize;
		pixelsPerMeter = tileSizeInPixels;
		tileSizeInMeters = pixelsToMeters(tileSizeInPixels);
		
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
				float x = (c * tileSizeInMeters) + (tileSizeInMeters / 2);
				float y = (r * tileSizeInMeters) + (tileSizeInMeters / 2);
				
				Tile bgTile = TileFactory.createTileById(x,
														y,
														r,
														c,
														tileSizeInMeters,
														tileMap.background[c][r],
														this);
				backgroundArray[c][r] = bgTile;
				
				Tile fgTile = TileFactory.createTileById(x, 
										y,
										r,
										c,
										tileSizeInMeters,
										tileMap.foreground[c][r],
										this);
				
				foregroundArray[c][r] = fgTile;
			}
		}
		
		//Add characters
		players = new ArrayList<Player>();
		
		bombs = new ArrayList<Bomb>();
		explosions = new ArrayList<Explosion>();
	}
	
	private void initBoundaries()
	{
		float thickness = 1f;

		float worldWidth = tileSizeInMeters * numColumns;
		float worldHeight = tileSizeInMeters * numRows;
		
		float xMiddle = worldWidth / 2;
		float yMiddle = worldHeight / 2;
		
		LevelBoundary top = new LevelBoundary(xMiddle, - thickness / 2, worldWidth, thickness, this);
		LevelBoundary bottom = new LevelBoundary(xMiddle, worldHeight + thickness / 2, worldWidth, thickness, this);
		LevelBoundary left = new LevelBoundary(-thickness / 2, yMiddle, thickness, worldHeight, this);
		LevelBoundary right = new LevelBoundary(worldWidth + thickness / 2, yMiddle, thickness, worldHeight, this);
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
		
		for(Bomb b : bombs)
		{
			b.render();
		}
		
		for(Player p : players)
		{
			p.render();
		}
		
		for(Explosion e : explosions)
		{
			e.render();
		}
	}
	
	public void update(GameContainer gc)
	{
		world.step(TIME_STEP, VELOCITY_ITERATION, POSITION_ITERATION);
		
		//Update bombs
		
		Iterator<Bomb> bombIter = bombs.iterator();
		while (bombIter.hasNext()) 
		{
		    Bomb b = bombIter.next();

			if(b.active)
			{
				b.update();
			}
			else
			{
				bombIter.remove();
			}
		}
		
		
		for(Player p : players)
		{
			p.update(gc);
		}
		
		Iterator<Explosion> explosionIter = explosions.iterator();
		while (explosionIter.hasNext()) 
		{
		    Explosion e = explosionIter.next();

			if(e.active)
			{
				e.update();
			}
			else
			{
				explosionIter.remove();
			}
		}
	}
	
	public void setForegroundTile(int row, int column, Tile newTile)
	{
		world.destroyBody(foregroundArray[column][row].body);
		foregroundArray[column][row] = newTile;
	}
	
	public float pixelsToMeters(float pixels)
	{
		return pixels / pixelsPerMeter;
	}
	
	public float metersToPixels(float meters)
	{
		return meters * pixelsPerMeter;
	}
	
	public Player newPlayer() throws SlickException
	{		
		
		return new Player(1, 1, this);
	}
}
