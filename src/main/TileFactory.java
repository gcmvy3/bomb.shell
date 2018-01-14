package main;

import org.jbox2d.dynamics.World;

public class TileFactory 
{
	public static Tile createTileById(float x, float y, int size, int id, World world)
	{
		switch(id)
		{
		case 0:
			return new EmptyTile(x, y, size, id, world);
		case 1:
			return new BackgroundTile(x, y, size, id, world);
		case 2:
			return new DestructibleTile(x, y, size, id, world);
		case 3:
			return new IndestructibleTile(x, y, size, id, world);
		default:
			return null;
		}
	}
}
