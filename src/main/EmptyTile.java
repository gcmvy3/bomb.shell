package main;

import org.jbox2d.dynamics.World;

public class EmptyTile extends Tile
{
	public EmptyTile(float x, float y, int size, int id, World world) 
	{
		super(x, y, size, id, world);
	}

}
