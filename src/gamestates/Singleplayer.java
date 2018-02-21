package gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.BomBoiGame;
import main.Level;
import main.Player;

public class Singleplayer extends BasicGameState
{
	public static final int ID = 3;
	
	Level level;
	
	Player player;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException 
	{
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game)
	{
		System.out.println("Entering singleplayer");
		
		level = BomBoiGame.level;
		try 
		{
			level.init();

			player = new Player(0.5f, 0.5f, level);
		} 
		catch (Exception e) 
		{
			//If the world cannot be loaded, return to the main menu
			game.enterState(1);
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException 
	{
		int x = (int)((BomBoiGame.width / 2) - (level.getWidth() / 2));
		int y = (int)((BomBoiGame.height / 2) - (level.getHeight() / 2));
		
		level.render(g, x, y);
		
		player.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		level.update(gc);
		player.update(gc);
		
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