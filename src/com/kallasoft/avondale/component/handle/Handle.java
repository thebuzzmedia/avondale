package com.kallasoft.avondale.component.handle;

import com.kallasoft.avondale.component.ResizableComponent;

public interface Handle extends ResizableComponent
{
	public static enum Coordinate
	{
		CENTER, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST
	}

	public Coordinate getCoordinate();
}