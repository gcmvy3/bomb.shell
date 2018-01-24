package main;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Entity
{	
	//Categories for collision filtering
	public final static int NONE = 0x0000;
	public final static int SOLID_TILE = 0x0002;
	public final static int NONSOLID_TILE = 0x0003;
	public final static int PLAYER = 0x0004;
	public final static int BOMB = 0x0005;
	public final static int LEVEL_BOUNDARY = 0x0006;
	
	public Body body;
	public Level level;
	
	public boolean active = true;
	
	public Entity(float x, float y, Level l)
	{
		level = l;
		
		BodyDef bodDef = new BodyDef();

		bodDef.position.set(x, y);
		bodDef.type = BodyType.STATIC;
		bodDef.userData = this;
		
		body = l.world.createBody(bodDef);
		
	}
	
	public void update()
	{
		
	}
	
	public void render()
	{
		
	}
	
	public void setShape(Shape s)
	{
		FixtureDef fixDef = new FixtureDef();

		fixDef.shape = s;

		body.createFixture(fixDef);
	}
	
	public float getMetersTo(Entity other)
	{
		float x1 = body.getPosition().x;
		float y1 = body.getPosition().y;
		
		float x2 = other.body.getPosition().x;
		float y2 = other.body.getPosition().y;
		
		return (float)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
}
