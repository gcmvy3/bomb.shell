package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.newdawn.slick.SlickException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class SettingsManager implements Serializable
{
	private static final long serialVersionUID = 8445432275188685172L;
	
	private static final String FILE_NAME = "settings.json";
	
	private static boolean fullscreen = true;
	private static int width = 1920;
	private static int height = 1080;
	
	public static void saveSettings()
	{
		try
		{
			JsonWriter writer = new JsonWriter(new FileWriter(FILE_NAME));
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
		boolean success = true;
		
		JsonParser parser = new JsonParser();
		
		JsonObject settingsObj = parser.parse(new FileReader(FILE_NAME)).getAsJsonObject();
		
		JsonElement fullscreenElement = settingsObj.get("fullscreen");
		if(fullscreenElement != null && fullscreenElement.isJsonPrimitive())
		{
			fullscreen = settingsObj.get("fullscreen").getAsBoolean();
		}
		else
		{
			success = false;
			System.err.println("WARNING: fullscreen setting is missing or invalid");
		}
		
		JsonElement widthElement = settingsObj.get("width");
		if(widthElement != null && widthElement.isJsonPrimitive())
		{
			width = settingsObj.get("width").getAsInt();
		}
		else
		{
			success = false;
			System.err.println("WARNING: width setting is missing or invalid");
		}
		
		JsonElement heightElement = settingsObj.get("height");
		if(heightElement != null && heightElement.isJsonPrimitive())
		{
			height = settingsObj.get("height").getAsInt();
		}
		else
		{
			success = false;
			System.err.println("WARNING: height setting is missing or invalid");
		}
		
		if(success)
		{
			System.out.println("Successfully loaded all settings");
		}
		else
		{
			System.err.println("Finished loading settings, but with errors");
		}
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
