package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Multiplayer extends BasicGameState
{
	public static final int ID = 4;
	
	World world;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException 
	{
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		System.out.println("Entering multiplayer");
		
		world = BomBoiGame.world;
		try 
		{
			world.init();
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
		world.render(g, 0, 0);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException 
	{
		
	}

	@Override
	public int getID() 
	{
		return Multiplayer.ID;
	}

}