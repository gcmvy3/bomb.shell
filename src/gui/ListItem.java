package gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

import main.Player;
import main.ResourceManager;

public class ListItem<T> implements Comparable<ListItem<T>>
{
	final String SPRITE_NAME = "listItem";
	
	int x;
	int y;
	int width;
	int height;
	
	public String title;
	public T object;
	
	MouseOverArea moa;
	
	GameContainer gc;
	
	ListView<T> lv;
	
	Image sprite;
	
	TrueTypeFont font;
	
	public ListItem(ListView<T> lv, GameContainer gc, T obj, int x, int y, int width, int height)
	{
		this.gc = gc;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		object = obj;
		title = obj.toString();
		
		sprite = ResourceManager.getGUISprite(SPRITE_NAME);
		
		moa = new MouseOverArea(gc, 
								ResourceManager.getGUISprite(SPRITE_NAME).getScaledCopy(width, height), 
								x,
								y,
								width,
								height);
		
		moa.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				lv.currentSelection = ListItem.this;
			}
		});
		moa.setMouseOverImage(ResourceManager.getGUISprite(SPRITE_NAME + "_mouseover").getScaledCopy(width, height));
		
		font = ResourceManager.getFont(ResourceManager.LIST_ITEM_FONT);
	}
	
	public void render(Graphics g)
	{
		moa.render(gc, g);

		int textX = x + width / 2 - font.getWidth(title) / 2;
		int textY = y + height / 2 - font.getHeight(title) / 2;
		
		font.drawString(textX, textY, title);
	}
	
	public void setTitle(String t)
	{
		title = t;
	}

	/*
	 * Allows us to sort ListViews
	 * We have defined custom behavior for player lists (leaderboards)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ListItem<T> other) 
	{
		if(other.object instanceof Player)
		{
			Player thisP = (Player) this.object;
			Player otherP = (Player) other.object;
			
			return thisP.numKills - otherP.numKills;
		}
		else
		{
			return this.title.compareTo(other.title);
		}
	}
}
