package gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import main.ResourceManager;

public class CustomButton extends MouseOverArea
{
	private final static String DEFAULT_SPRITE = "button1";
	private final static String DEFAULT_MOUSEOVER_SPRITE = "button1_mouseover";
	
	String label;
	
	TrueTypeFont font;
	
	public CustomButton(GUIContext container, int x, int y, int width, int height) 
	{
		this(container, x, y, width, height, "");
	}
	
	public CustomButton(GUIContext container, int x, int y, int width, int height, String label) 
	{
		super(container, ResourceManager.getGUISprite(DEFAULT_SPRITE).getScaledCopy(width, height), x, y, width, height);
		
		this.setMouseOverImage(ResourceManager.getGUISprite(DEFAULT_MOUSEOVER_SPRITE).getScaledCopy(width, height));
		
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
}
