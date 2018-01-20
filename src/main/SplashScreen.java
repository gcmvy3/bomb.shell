package main;

import java.io.OutputStream;
import java.io.PrintStream;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SplashScreen extends BasicGameState
{
	//TODO make this state show a splashscreen
	
	public static final int ID = 0;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame game) throws SlickException 
	{
    	System.setOut(new TommyOut(System.out));
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		gc.getGraphics().scale(BomBoiGame.scale, BomBoiGame.scale);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame game, Graphics arg2) throws SlickException 
	{
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame game, int arg2) throws SlickException 
	{
		game.enterState(1);
	}

	@Override
	public int getID() 
	{
		return SplashScreen.ID;
	}

}

class TommyOut extends PrintStream
{

	public TommyOut(OutputStream out) {
		super(out);
	}

	@Override
	public void println(String x)
	{
		super.println(x + " Tommy was here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
}
