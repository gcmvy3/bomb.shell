package main;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bomb extends Entity
{
	final float SIZE_RELATIVE_TO_TILE = 0.5f;
	final float EXPLOSION_THICKNESS_RELATIVE_TO_TILE = 0.6f;
	final int RADIUS_IN_BLOCKS = 4;
	
	final String PATH_TO_SPRITE = "assets/sprites/bomb1.png";
	
	int delay = 100;
	int age = 0;
	int damage = 100;
	float explosionRadius;
	float explosionThickness;
	
	float sizeInMeters;
	float sizeInPixels;
	
	Player parent;
	
	public Image sprite;
	
	AABB[] explosionShape;
	
	public Bomb(float x, float y, Level l, Player parent) throws SlickException 
	{
		super(x, y, l);
		
		this.parent = parent;
		
		sizeInMeters = l.tileSizeInMeters * SIZE_RELATIVE_TO_TILE;
		sizeInPixels = l.metersToPixels(sizeInMeters);
		
		sprite = new Image(PATH_TO_SPRITE);
		
		initExplosionShape();
	}
	
	private void initExplosionShape()
	{
		explosionRadius = level.tileSizeInMeters * RADIUS_IN_BLOCKS;
		explosionThickness = level.tileSizeInMeters * EXPLOSION_THICKNESS_RELATIVE_TO_TILE;
		
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		
		explosionShape = new AABB[2];
		
		Vec2 horizontalBottomLeft = new Vec2(x - explosionRadius, y - explosionThickness / 2);
		Vec2 horizontalTopRight = new Vec2(x + explosionRadius, y + explosionThickness / 2);
		
		Vec2 verticalBottomLeft = new Vec2(x - explosionThickness / 2, y - explosionRadius);
		Vec2 verticalTopRight = new Vec2(x + explosionThickness / 2, y + explosionRadius);
		
		AABB horizontal = new AABB(horizontalBottomLeft, horizontalTopRight);
		AABB vertical = new AABB(verticalBottomLeft, verticalTopRight);
		
		explosionShape[0] = horizontal;
		explosionShape[1] = vertical;
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
		Vec2[] rays = new Vec2[4];
		
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		
		Vec2 downRay = new Vec2(x, y + explosionRadius);
		Vec2 upRay = new Vec2(x, y - explosionRadius);
		Vec2 leftRay = new Vec2(x - explosionRadius, y);
		Vec2 rightRay = new Vec2(x + explosionRadius, y);
		
		rays[0] = downRay;
		rays[1] = upRay;
		rays[2] = leftRay;
		rays[3] = rightRay;
		
		Explosion explosion = new Explosion(x, y, level, damage, rays, explosionThickness);
		level.explosions.add(explosion);
		
		active = false;
	}
}
