package gamestates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.Leaderboard;
import main.BomBoiGame;
import main.Level;
import main.Player;
import self.totality.Totality;
import self.totality.webSocketServer.PacketProcessor.ControllerElementProcessor.Listener;
import self.totality.webSocketServer.controller.Button;
import self.totality.webSocketServer.controller.DPad;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.controller.Text;
import self.totality.webSocketServer.controller.TextInput;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class Multiplayer extends BasicGameState
{
	public static final int RESPAWN_DELAY = 100;
	public static final float LEADERBOARD_RELATIVE_WIDTH = 0.2f;
	
	Level level;
	
	public static HashMap<UUID, String> pendingPlayers;
	
	public static HashMap<UUID, Player> playerMap;
	public static ArrayList<Player> playerList;
	
	GameController loginController;
	GameController gameController;
	
	Leaderboard leaderboard;
	
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
		
		pendingPlayers = new HashMap<UUID, String>();
		playerMap = new HashMap<UUID, Player>();
		playerList = new ArrayList<Player>(playerMap.values());
		
		initTotality();

		int listWidth = (int)(gc.getWidth() * LEADERBOARD_RELATIVE_WIDTH);
		leaderboard = new Leaderboard(gc, 0, 0, listWidth, gc.getHeight(), 20);
	}
	
	private void initTotality()
	{		
		loginController = new GameController();
		loginController.addControllerElement(new Text("usernamePrompt", 0.5f, 0.15f, "Type a username:", 32));
		loginController.addControllerElement(new TextInput("nameInput", 0.5f, 0.25f, 0.8f, 0.05f));
		loginController.addControllerElement(new Button("loginButton", 0.5f, 0.75f, 1.0f, 0.5f));

		Totality.instance.setDefaultController(loginController);
		
		gameController = new GameController();
		gameController.addControllerElement(new Button("bombButton", 0.5f, 0.25f, 1.0f, 0.5f));
		gameController.addControllerElement(new DPad("dpad1", 0.5f, 0.75f, 1.0f, 0.5f));
		
		Totality.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
				System.out.println("New player connecting!");
				pendingPlayers.put(uuid, "");
			}
		});

		Totality.instance.addDisconnectListener(new DisconnectListener()
		{
			@Override
			public void onDisconnect(UUID uuid)
			{
				System.out.println("Disconnecting player!");
				
				Player p = playerMap.get(uuid);
				if(p != null)
				{	
					p.destroy();
					playerMap.remove(uuid);
				}
				playerList = new ArrayList<Player>(playerMap.values());
			}
		});
		
		Totality.addDataListener(Button.TYPE, new Listener<Button.DataClass>()
		{
			@Override
			public void onData(UUID uuid, Button.DataClass data) 
			{
				Player p = playerMap.get(uuid);
				
				if (data.id.equals("bombButton"))
				{
					p.dropBomb = true;
				}
				else if(data.id.equals("loginButton"))
				{
					spawnPlayer(uuid);
				}
			}
		});
		
		Totality.addDataListener(DPad.TYPE, new Listener<DPad.DataClass>()
		{
			@Override
			public void onData(UUID uuid, DPad.DataClass data) 
			{
				Player p = playerMap.get(uuid);
				
				p.dPadDirection = data.direction;
			}
		});
		
		Totality.addDataListener(TextInput.TYPE, new Listener<TextInput.DataClass>()
		{
			@Override
			public void onData(UUID uuid, TextInput.DataClass data) 
			{
				pendingPlayers.put(uuid, data.text);
			}
		});
		
		Totality.instance.setWebPort(8080);
		Totality.instance.start();
		Totality.instance.startMulticastServer("bomboi");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		int levelX = leaderboard.width;
		int levelY = 0;
		
		level.render(g, levelX, levelY, gc.getWidth() - leaderboard.width, gc.getHeight());
		level.renderPlayers(g, playerList);
		
		leaderboard.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		level.update(gc);
		
		for(Player p : playerList)
		{
			p.update(gc);
			
			//Respawn the player
			if(!p.isActive() && p.timeSinceDeath >= RESPAWN_DELAY)
			{
				Vec2 spawnPoint = level.getSpawnPoint();
				p.respawn(spawnPoint.x, spawnPoint.y);
			}
		}
		
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(GameStates.MAIN_MENU);
		}
		
		leaderboard.setList(playerList);
	}
	
	public void spawnPlayer(UUID uuid)
	{
		try
		{
			System.out.println("Spawning player!");
			
			if(playerMap.get(uuid) == null)
			{
				//Spawn player on a spawn block
				Vec2 spawnLocation = level.getSpawnPoint();
				
				Player newPlayer = new Player(spawnLocation.x, spawnLocation.y, level);

				if(pendingPlayers.get(uuid) != null)
				{
					newPlayer.name = pendingPlayers.get(uuid);
					pendingPlayers.remove(uuid);
				}
				
				int r = (int)(Math.random() * 255);
				int g = (int)(Math.random() * 200);
				int blue = (int)(Math.random() * 255);
				
				newPlayer.color = new Color(r, g, blue);
				
				playerMap.put(uuid, newPlayer);

				playerList = new ArrayList<Player>(playerMap.values());
			}
			
			Totality.instance.sendControllerToPlayer(uuid, gameController);
		}
		catch(SlickException ex)
		{
			System.err.println("Could not add player to game!");
		}
	}

	@Override
	public int getID() 
	{
		return GameStates.MULTIPLAYER;
	}

}