package main;

import java.io.File;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class World 
{
	public String name;
	private File path;
	private TiledMap tiledMap;
	
	public World(File path)
	{
		this.path = path;
		this.name = path.getName();
	}
	
	public void loadFromFile() throws SlickException
	{
			tiledMap = new TiledMap(path + "/tilemap.tmx");
	}
	
	public TiledMap getTiledMap() throws SlickException
	{
		if(tiledMap == null)
		{
			loadFromFile();
		}
		return tiledMap;
	}
}
