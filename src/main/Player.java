package main;

import java.io.File;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends Entity
{
	final String SPRITE_DIRECTORY = "assets" + File.separator + "characters" + File.separator;
	final float JOYSTICK_DEADZONE = 0.1f;
	
	final float sizeRelativeToTile = 0.8f;
	
	float sizeInMeters;
	float sizeInPixels;
	float speed = 8.0f;
	
	double joystickX = 0f;
	double joystickY = 0f;
	
	int maxHealth = 200;
	int health = maxHealth;
	
	int attackDelay = 15;
	int timeSinceAttack = 0;
	
	boolean dropBomb = false;
	
	Level level;
	
	Image sprite;
	
	public Player(float x, float y, Level l) throws SlickException 
	{
		super(x, y, l);
		
		level = l;
		
		body.setType(BodyType.DYNAMIC);
		
		sizeInMeters = l.tileSizeInMeters * sizeRelativeToTile;
		sizeInPixels = l.metersToPixels(sizeInMeters);
		
		CircleShape boundingBox = new CircleShape();
		boundingBox.setRadius(sizeInMeters / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.CHARACTER;
		filter.maskBits = Entity.SOLID_TILE;
		
		body.getFixtureList().setFilterData(filter);
	}
	
	private void loadSprite() throws SlickException
	{
		sprite = new Image(SPRITE_DIRECTORY + "default" + File.separator + "sprite1.png");
	}
	
	public void render()
	{
		if(sprite == null)
		{
			try 
			{
				loadSprite();
			} 
			catch (SlickException e) 
			{
				e.printStackTrace();
			}
		}
		
		int pixelsX = (int)level.metersToPixels(body.getPosition().x);
		int pixelsY = (int)level.metersToPixels(body.getPosition().y);
		
		sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2, sizeInPixels, sizeInPixels);
	}
	
	public void update(GameContainer gc)
	{	
		//Movement
		Vec2 currentVelocity = body.getLinearVelocity();
		
	    float desiredXVel = 0;
	    float desiredYVel = 0;
	    
	    //System.out.println("JoystickY: " + joystickY);
	    
		if(joystickY > JOYSTICK_DEADZONE)
		{
			//UP
			desiredYVel = Math.min(currentVelocity.y - 0.1f, speed * (float)joystickY);
		}
		if(joystickX < -JOYSTICK_DEADZONE)
		{
			desiredXVel = Math.min(currentVelocity.x - 0.1f, speed * (float)joystickX);
		}
		if(joystickY < -JOYSTICK_DEADZONE)
		{
			//DOWN
			desiredYVel = Math.max(currentVelocity.y - 0.1f, -speed * (float)joystickY);
		}
		if(joystickX > JOYSTICK_DEADZONE)
		{
			desiredXVel = Math.max(currentVelocity.x - 0.1f, speed * (float)joystickX);
		}
		
	    float velChangeX = desiredXVel - currentVelocity.x;
	    float impulseX = body.getMass() * velChangeX; //disregard time factor
	    
	    float velChangeY = desiredYVel - currentVelocity.y;
	    float impulseY = body.getMass() * velChangeY; //disregard time factor
		
		body.applyLinearImpulse(new Vec2(impulseX, impulseY), body.getWorldCenter());
		
		//Drop bombs
		if(timeSinceAttack <= attackDelay)
		{
			timeSinceAttack++;
		}

		if(dropBomb)
		{
			if(timeSinceAttack > attackDelay)
			{
				dropBomb();
			}
			dropBomb = !dropBomb;
		}
	}
	
	private void dropBomb()
	{
		timeSinceAttack = 0;
		
		try
		{
			Bomb b = new Bomb(body.getPosition().x, body.getPosition().y, level, this);
			
			level.bombs.add(b);
		}
		catch(SlickException e)
		{
			System.err.println("Could not create bomb!");
		}
	}
	
	public void takeDamage(int damage)
	{
		health -= damage;
		if(health >= 0)
		{
			//TODO destroy player here
		}
	}
}
