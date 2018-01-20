package main;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SpriteManager 
{
	final static String SPRITES_DIRECTORY = "assets/sprites/";
	
	static HashMap<String, Image> sprites = new HashMap<String, Image>();

	static boolean initialized = false;
	
	private static void loadSprites()
	{
		initialized = true;
		
		File[] spriteFiles = new File(SPRITES_DIRECTORY).listFiles();

		for(int i = 0; i < spriteFiles.length; i++)
		{
			try 
			{
				//Strip extension from filename
				String fileName = spriteFiles[i].getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0 && pos < (fileName.length() - 1)) 
				{
				    fileName = fileName.substring(0, pos);
				}
				
				Image image = new Image(spriteFiles[i].getPath());
				sprites.put(fileName, image);
			} 
			catch (SlickException e) 
			{
				
				e.printStackTrace();
			}
		}
	}
	
	public static Image getSprite(String id)
	{
		if(!initialized)
		{
			loadSprites();
		}
		
		return sprites.get(id);
	}
}
