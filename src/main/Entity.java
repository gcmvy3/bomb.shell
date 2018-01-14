package main;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public abstract class Entity
{	
	Body body;
	
	//Categories for collision filtering
	public final static int NONE = 0x0000;
	public final static int SOLID_TILE = 0x0002;
	public final static int NONSOLID_TILE = 0x0003;
	public final static int CHARACTER = 0x0004;
	
	public Entity(float x, float y, World w)
	{
		BodyDef bodDef = new BodyDef();

		bodDef.position.set(x, y);
		bodDef.type = BodyType.STATIC;

		body = w.createBody(bodDef);
	}
	
	public void setShape(Shape s)
	{
		FixtureDef fixDef = new FixtureDef();

		fixDef.shape = s;

		body.createFixture(fixDef);
	}
}
