package main;

import java.io.File;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity
{
	final String SPRITE_DIRECTORY = "assets" + File.separator + "characters" + File.separator;
	
	int size = 40;
	float speed = 100.0f;
	
	Image sprite;
	
	public Player(float x, float y, World w) throws SlickException 
	{
		super(x, y, w);
		loadSprite();
		
		body.setType(BodyType.DYNAMIC);
		
		PolygonShape boundingBox = new PolygonShape();
		boundingBox.setAsBox(size / 2, size / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.CHARACTER;
		filter.maskBits = Entity.NONSOLID_TILE;
		
		body.getFixtureList().setFilterData(filter);
	}
	
	private void loadSprite() throws SlickException
	{
		sprite = new Image(SPRITE_DIRECTORY + "default" + File.separator + "sprite1.png");
	}
	
	public void render()
	{
		sprite.draw(body.getPosition().x - size / 2, body.getPosition().y - size / 2, size, size);
	}
	
	public void update(GameContainer gc)
	{	
		Vec2 currentVelocity = body.getLinearVelocity();
		
	    float desiredXVel = 0;
	    float desiredYVel = 0;
	    
		if(gc.getInput().isKeyDown(Input.KEY_W))
		{
			desiredYVel = Math.min(currentVelocity.y - 0.1f, -speed);
		}
		if(gc.getInput().isKeyDown(Input.KEY_A))
		{
			desiredXVel = Math.min(currentVelocity.x - 0.1f, -speed);
		}
		if(gc.getInput().isKeyDown(Input.KEY_S))
		{
			desiredYVel = Math.max(currentVelocity.y - 0.1f, speed);
		}
		if(gc.getInput().isKeyDown(Input.KEY_D))
		{
			desiredXVel = Math.max(currentVelocity.x - 0.1f, speed);
		}
		
	    float velChangeX = desiredXVel - currentVelocity.x;
	    float impulseX = body.getMass() * velChangeX; //disregard time factor
	    
	    float velChangeY = desiredYVel - currentVelocity.y;
	    float impulseY = body.getMass() * velChangeY; //disregard time factor
		
		body.applyLinearImpulse(new Vec2(impulseX, impulseY), body.getWorldCenter());
	}
}
