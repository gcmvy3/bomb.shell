package tiles;

import org.newdawn.slick.Image;

public class TileType 
{
	public int id;
	public int health = 0;
	public boolean solid = false;
	public boolean destructible = false;
	public boolean visible = true;
	public boolean spawn = false;
	
	public Image sprite;
	
	public TileType(int id)
	{
		this.id = id;
	}
}
