package main;

import java.awt.Dimension;

public class Resolution extends Dimension
{
	private static final long serialVersionUID = 5278740914739384841L;
	
	public Resolution(int width, int height)
	{
		super(width, height);
	}
	
	@Override
	public String toString()
	{
		return width + " x " + height;
	}
}
