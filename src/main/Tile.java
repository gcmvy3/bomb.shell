package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Tile extends Entity
{
	public int id;
	public int size;
	
	public int row;
	public int column;
	
	public Image sprite;
	
	public Level level;
	
	public Tile(float x, float y, int row, int column, int size, int id, Level level)
	{
		super(x, y, level.world);
		
		this.size = size;
		this.id = id;
		this.level = level;
		
		sprite = level.tileset.getImage(id);
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(size / 2, size / 2);
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
			sprite.draw(body.getPosition().x - size / 2, body.getPosition().y - size / 2);
		}
	}
}
