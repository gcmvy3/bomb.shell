package main;

public class RayCollision implements Comparable<RayCollision>
{
	public Entity entity;
	public float fraction;
	
	public RayCollision(Entity e, float f)
	{
		entity = e;
		fraction = f;
	}

	@Override
	public int compareTo(RayCollision other) 
	{
		return (int) Math.signum(fraction - other.fraction);
	}
}
