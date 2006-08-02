package com.kallasoft.avondale.component.layout;

import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;

public class FitToChildrenContainerLayout implements ContainerLayout
{
	private static ContainerLayout instance = null;

	public static synchronized ContainerLayout getInstace()
	{
		if (instance == null)
			instance = new FitToChildrenContainerLayout();

		return instance;
	}

	public boolean layoutContainer(ContainerComponent containerComponent)
	{
		double x = Double.MAX_VALUE;
		double y = Double.MAX_VALUE;
		double width = 0;
		double height = 0;
		double value = 0;

		double oldX = containerComponent.getX();
		double oldY = containerComponent.getY();
		double oldWidth = containerComponent.getWidth();
		double oldHeight = containerComponent.getHeight();
		Component[] components = containerComponent.getComponents();

		/* If there are no children, make the x/y bounds 0 */
		if (containerComponent.getComponentCount() == 0)
		{
			x = 0;
			y = 0;
		}

		for (int i = 0; i < components.length; i++)
		{
			Component childComponent = components[i];

			/* Make sure the child is valid before using */
			if (!childComponent.isValid())
				childComponent.validate();

			Rectangle2D preferredBounds = childComponent.getPreferredBounds();

			value = preferredBounds.getX();

			if (value < x)
				x = value;

			value = preferredBounds.getY();

			if (value < y)
				y = value;

			value = preferredBounds.getWidth();

			if (value > width)
				width = value;

			value = preferredBounds.getHeight();

			if (value > height)
				height = value;
		}

		containerComponent.setBounds(x, y, width, height);

		/*
		 * Determine if the layout changed at all and return true, otherwise
		 * false
		 */
		return (x != oldX || y != oldY || width != oldWidth || height != oldHeight);
	}
}