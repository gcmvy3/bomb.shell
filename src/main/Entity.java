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
	public final static int CHARACTER = 0x0004;
	
	Body body;
	Level level;
	
	boolean alive = true;
	
	public Entity(float x, float y, Level l)
	{
		level = l;
		
		BodyDef bodDef = new BodyDef();

		bodDef.position.set(x, y);
		bodDef.type = BodyType.STATIC;

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
}
