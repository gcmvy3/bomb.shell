package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;

public abstract class Tile extends Entity
{
	public int id;
	public int size;
	
	public Tile(float x, float y, int size, int id, World world)
	{
		super(x, y, world);
		
		this.id = id;
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(size / 2, size / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.NONSOLID_TILE;
		filter.maskBits = Entity.NONE;
		
		body.getFixtureList().setFilterData(filter);
	}
}
