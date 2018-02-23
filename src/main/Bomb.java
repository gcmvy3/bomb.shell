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
	
	final String PATH_TO_SPRITE = "assets/sprites/bomb1.png";
	
	int delay = 120;
	int age = 0;
	int damage = 100;
	float explosionRadius;
	float explosionThickness;
	
	float sizeInMeters;
	float sizeInPixels;
	
	Player parent;
	
	public Image sprite;
	
	boolean collisionEnabled = false;
	
	public Bomb(float x, float y, Level l, Player parent) throws SlickException 
	{
		super(x, y, l);
		
		this.parent = parent;
		
		sizeInMeters = l.tileSizeInMeters * SIZE_RELATIVE_TO_TILE;
		sizeInPixels = l.metersToPixels(sizeInMeters);
		
		sprite = new Image(PATH_TO_SPRITE);
		
		initExplosionShape();
		
		CircleShape boundingBox = new CircleShape();
		boundingBox.setRadius(sizeInMeters / 2);
		setShape(boundingBox);
		
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
		
		sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2, sizeInPixels, sizeInPixels);
	}
	
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
