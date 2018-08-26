package gui;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

import main.ResourceManager;

public class ImageButton 
{
	private final static String DEFAULT_SPRITE = "button1";
	private final static String DEFAULT_MOUSEOVER_SPRITE = "button1_mouseover";
	private final static String DEFAULT_DISABLED_SPRITE = "button1_disabled";
	
	public MouseOverArea moa;
	public boolean enabled = true;
	
	protected Image enabledSprite;
	protected Image enabledMouseOverSprite;
	protected Image disabledSprite;
	
	protected GameContainer gc;
	
	private ArrayList<ComponentListener> listeners = new ArrayList<ComponentListener>();
	
	public ImageButton(GameContainer gc, int x, int y, int width, int height) 
	{
		this.gc = gc;
		initMOA(x, y, width, height);
	}
	
	private void initMOA(int x, int y, int width, int height)
	{
		enabledSprite = ResourceManager.getGUISprite(DEFAULT_SPRITE).getScaledCopy(width, height);
		enabledMouseOverSprite = ResourceManager.getGUISprite(DEFAULT_MOUSEOVER_SPRITE).getScaledCopy(width, height);
		disabledSprite = ResourceManager.getGUISprite(DEFAULT_DISABLED_SPRITE).getScaledCopy(width, height);
		
		moa = new MouseOverArea(gc, enabledSprite, x, y, width, height);
		
		moa.setNormalImage(enabledSprite);
		moa.setMouseOverImage(enabledMouseOverSprite);
		moa.setMouseDownImage(enabledMouseOverSprite);
		
		for(ComponentListener listener : listeners)
		{
			moa.addListener(listener);
		}
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		moa.render(gc, g);
	}
	
	public void enable()
	{
		if(!isEnabled())
		{
			enabled = true;
		}
	}
	
	public void disable()
	{
		if(isEnabled())
		{
			enabled = false;
		}
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public int getX()
	{
		return moa.getX();
	}
	
	public int getY()
	{
		return moa.getY();
	}
	
	public int getWidth()
	{
		return moa.getWidth();
	}
	
	public int getHeight()
	{
		return moa.getHeight();
	}
	
	public void setX(int x)
	{
		moa.setX(x);
	}
	
	public void setY(int y)
	{
		moa.setY(y);
	}
	
	public void addListener(ComponentListener listener)
	{
		listeners.add(listener);
		moa.addListener(listener);
	}
	
	public void removeListener(ComponentListener listener)
	{
		listeners.remove(listener);
		moa.removeListener(listener);
	}
	
	public void setMouseOverImage(Image image)
	{
		moa.setMouseOverImage(image);
	}
	
	public void setMouseDownImage(Image image)
	{
		moa.setMouseDownImage(image);
	}
	
	public void setNormalImage(Image image)
	{
		moa.setNormalImage(image);
	}
	
	public void setLocation(int x, int y)
	{
		moa.setX(x);
		moa.setY(y);
	}
	
	public void setDimensions(int width, int height)
	{
		//MouseOverArea cannot be scaled so we must make a new instance
		//This is expensive, so avoid it if at all possible
		if(width != moa.getWidth() || height != moa.getHeight())
		{
			initMOA(getX(), getY(), width, height);
		}
	}
}
