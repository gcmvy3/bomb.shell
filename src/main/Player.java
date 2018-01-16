package main;

import java.io.File;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity
{
	final String SPRITE_DIRECTORY = "assets" + File.separator + "characters" + File.separator;
	
	final float sizeRelativeToTile = 0.8f;
	
	float sizeInMeters;
	float sizeInPixels;
	float speed = 8.0f;
	
	Level level;
	
	Image sprite;
	
	public Player(float x, float y, Level l) throws SlickException 
	{
		super(x, y, l.world);
		
		level = l;
		
		loadSprite();
		
		body.setType(BodyType.DYNAMIC);
		
		sizeInMeters = l.tileSizeInMeters * sizeRelativeToTile;
		sizeInPixels = l.metersToPixels(sizeInMeters);
		
		CircleShape boundingBox = new CircleShape();
		boundingBox.setRadius(sizeInMeters / 2);
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
		int pixelsX = (int)level.metersToPixels(body.getPosition().x);
		int pixelsY = (int)level.metersToPixels(body.getPosition().y);
		
		sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2, sizeInPixels, sizeInPixels);
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
