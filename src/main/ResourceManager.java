package main;

import java.awt.Font;
import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class ResourceManager 
{
	private final static String SPRITES_DIRECTORY = "assets/sprites/";
	private final static String GUI_SPRITES_DIRECTORY = "assets/gui/";
	
	private static HashMap<String, Image> sprites = new HashMap<String, Image>();
	private static HashMap<String, Image> guiSprites = new HashMap<String, Image>();
	private static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	public final static int LIST_ITEM_FONT = 0;
	public final static int PLAYER_NAME_FONT = 1;
	public final static int BUTTON_FONT = 2;
	public final static int GUI_FONT = 3;
	
	static boolean initialized = false;
	
	public static void initialize()
	{
		initialized = true;
		loadSprites();
		loadFonts();
	}
	
	private static void loadFonts()
	{
		Font f = new Font("Courier New", Font.PLAIN, 32);
		TrueTypeFont listItemFont = new TrueTypeFont(f, true);
		fonts.put(LIST_ITEM_FONT, listItemFont);
		
		f = new Font("Verdana", Font.PLAIN, 28);
		TrueTypeFont playerNameFont = new TrueTypeFont(f, true);
		fonts.put(PLAYER_NAME_FONT, playerNameFont);
		
		f = new Font("Courier New", Font.PLAIN, 28);
		TrueTypeFont buttonFont = new TrueTypeFont(f, true);
		fonts.put(BUTTON_FONT, buttonFont);
		
		f = new Font("Courier New", Font.PLAIN, 28);
		TrueTypeFont guiFont = new TrueTypeFont(f, true);
		fonts.put(GUI_FONT, guiFont);
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
	
	public static TrueTypeFont getFont(int id)
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
