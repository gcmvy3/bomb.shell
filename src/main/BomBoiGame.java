package main;

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
    // Game state identifiers
    public static final int SPLASHSCREEN  = 0;
    public static final int MAIN_MENU     = 1;
    public static final int LEVEL_SELECT  = 2;
    public static final int SINGLEPLAYER  = 3;
    public static final int MULTIPLAYER   = 4;

    // Application Properties
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
        this.addState(new SplashScreen());
        this.addState(new MainMenu());
        this.addState(new LevelSelect());
        this.addState(new Singleplayer());
        this.addState(new Multiplayer());
    }

    // Main Method
    public static void main(String[] args) 
    {
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
}


