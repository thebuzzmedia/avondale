package com.kallasoft.avondale.component.layout;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.panel.ComponentPanel;

public class RootComponentLayout implements ContainerLayout
{
	private static ContainerLayout instance = null;

	public static synchronized ContainerLayout getInstace()
	{
		if (instance == null)
			instance = new RootComponentLayout();

		return instance;
	}

	public boolean layoutContainer(ContainerComponent containerComponent)
	{
		double width = 0;
		double height = 0;
		double value = 0;

		double oldWidth = containerComponent.getWidth();
		double oldHeight = containerComponent.getHeight();
		Component[] components = containerComponent.getComponents();

		for (int i = 0; i < components.length; i++)
		{
			Component childComponent = components[i];

			/* Make sure the child is valid before using */
			if (!childComponent.isValid())
				childComponent.validate();

			Rectangle2D preferredBounds = childComponent.getPreferredBounds();

			value = preferredBounds.getX() + preferredBounds.getWidth();

			if (value > width)
				width = value;

			value = preferredBounds.getY() + preferredBounds.getHeight();

			if (value > height)
				height = value;
		}

		/*
		 * Make sure the rootComponent atleast fills the visible area of the
		 * componentPanel.
		 */
		ComponentPanel componentPanel = containerComponent.getComponentPanel();

		if (componentPanel != null)
		{
			Rectangle visibleRectangle = componentPanel.getVisibleRect();

			value = visibleRectangle.getWidth();

			if (value > width)
				width = value;

			value = visibleRectangle.getHeight();

			if (value > height)
				height = value;
		}

		containerComponent.setSize(width, height);

		/*
		 * Determine if the layout changed at all and return true, otherwise
		 * false
		 */
		return (width != oldWidth || height != oldHeight);
	}
}