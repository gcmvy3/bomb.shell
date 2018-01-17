package main;

import java.util.ArrayList;
import java.util.Collections;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class Explosion implements RayCastCallback
{
	final int DURATION = 5;
	int age = 0;

	Level level;
	int damage;
	Vec2[][] rays;

	boolean active = true;
	
	ArrayList<RayCollision> collisions;
	
	public Explosion(Level l, int damage, Vec2[][] rays) 
	{
		this.level = l;
		this.damage = damage;
		this.rays = rays;
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
		
	}
	
	private void dealDamage()
	{
		collisions = new ArrayList<RayCollision>();
		
		//Calculate raycast
		for(int i = 0; i < rays.length; i++)
		{
			Vec2[] ray = rays[i];
			
			level.world.raycast(this, ray[0], ray[1]);
		}
		
		Collections.sort(collisions);
		
		for(RayCollision rc : collisions)
		{
			if(rc.entity instanceof IndestructibleTile)
			{
				break;
			}
			else if(rc.entity instanceof DestructibleTile)
			{
				DestructibleTile dt = (DestructibleTile)rc.entity;
				dt.takeDamage(damage);
			}
			else if(rc.entity instanceof Player)
			{
				Player p = (Player)rc.entity;
				p.takeDamage(damage);
			}
		}
	}

	//Used for tracing the explosion
	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) 
	{
		Entity entity = (Entity)fixture.getBody().getUserData();
		
		RayCollision collision = new RayCollision(entity, fraction);
		collisions.add(collision);
		
		if(entity instanceof IndestructibleTile)
		{			
			return fraction;
		}
		
		return 1;
	}
}
