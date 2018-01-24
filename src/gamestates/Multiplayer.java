package gamestates;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import main.BomBoiGame;
import main.Level;
import main.Player;
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
	public static ArrayList<Player> playerList;
	
	TrueTypeFont playerNameFont;
	
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
		playerList = new ArrayList<Player>(playerMap.values());
		
		initTotality();
		initFont();
	}
	
	private void initTotality()
	{
		TotalityServer.instance.addControllerElement("button1", ControllerElementType.BUTTON, 0.0f, 0.0f, 1.0f, 0.5f);
		TotalityServer.instance.addControllerElement("joystick1", ControllerElementType.JOYSTICK, 0.0f, 0.5f, 1.0f, 0.5f);

		TotalityServer.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
				try
				{
					System.out.println("New player connecting!");
					
					//TODO spawn player on a spawn block
					Player newPlayer = new Player(1, 1, level);
					
					newPlayer.name = "Anon";
					
					int r = (int)(Math.random() * 255);
					int g = (int)(Math.random() * 200);
					int b = (int)(Math.random() * 255);
					
					newPlayer.color = new Color(r, g, b);
					
					playerMap.put(uuid, newPlayer);

					playerList = new ArrayList<Player>(playerMap.values());
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
				playerList = new ArrayList<Player>(playerMap.values());
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
		
		TotalityServer.instance.start();
	}

	private void initFont()
	{
		Font f = new Font("Verdana", Font.BOLD, 32);
		playerNameFont = new TrueTypeFont(f, true);
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		int x = BomBoiGame.WIDTH - level.getWidth() / 2;
		int y = BomBoiGame.HEIGHT - level.getHeight() / 2;
		
		level.render(g, x, y);
		
		for(Player p : playerList)
		{
			if(p.active)
			{
				p.render(g);
				
				float nameLength = playerNameFont.getWidth(p.name);
				
				playerNameFont.drawString(p.getPixelsX() - nameLength / 2, p.getPixelsY(), p.name);
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		level.update(gc);
		
		for(Player p : playerList)
		{
			p.update(gc);
		}
		
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