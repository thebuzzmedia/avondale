package com.kallasoft.avondale.component.border;

import com.kallasoft.avondale.component.Component;

public abstract class AbstractComponentBorder implements ComponentBorder
{
	protected double borderWidth = 0;

	public double getBorderWidth(Component component)
	{
		return borderWidth;
	}
}