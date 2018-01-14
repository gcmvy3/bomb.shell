package main;

import java.io.File;
import java.util.ArrayList;

public class LevelFactory 
{
	private final static String TILEMAP_DIRECTORY = "assets/tilemaps";
	private final static String TILESET_DIRECTORY = "assets/tilesets";
	
	private static ArrayList<TileMap> tileMaps;
	private static ArrayList<Tileset> tilesets;
	
	//Checks the worlds directory for valid world files
	//Adds them to an arraylist, but does not load the tilemap into memory
	public static void refreshLists()
	{
		refreshTileMapList();
		refreshTilesetList();
	}
	
	private static void refreshTileMapList()
	{
		tileMaps = new ArrayList<TileMap>();
		
		File[] tileMapFolders = {};
		
		tileMapFolders = new File(TILEMAP_DIRECTORY).listFiles(File::isDirectory);
		
		System.out.println("Found " + tileMapFolders.length + " tilemaps");
		
		for(File f : tileMapFolders)
		{
			if(new File(f.getPath() + "/tilemap.tmx").exists())
			{
				TileMap tm = new TileMap(f);
				tileMaps.add(tm);
			}
			else
			{
				System.err.println("Could not find tilemap for: " + f.getName());
			}
		}
	}
	
	private static void refreshTilesetList()
	{
		tilesets = new ArrayList<Tileset>();
		
		File[] tilesetFolders = {};
		
		tilesetFolders = new File(TILESET_DIRECTORY).listFiles(File::isDirectory);
		
		System.out.println("Found " + tilesetFolders.length + " tilesets");
		
		for(File f : tilesetFolders)
		{
			if(new File(f.getPath() + "/tileset.tsx").exists())
			{
				Tileset ts = new Tileset(f);
				tilesets.add(ts);
			}
			else
			{
				System.err.println("Could not find tileset for: " + f.getName());
			}
		}
	}
	
	public static Level buildLevel(String tileMapName, String tilesetName) throws Exception
	{
		TileMap tileMap = null;
		Tileset tileset = null;
		
		for(TileMap tm : tileMaps)
		{
			if(tm.name.equals(tileMapName))
			{
				tileMap = tm;
			}
		}
		
		for(Tileset ts : tilesets)
		{
			if(ts.name.equals(tilesetName))
			{
				tileset = ts;
			}
		}
		
		//If the tilemap or tileset cannot be found, throw an exception
		if(tileMap == null || tileset == null)
		{
			throw new Exception();
		}
		
		return new Level(tileMap, tileset);
	}
}
