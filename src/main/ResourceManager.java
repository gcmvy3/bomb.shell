package main;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ResourceManager 
{
	private final static String ANIMATIONS_DIRECTORY = "assets/animations";
	private final static String SPRITES_DIRECTORY = "assets/sprites/";
	private final static String GUI_SPRITES_DIRECTORY = "assets/gui/";
	
	private static HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();
	private static HashMap<String, Image> sprites = new HashMap<String, Image>();
	private static HashMap<String, Image> guiSprites = new HashMap<String, Image>();

	public final static int LIST_ITEM_FONT = 0;
	public final static int PLAYER_NAME_FONT = 1;
	public final static int BUTTON_FONT = 2;
	public final static int GUI_FONT = 3;
	
	static boolean initialized = false;
	
	public static void initialize()
	{
		initialized = true;
		loadAnimations();
		loadSprites();
		loadGUISprites();
		loadFonts();
	}
	
	private static Animation createAnimationFromFile(File file)
	{
		Animation animation = null;
		File animationDirectory = file.getParentFile();
		String name = animationDirectory.getName();
		int duration = 0;
		boolean loop = true;
		
		try 
		{
			JsonParser parser = new JsonParser();
			
			JsonObject dataObj = parser.parse(new FileReader(file)).getAsJsonObject();

			JsonElement durationElement = dataObj.get("duration");
			if(durationElement != null && durationElement.isJsonPrimitive())
			{
				duration = dataObj.get("duration").getAsInt();
			}
			else
			{
				System.err.println("WARNING: duration not specified for animation " + name);
			}
			
			JsonElement loopElement = dataObj.get("loop");
			if(loopElement != null && loopElement.isJsonPrimitive())
			{
				loop = dataObj.get("loop").getAsBoolean();
			}
			
			if(dataObj.has("frames") && dataObj.get("frames").isJsonArray()) 
			{	
				//Load the file names of the frames from the data.json file
				JsonArray jsonArray = dataObj.getAsJsonArray("frames");
				ArrayList<String> fileNames = new ArrayList<String>();
				for(int i = 0; i < jsonArray.size(); i++)
				{
					JsonElement element = jsonArray.get(i);
					if(element.isJsonPrimitive())
					{
						fileNames.add(element.getAsString());
					}
				}
				
				//Load the image files into Image objects
				Image[] frames = new Image[fileNames.size()];
				for(int i = 0; i < frames.length; i++)
				{
					String imgFilePath = animationDirectory.getPath() + File.separator + fileNames.get(i);
					try
					{
						frames[i] = new Image(imgFilePath);
					}
					catch(SlickException e)
					{
						System.err.println("ERROR: Could not load frames for animation " + name);
						return null;
					}
				}
				
				animation = new Animation(frames, duration);
			}
			else if(dataObj.has("spritesheet") && dataObj.get("spritesheet").isJsonObject())
			{
				JsonObject spritesheetObj = dataObj.getAsJsonObject("spritesheet");
				
				String spriteSheetPath;
				int tileWidth;
				int tileHeight;
				
				JsonElement nameElement = spritesheetObj.get("name");
				JsonElement widthElement = spritesheetObj.get("tileWidth");
				JsonElement heightElement = spritesheetObj.get("tileHeight");
				
				if(nameElement != null 
						&& widthElement != null 
						&& heightElement != null
						&& nameElement.isJsonPrimitive() 
						&& widthElement.isJsonPrimitive() 
						&& heightElement.isJsonPrimitive())
				{
					spriteSheetPath = animationDirectory + File.separator + nameElement.getAsString();
					tileWidth = widthElement.getAsInt();
					tileHeight = heightElement.getAsInt();
					
					try 
					{
						SpriteSheet spriteSheet = new SpriteSheet(spriteSheetPath, tileWidth, tileHeight);
						animation = new Animation(spriteSheet, duration);
					} 
					catch (SlickException e) 
					{
						System.err.println("ERROR: Could not load spritesheet for animation " + name);
						return null;
					}
				}
				else
				{
					System.err.println("ERROR: missing spritesheet data for animation " + name);
					return null;
				}
			}
			else
			{
				System.err.println("ERROR: animation " + name + " is missing image data and could not be loaded");
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("ERROR: data.json file is missing for animation " + name);
			return null;
		}
		
		animation.setLooping(loop);
		return animation;
	}
	
	private static void loadAnimations() 
	{
		File[] directories = new File(ANIMATIONS_DIRECTORY).listFiles();

		if(directories != null)
		{
			for(int i = 0; i < directories.length; i++)
			{
				File directory = directories[i];
				if(directory.isDirectory())
				{
					String name = directory.getName();
					
					String dataFilePath = directory.getAbsolutePath() + File.separator + "data.json";
					File dataFile = new File(dataFilePath);
					if(dataFile.exists())
					{
						Animation animation = createAnimationFromFile(dataFile);
						animations.put(name, animation);
					}
					else
					{
						System.err.println("WARNING: Animation " + name + " could not be loaded (could not find data.json file)");
					}
				}
			}
		}
		else
		{
			System.err.println("ERROR: Animations directory not found");
		}
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
	}
	
	private static void loadGUISprites() 
	{
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
	
	public static Animation getAnimation(String name)
	{
		if(!initialized)
		{
			initialize();
		}
		
		return animations.get(name);
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
