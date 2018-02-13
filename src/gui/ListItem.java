package gui;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

import main.SpriteManager;

public class ListItem<T> 
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
		
		sprite = SpriteManager.getGUISprite(SPRITE_NAME);
		
		moa = new MouseOverArea(gc, 
								SpriteManager.getGUISprite(SPRITE_NAME), 
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
		
		Font f = new Font("Verdana", Font.BOLD, 32);
		font = new TrueTypeFont(f, true);
	}
	
	public void render(Graphics g)
	{
		moa.render(gc, g);
		sprite.draw(x, y, width, height);
		
		int textX = x + width / 2 - font.getWidth(title) / 2;
		int textY = y + height / 2 - font.getHeight(title) / 2;
		
		font.drawString(textX, textY, title);
	}
	
	public void setTitle(String t)
	{
		title = t;
	}
}
