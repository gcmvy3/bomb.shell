package main;

import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;

public class IndestructibleTile extends Tile
{
	public IndestructibleTile(float x, float y, int size, int id, World world) 
	{
		super(x, y, size, id, world);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.SOLID_TILE;
		filter.maskBits = Entity.CHARACTER;
		
		body.getFixtureList().setFilterData(filter);
	}

}
