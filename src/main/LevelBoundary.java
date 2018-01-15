package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.World;

public class LevelBoundary extends Entity
{
	public LevelBoundary(float x, float y, float width, float height, World w) 
	{
		super(x, y, w);
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(width / 2, height / 2);
		setShape(boundingBox);
	}

}
