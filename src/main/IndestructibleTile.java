package main;

import org.jbox2d.dynamics.Filter;

import tiles.Tile;

public class IndestructibleTile extends Tile
{
	public IndestructibleTile(float x, float y, int row, int column, int size, int id, Level level) 
	{
		super(x, y, row, column, size, id, level);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.SOLID_TILE;
		filter.maskBits = Entity.PLAYER;
		
		body.getFixtureList().setFilterData(filter);
	}
}
