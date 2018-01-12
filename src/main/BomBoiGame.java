package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class BomBoiGame extends BasicGame
{
	public BomBoiGame() 
	{
		super("BomBoi");
	}

    public static void main(String[] arguments)
    {
        try
        {
            AppGameContainer app = new AppGameContainer(new BomBoiGame());
            app.setDisplayMode(1920, 1080, false);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
	
	@Override
	public void init(GameContainer arg0) throws SlickException 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException 
	{
		// TODO Auto-generated method stub
		
	}

}
