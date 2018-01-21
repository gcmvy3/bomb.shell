package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Filter;

public class LevelBoundary extends Entity
{
	public LevelBoundary(float x, float y, float width, float height, Level l) 
	{
		super(x, y, l);
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(width / 2, height / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.LEVEL_BOUNDARY;
		filter.maskBits = Entity.PLAYER | Entity.BOMB;
		
		body.getFixtureList().setFilterData(filter);
	}

}
