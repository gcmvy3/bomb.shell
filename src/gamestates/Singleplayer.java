package gamestates;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
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
	Level level;
	
	ArrayList<Player> players;
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
			
			//Spawn the player
			Vec2 spawnPoint = level.getSpawnPoint();
			player = new Player(spawnPoint.x, spawnPoint.y, level);
			players = new ArrayList<Player>();
			players.add(player);
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
		level.render(g, 0, 0, gc.getWidth(), gc.getHeight());
		level.renderPlayers(g, players);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		level.update(gc);
		player.update(gc);
		
		//If the user presses 'escape', return to the main menu
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(1);
		}
	}

	@Override
	public int getID() 
	{
		return GameStates.SINGLEPLAYER;
	}

}