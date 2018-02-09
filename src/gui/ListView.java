package gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import main.SpriteManager;

public class ListView<T> 
{
	final String SPRITE_NAME = "listBackground";
	
	int x;
	int y;
	int width;
	int height;
	int listItemHeight;
	GameContainer gameContainer;
	
	ArrayList<ListItem<T>> listItems;
	
	ListItem<T> currentSelection;
	
	//TODO add scrolling functionality
	public ListView(GameContainer gt, int x, int y, int width, int height)
	{
		this(gt, x, y, width, height, height / 20);
	}
	
	public ListView(GameContainer gt, int x, int y, int width, int height, int numItemsShown)
	{
		this.gameContainer = gt;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		listItems = new ArrayList<ListItem<T>>();
		
		listItemHeight = height / numItemsShown;
	}
	
	public void render(Graphics g)
	{
		SpriteManager.getGUISprite(SPRITE_NAME).draw(x, y, width, height);
		
		for(ListItem<T> li : listItems)
		{
			li.render(g);
		}
	}
	
	public void addItem(T obj)
	{
		ListItem<T> li = new ListItem<T>(this, gameContainer, obj, x, y + listItemHeight * listItems.size(), width, listItemHeight);
		listItems.add(li);
	}
	
	public void removeItem(T obj)
	{
		Iterator<ListItem<T>> it = listItems.iterator();
		 
	    while(it.hasNext()) 
	    {
	      if(it.next().object == obj)
	      {
	    	  it.remove();
	      }
	    }
	}
	
	public void setList(ArrayList<T> newList)
	{
		listItems = new ArrayList<ListItem<T>>();
		
		for(T obj : newList)
		{
			addItem(obj);
		}
	}
	
	public T getCurrentSelection()
	{
		if(currentSelection != null)
		{
			return currentSelection.object;
		}
		return null;
	}
}