package gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import main.ResourceManager;

public class CustomButton extends MouseOverArea
{
	private final static String DEFAULT_SPRITE = "button1";
	private final static String DEFAULT_MOUSEOVER_SPRITE = "button1_mouseover";
	private final static String DEFAULT_DISABLED_SPRITE = "button1_disabled";
	
	private Image enabledSprite;
	private Image enabledMouseOverSprite;
	private Image disabledSprite;
	
	private boolean enabled = true;
	
	String label;
	
	TrueTypeFont font;
	
	public CustomButton(GUIContext container, int x, int y, int width, int height) 
	{
		this(container, x, y, width, height, "");
	}
	
	public CustomButton(GUIContext container, int x, int y, int width, int height, String label) 
	{
		super(container, ResourceManager.getGUISprite(DEFAULT_SPRITE).getScaledCopy(width, height), x, y, width, height);
		
		enabledSprite = ResourceManager.getGUISprite(DEFAULT_SPRITE).getScaledCopy(width, height);
		enabledMouseOverSprite = ResourceManager.getGUISprite(DEFAULT_MOUSEOVER_SPRITE).getScaledCopy(width, height);
		disabledSprite = ResourceManager.getGUISprite(DEFAULT_DISABLED_SPRITE).getScaledCopy(width, height);
		
		setNormalImage(enabledSprite);
		setMouseOverImage(enabledMouseOverSprite);
		setMouseDownImage(enabledMouseOverSprite);
		
		this.label = label;
		
		font = ResourceManager.getFont(ResourceManager.BUTTON_FONT);
	}
	
	@Override
	public void render(GUIContext gc, Graphics g)
	{
		super.render(gc, g);
		
		float textWidth = font.getWidth(label);

		font.drawString(getX() + getWidth() / 2 - textWidth / 2, getY() + getHeight() / 2 - font.getLineHeight() / 2, label);
	}
	
	public void setLabel(String s)
	{
		label = s;
	}
	
	public void enable()
	{
		if(!isEnabled())
		{
			enabled = true;
			setNormalImage(enabledSprite);
			setMouseOverImage(enabledMouseOverSprite);	
			setMouseDownImage(enabledMouseOverSprite);
		}
	}
	
	public void disable()
	{
		if(isEnabled())
		{
			enabled = false;
			setNormalImage(disabledSprite);
			setMouseOverImage(disabledSprite);
			setMouseDownImage(disabledSprite);
		}
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
}
