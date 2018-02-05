package main;

import java.util.ArrayList;
import java.util.Collections;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.Image;

import tiles.Tile;

public class Explosion implements RayCastCallback
{
	final int DURATION = 20;
	int age = 0;
	int damage;
	float x; //Center of the explosion
	float y;
	float thickness = 0;
	
	int rayIndex; //Temp variable used for calculating ray lengths
	
	Level level;
	Vec2[] rays;
	float[] rayLengths;

	boolean active = true;
	
	ArrayList<RayCollision> collisions;
	
	Image sprite;
	
	public Explosion(float x, float y, Level l, int damage, Vec2[] rays, float thickness) 
	{
		this.x = x;
		this.y = y;
		this.level = l;
		this.damage = damage;
		this.rays = rays;
		this.thickness = l.metersToPixels(thickness);
		
		rayLengths = new float[rays.length];
		for(int i = 0; i < rayLengths.length; i++)
		{
			rayLengths[i] = 1;
		}
	}
	
	public void update()
	{
		age++;
		if(age == 1)
		{
			dealDamage();
		}
		if(age >= DURATION)
		{
			active = false;
		}
	}
	
	public void render()
	{
		if(sprite == null)
		{
			loadSprite();
		}
		
		sprite.setCenterOfRotation(0, thickness / 2);
		
		for(int i = 0; i < rays.length; i++)
		{
			float angle = getAngleTo(rays[i].x, rays[i].y);
			
			float width = Math.abs(x - rays[i].x);
			float height = Math.abs(y - rays[i].y);
			
			float xPixels = level.metersToPixels(x);
			float yPixels = level.metersToPixels(y);
			
			float length = level.metersToPixels((float)Math.sqrt((width * width) + (height * height)));
			length *= rayLengths[i];
			
			sprite.setRotation(angle);
			
			sprite.draw(xPixels, yPixels - thickness / 2, length, thickness);
			
			sprite.setRotation(0);
		}
	}
	
	private void loadSprite()
	{
		sprite = SpriteManager.getSprite("explosion1");
	}
	
	private void dealDamage()
	{
		//Calculate raycast
		for(int i = 0; i < rays.length; i++)
		{
			rayIndex = i;
			
			collisions = new ArrayList<RayCollision>();
			level.world.raycast(this, new Vec2(x, y), rays[i]);
			
			Collections.sort(collisions);
			
			for(RayCollision rc : collisions)
			{
				if(rc.entity instanceof Tile)
				{
					Tile t = (Tile)rc.entity;
					if(!t.tileType.destructible && t.tileType.solid)
					{
						break;
					}
				}
				rc.entity.takeDamage(damage);
			}
		}
	}

	/**
	 * Used for tracing the explosion
	 * Is called automatically when an explosion ray collides with a fixture
	 */
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) 
	{
		Entity entity = (Entity)fixture.getBody().getUserData();
		
		//If we hit an indestructible entity, record it
		if(entity instanceof Tile)
		{
			Tile t = (Tile)entity;
			
			if(!t.tileType.destructible && t.tileType.solid)
			{
				if(fraction < rayLengths[rayIndex])
				{
					rayLengths[rayIndex] = fraction;
				}	
			}
		}
		else if(entity instanceof LevelBoundary)
		{
			if(fraction < rayLengths[rayIndex])
			{
				rayLengths[rayIndex] = fraction;
			}
		}
		
		RayCollision collision = new RayCollision(entity, fraction);
		collisions.add(collision);
		
		return 1;
	}
	
	/**
	 * Calculates angle from this object to a given point
	 * @param targetX
	 * @param targetY
	 * @return The angle in degrees
	 */
	public float getAngleTo(float targetX, float targetY) 
	{
	    float angle = (float) Math.toDegrees(Math.atan2(targetY - y, targetX - x));

	    if(angle < 0)
	    {
	        angle += 360;
	    }

	    return angle;
	}
}
