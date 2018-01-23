package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.BomBoiGame;

public class SplashScreen extends BasicGameState
{
	//TODO make this state show a splashscreen
	
	public static final int ID = 0;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame game) throws SlickException 
	{
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
