package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import self.totality.TotalityServer;
import self.totality.webSocketServer.controller.Button;
import self.totality.webSocketServer.controller.ControllerElement;
import self.totality.webSocketServer.controller.ControllerElementType;
import self.totality.webSocketServer.controller.Joystick;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DataListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class Multiplayer extends BasicGameState
{
	public static final int ID = 4;
	
	Level level;
	
	public static HashMap<UUID, Player> playerMap;
	
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
		
		playerMap = new HashMap<UUID, Player>();
		level.players = new ArrayList<Player>(playerMap.values());
		
		initTotality();
		TotalityServer.instance.start();
	}
	
	private void initTotality()
	{
		TotalityServer.instance.addControllerElement("button1", ControllerElementType.BUTTON, 0.1f, 0.1f, 0.2f, 0.2f);
		TotalityServer.instance.addControllerElement("joystick1", ControllerElementType.JOYSTICK, 0.6f, 0.3f, 0.4f, 0.4f);

		TotalityServer.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
				try
				{
					System.out.println("New player connecting!");
					
					Player newPlayer = level.newPlayer();
					
					int r = (int)(Math.random() * 255);
					int g = (int)(Math.random() * 200);
					int b = (int)(Math.random() * 255);
					
					newPlayer.color = new Color(r, g, b);
					
					playerMap.put(uuid, newPlayer);

					level.players = new ArrayList<Player>(playerMap.values());
				}
				catch(SlickException e)
				{
					System.err.println("Could not add player to game!");
				}
			}
		});

		TotalityServer.instance.addDisconnectListener(new DisconnectListener()
		{
			@Override
			public void onDisconnect(UUID uuid)
			{
				System.out.println("Disconnecting player!");
				
				Player p = playerMap.get(uuid);
				p.destroy();
				playerMap.remove(uuid);
				level.players = new ArrayList<Player>(playerMap.values());
			}
		});

		TotalityServer.instance.addDataListener(new DataListener()
		{
			@Override
			public void onDataUpdate(UUID uuid, ControllerElement e)
			{
				Player p = playerMap.get(uuid);

				if (e.type == ControllerElementType.JOYSTICK)
				{
					Joystick j = (Joystick) e;

					p.joystickX = j.getXVal();
					p.joystickY = j.getYVal();
				}
				else if (e.type == ControllerElementType.BUTTON)
				{
					Button b = (Button) e;

					if (b.id.equals("button1"))
					{
						p.dropBomb = true;
					}
				}
			}
		});
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		int x = BomBoiGame.WIDTH - level.getWidth() / 2;
		int y = BomBoiGame.HEIGHT - level.getHeight() / 2;
		
		level.render(g, x, y);
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