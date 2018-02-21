package main;

import java.awt.Font;
import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class ResourceManager 
{
	final static String SPRITES_DIRECTORY = "assets/sprites/";
	final static String GUI_SPRITES_DIRECTORY = "assets/gui/";
	
	static HashMap<String, Image> sprites = new HashMap<String, Image>();
	static HashMap<String, Image> guiSprites = new HashMap<String, Image>();
	static HashMap<String, TrueTypeFont> fonts = new HashMap<String, TrueTypeFont>();
	
	static boolean initialized = false;
	
	public static void initialize()
	{
		initialized = true;
		loadSprites();
		loadFonts();
	}
	
	private static void loadFonts()
	{
		Font f = new Font("Verdana", Font.BOLD, 32);
		TrueTypeFont listItemFont = new TrueTypeFont(f, true);
		fonts.put("listItemFont", listItemFont);
		
		f = new Font("Verdana", Font.BOLD, 28);
		TrueTypeFont playerNameFont = new TrueTypeFont(f, true);
		fonts.put("playerNameFont", playerNameFont);
	}
	
	private static void loadSprites()
	{
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
		
		File[] guiSpriteFiles = new File(GUI_SPRITES_DIRECTORY).listFiles();

		for(int i = 0; i < guiSpriteFiles.length; i++)
		{
			try 
			{
				//Strip extension from filename
				String fileName = guiSpriteFiles[i].getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0 && pos < (fileName.length() - 1)) 
				{
				    fileName = fileName.substring(0, pos);
				}
				
				Image image = new Image(guiSpriteFiles[i].getPath());
				guiSprites.put(fileName, image);
			} 
			catch (SlickException e) 
			{
				
				e.printStackTrace();
			}
		}
	}
	
	public static TrueTypeFont getFont(String id)
	{
		if(!initialized)
		{
			initialize();
		}
		
		return fonts.get(id);
	}
	
	public static Image getSprite(String id)
	{
		if(!initialized)
		{
			initialize();
		}
		
		return sprites.get(id);
	}
	
	public static Image getGUISprite(String id)
	{
		if(!initialized)
		{
			initialize();
		}
		
		return guiSprites.get(id);
	}
}
