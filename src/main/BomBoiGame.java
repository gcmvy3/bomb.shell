package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class BomBoiGame extends StateBasedGame
{
    // Game state identifiers
    public static final int SPLASHSCREEN  = 0;
    public static final int MAIN_MENU     = 1;
    public static final int WORLD_SELECT  = 2;
    public static final int SINGLEPLAYER  = 3;
    public static final int MULTIPLAYER   = 4;

    // Application Properties
    public static final int WIDTH   = 1920;
    public static final int HEIGHT  = 1080;
    
    public static float scale = 4.0f;
    
    public static boolean multiplayer = false;
    public static World world;
	
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
        this.addState(new WorldSelect());
        this.addState(new Singleplayer());
        this.addState(new Multiplayer());
    }

    // Main Method
    public static void main(String[] args) 
    {
        try 
        {
            AppGameContainer app = new AppGameContainer(new ScalableGame(new BomBoiGame(), WIDTH, HEIGHT));
            app.setDisplayMode(WIDTH, HEIGHT, false);
            app.setShowFPS(true);
            app.setVSync(true);
            app.start();
        } 
        catch(SlickException e) 
        {
            e.printStackTrace();
        }
    }
}
