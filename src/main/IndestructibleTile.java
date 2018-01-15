package main;

import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.Graphics;

public class IndestructibleTile extends Tile
{
	public IndestructibleTile(float x, float y, int row, int column, int size, int id, Level level) 
	{
		super(x, y, row, column, size, id, level);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.SOLID_TILE;
		filter.maskBits = Entity.CHARACTER;
		
		body.getFixtureList().setFilterData(filter);
	}

	public void render(Graphics g)
	{
		sprite.draw(body.getPosition().x - size / 2, body.getPosition().y - size / 2);
	}
}
