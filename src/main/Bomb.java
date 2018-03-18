package main;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bomb extends Entity
{
	final float SIZE_RELATIVE_TO_TILE = 0.5f;
	final float EXPLOSION_THICKNESS_RELATIVE_TO_TILE = 0.6f;
	final int RADIUS_IN_BLOCKS = 4;
	
	int delay = 120;
	int age = 0;
	int damage = 100;
	int redDelay = 12;
	int timeSinceRed = 0;
	float explosionRadius;
	float explosionThickness;
	
	float sizeInMeters;
	float sizeInPixels;
	
	Image redSprite;
	
	Player parent;
	
	boolean collisionEnabled = false;
	
	public Bomb(float x, float y, Level l, Player parent) throws SlickException 
	{
		super(x, y, l);
		
		this.parent = parent;
		
		sizeInMeters = l.tileSizeInMeters * SIZE_RELATIVE_TO_TILE;
		sizeInPixels = l.metersToPixels(sizeInMeters);
		
		sprite = ResourceManager.getSprite("bomb").getScaledCopy((int)sizeInPixels, (int)sizeInPixels);
		redSprite = ResourceManager.getSprite("bomb_red").getScaledCopy((int)sizeInPixels, (int)sizeInPixels);
		
		initExplosionShape();
		
		//Set up bounding box
		CircleShape boundingBox = new CircleShape();
		boundingBox.setRadius(sizeInMeters / 2);
		setShape(boundingBox);
		
		//Set up collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.BOMB;
		filter.maskBits = Entity.NONE;
		body.getFixtureList().setFilterData(filter);
	}
	
	private void initExplosionShape()
	{
		explosionRadius = level.tileSizeInMeters * RADIUS_IN_BLOCKS;
		explosionThickness = level.tileSizeInMeters * EXPLOSION_THICKNESS_RELATIVE_TO_TILE;
	}
	
	public void update()
	{		
		//Don't collide with things until the parent moves
		if(isActive())
		{
			if(!collisionEnabled || parent == null)
			{
				try
				{
					CircleShape boundingBox = (CircleShape)body.getFixtureList().getShape();
					CircleShape parentBoundingBox = (CircleShape)parent.body.getFixtureList().getShape();
					
					float maxParentDistance = boundingBox.getRadius() + parentBoundingBox.getRadius();
					
					if(getMetersTo(parent) > maxParentDistance)
					{
						enableCollision();
					}
				}
				catch (Exception e)
				{
					enableCollision();
				}
			}
			
			age++;
			if(age >= delay)
			{
				explode();
			}
		}
	}
	
	public void enableCollision()
	{
		collisionEnabled = true;
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.BOMB;
		filter.maskBits = Entity.PLAYER | Entity.LEVEL_BOUNDARY;

		body.getFixtureList().setFilterData(filter);
	}
	
	public void render()
	{
		int pixelsX = (int)level.metersToPixels(body.getPosition().x);
		int pixelsY = (int)level.metersToPixels(body.getPosition().y);
		
		//Flash red
		timeSinceRed++;
		if(timeSinceRed > redDelay)
		{
			redSprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2, sizeInPixels, sizeInPixels);
			
			timeSinceRed = 0;
			redDelay--;
			if(redDelay < 0)
			{
				redDelay = 0;
			}
		}
		else
		{
			sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2, sizeInPixels, sizeInPixels);
		}
	}
	
	//Create an explosion object at the current location
	public void explode()
	{
		Vec2[] rays = new Vec2[4];
		
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		
		Vec2 downRay = new Vec2(x, y + explosionRadius);
		Vec2 upRay = new Vec2(x, y - explosionRadius);
		Vec2 leftRay = new Vec2(x - explosionRadius, y);
		Vec2 rightRay = new Vec2(x + explosionRadius, y);
		
		rays[0] = downRay;
		rays[1] = upRay;
		rays[2] = leftRay;
		rays[3] = rightRay;		
		Explosion explosion = new Explosion(parent, x, y, level, damage, rays, explosionThickness);
		level.addExplosion(explosion);
		
		setActive(false);
		
		level.world.destroyBody(body);
	}
	
	@Override
	public void takeDamage(int damage)
	{
		//If the bomb gets hit with an explosion, it should explode
		explode();
	}
}
