package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Multiplayer extends BasicGameState
{
	public static final int ID = 4;
	
	Level level;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException 
	{
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		System.out.println("Entering multiplayer");
		
		level = BomBoiGame.level;
		try 
		{
			level.init();
		} 
		catch (Exception e) 
		{
			//If the world cannot be loaded, return to the main menu
			game.enterState(1);
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		level.render(g, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		level.update(gc);
		
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(1);
		}
	}

	@Override
	public int getID() 
	{
		return Multiplayer.ID;
	}

}