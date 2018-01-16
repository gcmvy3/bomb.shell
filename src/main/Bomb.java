package main;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bomb extends Entity
{
	final float SIZE_RELATIVE_TO_TILE = 0.5f;

	final String PATH_TO_SPRITE = "assets/sprites/bomb1.png";
	
	int delay = 100;
	int age = 0;
	int damage = 100;
	float explosionRadius;
	
	float sizeInMeters;
	float sizeInPixels;
	
	Player parent;
	
	public Image sprite;
	
	public Bomb(float x, float y, Level l, Player parent) throws SlickException 
	{
		super(x, y, l);
		
		this.parent = parent;
		
		sizeInMeters = l.tileSizeInMeters * SIZE_RELATIVE_TO_TILE;
		sizeInPixels = l.metersToPixels(sizeInMeters);
		
		sprite = new Image(PATH_TO_SPRITE);
	}
	
	public void update()
	{
		age++;
		if(age >= delay)
		{
			explode();
		}
	}
	
	public void render()
	{
		int pixelsX = (int)level.metersToPixels(body.getPosition().x);
		int pixelsY = (int)level.metersToPixels(body.getPosition().y);
		
		sprite.draw(pixelsX - sizeInPixels / 2, pixelsY - sizeInPixels / 2, sizeInPixels, sizeInPixels);
	}
	
	private void explode()
	{
		alive = false;
	}
}
