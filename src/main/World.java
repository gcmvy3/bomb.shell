package main;

public class World 
{
	public String name;
	private TileMap tileMap;
	private Tileset tileset;
	
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
	}

	public void render()
	{
		//TODO write render method for world
	}
}
