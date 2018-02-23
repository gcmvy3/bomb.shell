package main;

import java.util.Objects;

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
	
	@Override
	public boolean equals(Object o) 
	{
	    // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;

	    RayCollision other = (RayCollision)o;
	    // field comparison
	    return Objects.equals(entity, other.entity);
	}
}
