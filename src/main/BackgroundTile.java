package main;

import org.jbox2d.dynamics.Filter;

public class BackgroundTile extends Tile
{
	public BackgroundTile(float x, float y, int row, int column, int size, int id, Level level) 
	{
		super(x, y, row, column, size, id, level);
		
		Filter filter = new Filter();
		filter.categoryBits = 0;
		
		this.body.getFixtureList().setFilterData(filter);
	}

}
