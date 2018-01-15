package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Singleplayer extends BasicGameState
{
	public static final int ID = 3;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException 
	{
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		System.out.println("Entering singleplayer");
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException 
	{
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(1);
		}
	}

	@Override
	public int getID() 
	{
		return Singleplayer.ID;
	}

}