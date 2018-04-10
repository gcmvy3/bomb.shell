package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import gamestates.LevelSelect;
import gamestates.MainMenu;
import gamestates.Multiplayer;
import gamestates.SettingsMenu;
import gamestates.Singleplayer;
import gamestates.SplashScreen;

public class BombshellGame extends StateBasedGame
{
	//These will be loaded from memory
    public static boolean fullscreen;
    public static int width;
    public static int height;
    
    public static boolean multiplayer = false;
    public static boolean reloaded = false;
    public static Level level;
	
	public BombshellGame() 
	{
		super("bomb.shell");
	}

    // Initialize game states (calls init method of each gamestate, and set's the state ID)
    public void initStatesList(GameContainer gc) throws SlickException 
    {
        // The first state added will be the one that is loaded first, when the application is launched
        this.addState(new SplashScreen());	//State 0
        this.addState(new MainMenu());		//State 1
        this.addState(new LevelSelect());   //State 2
        this.addState(new Singleplayer());  //State 3
        this.addState(new Multiplayer());   //State 4
        this.addState(new SettingsMenu());  //State 5
    }

    // Main Method
    public static void main(String[] args) 
    {
    	try
    	{
    		SettingManager.loadSettings();
    		fullscreen = SettingManager.isFullscreen();
    		width = SettingManager.getWidth();
    		height = SettingManager.getHeight();
    	}
    	catch(IOException e)
    	{
    		System.err.println("Could not load settings from memory");
    		
    		fullscreen = true;
    		
        	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        	width = gd.getDisplayMode().getWidth();
        	height = gd.getDisplayMode().getHeight();
        	
        	SettingManager.setFullscreen(fullscreen);
        	SettingManager.setWidth(width);
        	SettingManager.setHeight(height);
    	}
    
    	setLWJGLNatives();
        try 
        {
        	ScalableGame scalableGame = new ScalableGame(new BombshellGame(), width, height, true);
            AppGameContainer app = new AppGameContainer(scalableGame);
            app.setDisplayMode(width, height, SettingManager.isFullscreen());
            app.setVSync(true);

            app.start();
        }
        catch(SlickException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void setLWJGLNatives()
    {
    	Class<BombshellGame> c = BombshellGame.class;

		System.out.println(c.getResource(c.getName() + ".class"));
    	
    	String currentDirectory = new File("native").getAbsolutePath();
    	String nativesPath = currentDirectory + File.separator + getOSName();
    	System.setProperty("org.lwjgl.librarypath", nativesPath);
    }
    
    public static String getOSName()
    {
    	String system = System.getProperty("os.name").toLowerCase();
    	
        if(system.startsWith("win"))
        {
            return "windows";
        }

        if(system.startsWith("mac"))
        {
            return "macosx";
        }

        if(system.startsWith("lin"))
        {
            return "linux";
        }

        if(system.startsWith("sol"))
        {
            return "solaris";
        }
        return "unknown";
    }
}


