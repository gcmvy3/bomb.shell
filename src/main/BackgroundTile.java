package main;

import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;

public class BackgroundTile extends Tile
{
	public BackgroundTile(float x, float y, int size, int id, World world) 
	{
		super(x, y, size, id, world);
		
		Filter filter = new Filter();
		filter.categoryBits = 0;
		
		this.body.getFixtureList().setFilterData(filter);
	}

}
