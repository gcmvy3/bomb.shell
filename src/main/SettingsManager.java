package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.newdawn.slick.SlickException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class SettingsManager implements Serializable
{
	private static final long serialVersionUID = 8445432275188685172L;
	
	private static boolean fullscreen = true;
	private static int width = 1920;
	private static int height = 1080;
	
	public static void saveSettings()
	{
		try
		{
			JsonWriter writer = new JsonWriter(new FileWriter("settings.json"));
			writer.beginObject();
			writer.name("fullscreen").value(fullscreen);
			writer.name("width").value(width);
			writer.name("height").value(height);
			writer.endObject();
			writer.close();
			
	        System.out.println("Settings saved to settings.cfg");
		}
		catch(IOException e)
		{
			System.err.println("Could not save settings");
		}
	}
	
	public static void loadSettings() throws IOException
	{
		JsonReader reader = new JsonReader(new FileReader("settings.json"));
		reader.beginObject();
		while(reader.hasNext())
		{
			String name = reader.nextName();
			
			switch(name)
			{
				case "fullscreen":
					fullscreen = reader.nextBoolean();
					break;
				case "width":
					width = reader.nextInt();
					break;
				case "height":
					height = reader.nextInt();
					break;
				default:
					System.out.println("WARNING: setting " + name + " is not recognized");
					break;
			}
		}
		reader.endObject();
		reader.close();
		System.out.println("Successfully loaded settings");
	}

	public static boolean isFullscreen() 
	{
		return fullscreen;
	}

	public static void setFullscreen(boolean fullscreen) 
	{
		SettingsManager.fullscreen = fullscreen;

		saveSettings();
	}

	public static int getWidth() 
	{
		return width;
	}

	public static void setWidth(int width) 
	{
		SettingsManager.width = width;
	}

	public static int getHeight() 
	{
		return height;
	}

	public static void setHeight(int height) 
	{
		SettingsManager.height = height;
	}
	
	public static void applySettings()
	{
		try 
		{
			BombshellGame.app.setDisplayMode(width, height, fullscreen);
		} 
		catch (SlickException e) 
		{
			System.err.println("Could not apply settings");
			e.printStackTrace();
		}
	}
}
