package gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import main.ResourceManager;

public class TextButton extends ImageButton
{
	String label;
	
	TrueTypeFont font;
	
	public TextButton(GameContainer gc, int x, int y, int width, int height) 
	{
		this(gc, x, y, width, height, "");
	}
	
	public TextButton(GameContainer gc, int x, int y, int width, int height, String label) 
	{
		super(gc, x, y, width, height);
		
		this.label = label;
		
		font = ResourceManager.getFont(ResourceManager.BUTTON_FONT);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g)
	{
		super.render(gc, g);
		
		float textWidth = font.getWidth(label);

		font.drawString(getX() + getWidth() / 2 - textWidth / 2, getY() + getHeight() / 2 - font.getLineHeight() / 2, label);
	}
	
	public void setLabel(String s)
	{
		label = s;
	}
	
	@Override
	public void enable()
	{
		if(!isEnabled())
		{
			enabled = true;
			moa.setNormalImage(enabledSprite);
			moa.setMouseOverImage(enabledMouseOverSprite);	
			moa.setMouseDownImage(enabledMouseOverSprite);
		}
	}
	
	@Override
	public void disable()
	{
		if(isEnabled())
		{
			enabled = false;
			moa.setNormalImage(disabledSprite);
			moa.setMouseOverImage(disabledSprite);
			moa.setMouseDownImage(disabledSprite);
		}
	}
}
