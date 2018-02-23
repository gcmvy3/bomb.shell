package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import main.Player;
import main.ResourceManager;

public class Leaderboard extends ListView<Player>
{
	TrueTypeFont font;
	
	public Leaderboard(GameContainer gt, int x, int y, int width, int height, int numPlayers) 
	{
		super(gt, x, y, width, height, numPlayers);
		
		font = ResourceManager.getFont("listItemFont");
	}
	
	@Override
	public void render(Graphics g)
	{
		ResourceManager.getGUISprite(SPRITE_NAME).draw(x, y, width, height);
		
		int textX = x + width / 10;
		int textY = y + listItemHeight / 2;
		
		for(ListItem<Player> li : listItems)
		{
			Player p = li.object;
			
			String message = String.format("%s %d %d", p.name, p.numKills, p.numDeaths);
			
			font.drawString(textX, textY, message, Color.black);
			
			textY += listItemHeight;
		}
	}

}
