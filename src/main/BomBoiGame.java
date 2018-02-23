package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import gamestates.LevelSelect;
import gamestates.MainMenu;
import gamestates.Multiplayer;
import gamestates.Singleplayer;
import gamestates.SplashScreen;

public class BomBoiGame extends StateBasedGame
{
    // Default resolution (should be automatically changed)
    public static int width  = 1920;
    public static int height = 1080;
    
    public static boolean multiplayer = false;
    public static boolean reloaded = false;
    public static Level level;
	
	public BomBoiGame() 
	{
		super("BomBoi");
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
    }

    // Main Method
    public static void main(String[] args) 
    {
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	width = gd.getDisplayMode().getWidth();
    	height = gd.getDisplayMode().getHeight();
    	
    	setLWJGLNatives();
        try 
        {
        	ScalableGame scalableGame = new ScalableGame(new BomBoiGame(), width, height, true);
            AppGameContainer app = new AppGameContainer(scalableGame);
            app.setDisplayMode(width, height, true);
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
    	Class<BomBoiGame> c = BomBoiGame.class;

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


