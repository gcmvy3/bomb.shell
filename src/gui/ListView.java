package gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import main.ResourceManager;

public class ListView<T> 
{
	final String SPRITE_NAME = "listBackground";
	final int DFLT_NUM_ITEMS_SHOWN = 5;
	
	public int x;
	public int y;
	public int width;
	public int height;
	public int listItemHeight;
	public int numItemsShown;
	GameContainer gameContainer;
	
	ArrayList<ListItem<T>> listItems;
	
	ListItem<T> currentSelection;
	
	//TODO add scrolling functionality
	public ListView(GameContainer gt, int x, int y, int width, int height)
	{
		this.gameContainer = gt;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		listItems = new ArrayList<ListItem<T>>();
		
		listItemHeight = ResourceManager.getFont(ResourceManager.LIST_ITEM_FONT).getLineHeight();
		
		numItemsShown = DFLT_NUM_ITEMS_SHOWN;
	}
	
	public ListView(GameContainer gt, int x, int y, int width, int height, int numItemsShown)
	{
		this(gt, x, y, width, height);
		
		this.numItemsShown = numItemsShown;
	}
	
	public void render(GameContainer gc, Graphics g)
	{	
		ResourceManager.getGUISprite(SPRITE_NAME).draw(x, y, width, height);
		
		for(ListItem<T> li : listItems)
		{
			li.render(gc, g);
		}
	}
	
	public void setLocation(int x, int y)
	{
		if(x != this.x || y != this.y)
		{
			translate(x - this.x, y - this.y);
		}
	}
	
	public void translate(int dx, int dy)
	{
		x += dx;
		y += dy;
		
		for(ListItem<T> li : listItems) 
		{
			li.setLocation(li.getX() + dx, li.getY() + dy);
		}
	}
	
	public void setDimensions(int width, int height)
	{
		if(width != this.width || height != this.height)
		{
			this.width = height;
			this.height = height;
			
			for(ListItem<T> li : listItems) 
			{
				li.setDimensions(width, listItemHeight);
			}
		}
	}
	
	public void addItem(T obj)
	{
		ListItem<T> li = new ListItem<T>(this, gameContainer, obj, x, y + listItemHeight * listItems.size(), width, listItemHeight);
		listItems.add(li);
		
		if(currentSelection == null)
		{
			currentSelection = li;
		}
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
	
	public T getIndex(int i)
	{
		ListItem<T> item = listItems.get(i);
		
		if(item != null)
		{
			T obj = item.object;
			
			if(obj != null)
			{
				return obj;
			}
		}
		
		return null;
	}
}
