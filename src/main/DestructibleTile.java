package main;

import org.jbox2d.dynamics.Filter;

public class DestructibleTile extends Tile
{
	int maxHealth = 100;
	int health = maxHealth;
	Level level;
	
	public DestructibleTile(float x, float y, int row, int column, int size, int id, Level level) 
	{
		super(x, y, row, column, size, id, level);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.SOLID_TILE;
		filter.maskBits = Entity.CHARACTER;
		
		body.getFixtureList().setFilterData(filter);
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		if(health >= 0)
		{
			destroy();
		}
	}
	
	public void destroy()
	{
		Tile replacementTile = TileFactory.createTileById(body.getPosition().x, body.getPosition().y, row, column, size, 0, level);
		level.setForegroundTile(column, row, replacementTile);
	}
}
