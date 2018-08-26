package gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import main.Player;
import main.ResourceManager;

public class ListItem<T> extends TextButton implements Comparable<ListItem<T>>
{
	final String SPRITE_NAME = "listItem";

	public T object;

	ListView<T> lv;
	
	public ListItem(ListView<T> lv, GameContainer gc, T obj, int x, int y, int width, int height)
	{
		super(gc, x, y, width, height);
		
		this.lv = lv;

		object = obj;
		label = obj.toString();
		
		setNormalImage(ResourceManager.getGUISprite(SPRITE_NAME).getScaledCopy(width, height));
		setMouseOverImage(ResourceManager.getGUISprite(SPRITE_NAME + "_mouseover").getScaledCopy(width, height));
		
		addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				lv.currentSelection = ListItem.this;
			}
		});
		
		font = ResourceManager.getFont(ResourceManager.LIST_ITEM_FONT);
	}
	
	/*
	 * Allows us to sort ListViews
	 * We have defined custom behavior for player lists (leaderboards)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ListItem<T> other) 
	{
		//TODO this should be moved into the Player class
		if(other.object instanceof Player)
		{
			Player thisP = (Player) this.object;
			Player otherP = (Player) other.object;
			
			return otherP.numKills - thisP.numKills;
		}
		else
		{
			return this.label.compareTo(other.label);
		}
	}
}
