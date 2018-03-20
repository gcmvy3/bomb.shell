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
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import gui.CustomButton;
import gui.Leaderboard;
import main.BomBoiGame;
import main.Level;
import main.Player;
import main.ResourceManager;
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
	
	public static ArrayList<Player> winners;
	
	public static int killLimit = 5;
	public static boolean gameOver = false;
	
	private CustomButton restartButton;
	private CustomButton quitButton;
	
	GameController loginController;
	GameController gameController;
	
	Leaderboard leaderboard;
	
	TrueTypeFont font;
	
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
		winners = new ArrayList<Player>();
		
		initButtons(gc, game);
		initTotality();

		font = ResourceManager.getFont(ResourceManager.GUI_FONT);
		
		int listWidth = (int)(gc.getWidth() * LEADERBOARD_RELATIVE_WIDTH);
		leaderboard = new Leaderboard(gc, 0, 0, listWidth, gc.getHeight() - font.getLineHeight());
	}
	
	private void initButtons(GameContainer gc, StateBasedGame game)
	{
		int buttonWidth = gc.getWidth() / 6;
		int buttonHeight = gc.getHeight() / 12;
		int buttonSpacing = (int)(buttonHeight * 1.5);
		
		restartButton = new CustomButton(gc,  
											gc.getWidth() / 2 - buttonWidth / 2,
											gc.getHeight() / 2,
											buttonWidth,
											buttonHeight,
											"Restart");
		restartButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				restartGame();
			}
		});
		
		quitButton = new CustomButton(gc,  
										gc.getWidth() / 2 - buttonWidth / 2,
										gc.getHeight() / 2 + buttonSpacing,
										buttonWidth,
										buttonHeight,
										"Quit");
		quitButton.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
					game.enterState(GameStates.MAIN_MENU);
			}
		});
	}
	
	private void initTotality()
	{		
		loginController = new GameController();
		loginController.addControllerElement(new Text("usernamePrompt", 0.5f, 0.15f, "Type a username:", 32));
		loginController.addControllerElement(new TextInput("nameInput", 0.5f, 0.25f, 0.8f, 0.05f));
		loginController.addControllerElement(new Button("loginButton", 0.5f, 0.75f, 1.0f, 0.5f));

		Totality.instance.setDefaultController(loginController);
		
		gameController = new GameController();
		gameController.addControllerElement(new Button("bombButton", 0.25f, 0.5f, 0.5f, 1.0f));
		gameController.addControllerElement(new DPad("dpad1", 0.75f, 0.5f, 0.45f, 0.9f));
		
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
		
		Totality.instance.start();
		Totality.instance.startMulticastServer("bomboi");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException 
	{
		if(!gameOver)
		{
			int levelX = leaderboard.width;
			int levelY = 0;
			
			level.render(g, levelX, levelY, gc.getWidth() - leaderboard.width, gc.getHeight());
			level.renderPlayers(g, playerList);
			
			leaderboard.render(g);
			
			font.drawString(0, gc.getHeight() - font.getLineHeight(), Totality.localIp, Color.white);
		}
		else
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			
			restartButton.render(gc, g);
			quitButton.render(gc, g);
			
			String endMessage = "";
			float messageWidth = 0;
			
			if(winners.size() > 0)
			{
				if(winners.size() == 1) //One winner
				{
					endMessage = winners.get(0).name + " wins!";
				}
				else //Multiple winners (in case of a draw)
				{
					endMessage = "Winners: ";
					for(int i = 0; i < winners.size(); i++)
					{
						Player w = winners.get(i);
						endMessage += w.name;
						
						if(i == winners.size() - 2)
						{
							endMessage += ", and ";
						}
						else if(i == winners.size() - 2)
						{
							endMessage += ", ";
						}
					}
				}
			}
			else
			{
				endMessage = "Something went wrong - let's call it a draw";
			}
			
			messageWidth = font.getWidth(endMessage);
			font.drawString(gc.getWidth() / 2 - messageWidth / 2, gc.getHeight() / 3, endMessage);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int arg2) throws SlickException 
	{
		if(!gameOver)
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
				
				if(p.numKills >= killLimit)
				{
					gameOver = true;
					winners.add(p);
				}
			}
			
			//Update the leaderboard
			leaderboard.setList(playerList);
			leaderboard.update();
		}

		//If escape is pressed, quit to main menu
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			game.enterState(GameStates.MAIN_MENU);
			Totality.instance.stop();
		}
	}
	
	public void restartGame()
	{
		
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