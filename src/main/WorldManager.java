package main;

import java.io.File;
import java.util.ArrayList;

public class WorldManager 
{
	private final static String WORLDS_DIRECTORY = "assets/worlds";
	
	private static ArrayList<World> worlds;
	
	//Checks the worlds directory for valid world files
	//Adds them to an arraylist, but does not load the tilemap into memory
	public static void refreshList()
	{
		worlds = new ArrayList<World>();
		
		File[] worldFolders = {};
		
		worldFolders = new File(WORLDS_DIRECTORY).listFiles(File::isDirectory);
		
		System.err.println("Found " + worldFolders.length + " worlds");
		
		for(File f : worldFolders)
		{
			if(new File(f.getPath() + "/tilemap.tmx").exists())
			{
				World newWorld = new World(f);
				worlds.add(newWorld);
			}
			else
			{
				System.err.println("Could not find tilemap for world: " + f.getName());
			}
		}
	}
	
	public static World getWorld(String name)
	{
		for(World w : worlds)
		{
			if(w.name.equals(name))
			{
				return w;
			}
		}
		return null;
	}
}
