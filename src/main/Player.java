package main;

import java.io.File;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Entity
{
	final String SPRITE_DIRECTORY = "assets" + File.separator + "characters" + File.separator;
	final float JOYSTICK_DEADZONE = 0.5f;
	
	final float sizeRelativeToTile = 0.8f;
	
	float sizeInMeters;
	float speed = 8.0f;
	
	//public double joystickX = 0f;
	//public double joystickY = 0f;
	
	public boolean dPadUp = false;
	public boolean dPadDown = false;
	public boolean dPadLeft = false;
	public boolean dPadRight = false;
	
	int maxHealth = 100;
	int health = maxHealth;
	
	int attackDelay = 30;
	int timeSinceAttack = 0;
	public int timeSinceDeath = 0;
	public int numDeaths = 0;
	public int numKills = 0;
	public boolean dropBomb = false;
	
	public String name = "";
	
	public Color color = null;
	
	Level level;
	
	Image sprite;
	
	public Player(float x, float y, Level l) throws SlickException 
	{
		super(x, y, l);
		
		level = l;
		
		body.setType(BodyType.DYNAMIC);
		
		sizeInMeters = l.tileSizeInMeters * sizeRelativeToTile;
		
		CircleShape boundingBox = new CircleShape();
		boundingBox.setRadius(sizeInMeters / 2);
		setShape(boundingBox);
		
		//Setup collision filtering
		Filter filter = new Filter();
		filter.categoryBits = Entity.PLAYER;
		filter.maskBits = Entity.SOLID_TILE | Entity.BOMB | Entity.LEVEL_BOUNDARY | Entity.PLAYER;
		
		body.getFixtureList().setFilterData(filter);
	}
	
	private void loadSprite() throws SlickException
	{
		sprite = new Image(SPRITE_DIRECTORY + "default" + File.separator + "sprite1.png");
	}
	
	public void render(Graphics g)
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
		
		sprite.draw(getPixelsX() - getSizeInPixels() / 2, getPixelsY() - getSizeInPixels() / 2, 
					getSizeInPixels(), getSizeInPixels(), color);
	}
	
	public void update(GameContainer gc)
	{	
		if(!isActive())
		{
			timeSinceDeath++;
			return;
		}
		
		//Movement
		Vec2 currentVelocity = body.getLinearVelocity();
		
	    float desiredXVel = 0;
	    float desiredYVel = 0;
	    
	    //System.out.println("JoystickY: " + joystickY);
	    
	    Input input = gc.getInput();
	    
		if(dPadDown || input.isKeyDown(Input.KEY_S))
		{
			//DOWN
			desiredYVel = Math.max(currentVelocity.y - 0.1f, speed);
		}
		if(dPadLeft || input.isKeyDown(Input.KEY_A))
		{
			//LEFT
			desiredXVel = Math.min(currentVelocity.x + 0.1f, -speed);
		}
		if(dPadUp || input.isKeyDown(Input.KEY_W))
		{
			//UP
			desiredYVel = Math.min(currentVelocity.y + 0.1f, -speed);
		}
		if(dPadRight || input.isKeyDown(Input.KEY_D))
		{
			//RIGHT
			desiredXVel = Math.max(currentVelocity.x - 0.1f, speed);
		}
		
	    float velChangeX = desiredXVel - currentVelocity.x;
	    float impulseX = body.getMass() * velChangeX; //disregard time factor
	    
	    float velChangeY = desiredYVel - currentVelocity.y;
	    float impulseY = body.getMass() * velChangeY; //disregard time factor
		
		body.applyLinearImpulse(new Vec2(impulseX, impulseY), body.getWorldCenter());
		
		//Drop bombs
		
		if(input.isKeyDown(Input.KEY_SPACE))
		{
			dropBomb = true;
		}
		
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
		if(health <= 0)
		{
			destroy();
		}
	}
	
	public void respawn(float x, float y)
	{
		body.setTransform(new Vec2(x, y), body.getAngle());
		
		health = maxHealth;
		setActive(true);
	}
	
	public void destroy()
	{
		setActive(false);
		
		numDeaths++;
		timeSinceDeath++;
	}
	
	public float getPixelsX()
	{
		return level.metersToPixels(body.getPosition().x);
	}
	
	public float getPixelsY()
	{
		return level.metersToPixels(body.getPosition().y);
	}
	
	public float getSizeInPixels()
	{
		return level.metersToPixels(sizeInMeters);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
