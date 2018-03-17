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

import self.totality.webSocketServer.controller.DPad;

public class Player extends Entity
{
	final String SPRITE_DIRECTORY = "assets" + File.separator + "characters" + File.separator;

	final float TOP_SPEED = 7.5f;
	final float ACCELERATION = 0.3f;
	
	final float sizeRelativeToTile = 0.8f;
	
	float sizeInMeters;
	
	public int dPadDirection = 0;
	
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
	    
	    Input input = gc.getInput();
	    
	    //Right is positive
	    //Down is positive
	    
		if(dPadDirection == DPad.DOWN || input.isKeyDown(Input.KEY_S))
		{	
			//DOWN
			if(currentVelocity.y > 0 && currentVelocity.y < TOP_SPEED / 2)
			{
				desiredYVel = Math.min(currentVelocity.y + ACCELERATION, TOP_SPEED);
			}
			else
			{
				desiredYVel = Math.min(currentVelocity.y + ACCELERATION * 2, TOP_SPEED);
			}
		}
		if(dPadDirection == DPad.LEFT || input.isKeyDown(Input.KEY_A))
		{
			//LEFT
			if(currentVelocity.x < 0 && currentVelocity.x > -TOP_SPEED / 2)
			{
				desiredXVel = Math.max(currentVelocity.x - ACCELERATION, -TOP_SPEED);
			}
			else
			{
				desiredXVel = Math.max(currentVelocity.x - ACCELERATION * 2, -TOP_SPEED);
			}
		}
		if(dPadDirection == DPad.UP || input.isKeyDown(Input.KEY_W))
		{
			//UP
			if(currentVelocity.y < 0 && currentVelocity.y > -TOP_SPEED / 2)
			{
				desiredYVel = Math.max(currentVelocity.y - ACCELERATION, -TOP_SPEED);
			}
			else
			{
				desiredYVel = Math.max(currentVelocity.y - ACCELERATION * 2, -TOP_SPEED);
			}
		}
		if(dPadDirection == DPad.RIGHT || input.isKeyDown(Input.KEY_D))
		{
			//RIGHT
			if(currentVelocity.x > 0 && currentVelocity.x < TOP_SPEED / 2)
			{
				desiredXVel = Math.min(currentVelocity.x + ACCELERATION, TOP_SPEED);
			}
			else
			{
				desiredXVel = Math.min(currentVelocity.x + ACCELERATION * 2, TOP_SPEED);
			}
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
