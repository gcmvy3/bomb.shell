package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SettingManager implements Serializable
{
	private static final long serialVersionUID = 8445432275188685172L;
	
	private static boolean fullscreen = true;
	private static int width = 1920;
	private static int height = 1080;
	
	public static void saveSettings()
	{
		try
		{
	        FileOutputStream fileOut = new FileOutputStream("settings.cfg");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeBoolean(fullscreen);
	        out.writeInt(width);
	        out.writeInt(height);
	        out.close();
	        fileOut.close();
	        System.out.printf("Settings saved to settings.cfg");
		}
		catch(IOException e)
		{
			System.err.println("Could not save settings");
		}
	}
	
	public static void loadSettings() throws IOException
	{
         FileInputStream fileIn = new FileInputStream("settings.cfg");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         fullscreen = in.readBoolean();
         width = in.readInt();
         height = in.readInt();
         in.close();
         fileIn.close();
	}

	public static boolean isFullscreen() 
	{
		return fullscreen;
	}

	public static void setFullscreen(boolean fullscreen) 
	{
		SettingManager.fullscreen = fullscreen;

		saveSettings();
	}

	public static int getWidth() 
	{
		return width;
	}

	public static void setWidth(int width) 
	{
		SettingManager.width = width;
	}

	public static int getHeight() 
	{
		return height;
	}

	public static void setHeight(int height) 
	{
		SettingManager.height = height;
	}
}
