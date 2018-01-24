package tiles;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.Graphics;

import main.Entity;
import main.Level;

public class Tile extends Entity
{
	public float sizeInMeters;
	private float sizeInPixels;
	
	public TileType tileType;
	
	public int row;
	public int column;
	
	public Level level;
	
	public Tile(float x, float y, int row, int column, float size, TileType type, Level level)
	{
		super(x, y, level);
		
		sizeInMeters = size;
		this.row = row;
		this.column = column;
		this.tileType = type;
		this.level = level;
		
		maxHealth = tileType.health;
		health = maxHealth;
		
		sizeInPixels = level.metersToPixels(size);

		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(sizeInMeters / 2, sizeInMeters / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		if(tileType.solid)
		{
			filter.categoryBits = Entity.SOLID_TILE;
			filter.maskBits = Entity.PLAYER;
		}
		else
		{
			filter.categoryBits = Entity.NONSOLID_TILE;
			filter.maskBits = Entity.NONE;
		}
		
		body.getFixtureList().setFilterData(filter);
	}
	
	public void render(Graphics g)
	{
		render(g, 0, 0);
	}
	
	public void render(Graphics g, int x, int y)
	{
		if(tileType.visible)
		{
			if(tileType.sprite != null)
			{
				int pixelsX = x + (int)level.metersToPixels(body.getPosition().x);
				int pixelsY = y + (int)level.metersToPixels(body.getPosition().y);
				
				tileType.sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2);
			}
		}
	}
	
	@Override
	public void takeDamage(int damage)
	{
		if(tileType.destructible)
		{
			health -= damage;
			if(health >= 0)
			{
				destroy();
			}
		}
	}
	
	@Override
	public void destroy()
	{
		//Replace tile with a nullTile
		Tile replacementTile = new Tile(body.getPosition().x, 
										body.getPosition().y, 
										row, 
										column, 
										sizeInMeters, 
										level.tileset.getTileType(-1),
										level);

		level.setForegroundTile(row, column, replacementTile);
	}
}
