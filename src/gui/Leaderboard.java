package gui;

import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import main.Player;
import main.ResourceManager;

public class Leaderboard extends ListView<Player>
{
	private final float COLUMN_WIDTH = 0.16f;
	private final float HEADER_HEIGHT = 0.1f;
	private final String HEADER_SPRITE_NAME = "leaderboardHeader";
	private final String NAME_LABEL = "Name";
	private final String KILLS_LABEL = "K";
	private final String DEATHS_LABEL = "D";
	
	TrueTypeFont font;
	
	int headerHeight;
	int columnWidth;
	int nameColumnWidth;
	
	public Leaderboard(GameContainer gt, int x, int y, int width, int height) 
	{
		super(gt, x, y, width, height);
		
		headerHeight = (int)(HEADER_HEIGHT * height);
		columnWidth =  (int)(COLUMN_WIDTH * width);
		nameColumnWidth = width - columnWidth * 2;
		
		font = ResourceManager.getFont(ResourceManager.LIST_ITEM_FONT);
	}
	
	public Leaderboard(GameContainer gt, int x, int y, int width, int height, int numPlayers) 
	{
		super(gt, x, y, width, height, numPlayers);
		
		headerHeight = (int)(HEADER_HEIGHT * height);
		columnWidth =  (int)(COLUMN_WIDTH * width);
		nameColumnWidth = width - columnWidth * 2;
		
		font = ResourceManager.getFont(ResourceManager.LIST_ITEM_FONT);
	}
	
	public void update()
	{
		Collections.sort(listItems);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g)
	{
		//Draw background sprites
		ResourceManager.getGUISprite(HEADER_SPRITE_NAME).draw(x, y, width, headerHeight);	
		ResourceManager.getGUISprite(SPRITE_NAME).draw(x, y + headerHeight, width, height - headerHeight);
		
		g.setColor(Color.white);
		
		//Draw lines dividing the columns
		float dividerX = x + nameColumnWidth;
		g.drawLine(dividerX, y, dividerX, y + height);
		dividerX += columnWidth;
		g.drawLine(dividerX, y, dividerX, y + height);
		
		float labelsY = y + headerHeight / 2 - font.getLineHeight() / 2;
		float nameX = x + nameColumnWidth / 2 - font.getWidth(NAME_LABEL) / 2;
		float killsX = x + nameColumnWidth + columnWidth / 2 - font.getWidth(KILLS_LABEL) / 2;
		float deathsX = (float)(x + nameColumnWidth + columnWidth * 1.5 - font.getWidth(DEATHS_LABEL) / 2);
		
		//Draw labels on the header
		font.drawString(nameX, labelsY, NAME_LABEL);
		font.drawString(killsX, labelsY, KILLS_LABEL);
		font.drawString(deathsX, labelsY, DEATHS_LABEL);
		
		float lineY;
		
		//Draw an entry for each player
		for(int i = 0; i < listItems.size(); i++)
		{
			Player p = listItems.get(i).object;
			
			lineY = y + headerHeight + (listItemHeight / 2) + (i * listItemHeight);
			
			float nameWidth = font.getWidth(p.name);
			nameX = x + nameColumnWidth / 2 - nameWidth / 2;
			font.drawString(nameX, lineY, p.name, Color.white);
			
			float killsWidth = font.getWidth("" + p.numKills);
			killsX = x + nameColumnWidth + columnWidth / 2 - killsWidth / 2;
			font.drawString(killsX, lineY, "" + p.numKills);
			
			float deathsWidth = font.getWidth("" + p.numDeaths);
			deathsX = (float)(x + nameColumnWidth + (columnWidth * 1.5) - (deathsWidth / 2));
			font.drawString(deathsX, lineY, "" + p.numDeaths);
		}
	}
}
