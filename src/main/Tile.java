package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Tile extends Entity
{
	public int id;
	public float sizeInMeters;
	private float sizeInPixels;
	
	public int row;
	public int column;
	
	public Image sprite;
	
	public Level level;
	
	public Tile(float x, float y, int row, int column, int size, int id, Level level)
	{
		super(x, y, level);
		
		sizeInMeters = size;
		this.row = row;
		this.column = column;
		this.id = id;
		this.level = level;
		
		sizeInPixels = level.metersToPixels(size);
		
		sprite = level.tileset.getImage(id);
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(sizeInMeters / 2, sizeInMeters / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.NONSOLID_TILE;
		filter.maskBits = Entity.NONE;
		
		body.getFixtureList().setFilterData(filter);
	}
	
	public void render(Graphics g)
	{
		if(sprite != null)
		{
			int pixelsX = (int)level.metersToPixels(body.getPosition().x);
			int pixelsY = (int)level.metersToPixels(body.getPosition().y);
			
			sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2);
		}
	}
}
