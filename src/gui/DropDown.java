package gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import main.ResourceManager;

public class DropDown<T> extends ListView<T>
{
	private final String DOWN_ARROW = "downArrow";
	private final String UP_ARROW = "upArrow";
	
	private int arrowWidth;
	private int itemWidth;
	
	public boolean isOpen = false;
	
	private ImageButton downArrow;
	private ImageButton upArrow;
	
	public DropDown(GameContainer gc, int x, int y, int width, int height) 
	{
		super(gc, x, y, width, height);
		
		arrowWidth = height;
		itemWidth = width - arrowWidth;
		
		initArrowButtons(gc);
	}
	
	public DropDown(GameContainer gc, int x, int y, int width, int height, int numItemsShown) 
	{
		super(gc, x, y, width, height, numItemsShown);
	}
	
	private void initArrowButtons(GameContainer gc)
	{
		//Initialize
		downArrow = new ImageButton(gc,  
				x + width - arrowWidth,
				y,
				arrowWidth,
				arrowWidth);
	
		downArrow.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				isOpen = true;
			}
		});
		downArrow.setNormalImage(ResourceManager.getGUISprite(DOWN_ARROW).getScaledCopy(arrowWidth, arrowWidth));
		downArrow.setMouseOverImage(ResourceManager.getGUISprite(DOWN_ARROW + "_mouseover").getScaledCopy(arrowWidth, arrowWidth));
		
		upArrow = new ImageButton(gc,  
				x + width - arrowWidth,
				y,
				arrowWidth,
				arrowWidth);
	
		upArrow.addListener(new ComponentListener() 
		{
			@Override
			public void componentActivated(AbstractComponent arg0) 
			{
				isOpen = false;
			}
		});
		upArrow.setNormalImage(ResourceManager.getGUISprite(UP_ARROW).getScaledCopy(arrowWidth, arrowWidth));
		upArrow.setMouseOverImage(ResourceManager.getGUISprite(UP_ARROW + "_mouseover").getScaledCopy(arrowWidth, arrowWidth));
	}
	
	public void render(GameContainer gc, Graphics g)
	{	
		if(isOpen)
		{
			ResourceManager.getGUISprite(SPRITE_NAME).draw(x, y, width, height * listItems.size());
			upArrow.render(gc, g);
		}
		else
		{
			ResourceManager.getGUISprite(SPRITE_NAME).draw(x, y, width, height);
			downArrow.render(gc, g);
		}
		
		if(currentSelection != null)
		{
			currentSelection.render(gc, g);
		}
		
		if(isOpen)
		{
			for(ListItem<T> li : listItems)
			{
				li.render(gc, g);
			}
		}
	}
	
	public void addItem(T obj)
	{
		ListItem<T> li = new ListItem<T>(this, gameContainer, obj, x, y + listItemHeight * listItems.size(), itemWidth, listItemHeight);
		listItems.add(li);
		
		if(currentSelection == null)
		{
			currentSelection = li;
		}
	}
	
	public void translate(int dx, int dy)
	{
		super.translate(dx, dy);
		
		upArrow.setLocation(upArrow.getX() + dx, upArrow.getY() + dy);
		downArrow.setLocation(downArrow.getX() + dx, downArrow.getY() + dy);
	}
}
