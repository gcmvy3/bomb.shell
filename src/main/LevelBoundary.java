package main;

import org.jbox2d.collision.shapes.PolygonShape;

public class LevelBoundary extends Entity
{
	public LevelBoundary(float x, float y, float width, float height, Level l) 
	{
		super(x, y, l);
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(width / 2, height / 2);
		setShape(boundingBox);
	}

}
