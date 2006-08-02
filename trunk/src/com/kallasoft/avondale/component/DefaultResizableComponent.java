package com.kallasoft.avondale.component;

import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.DefaultComponentPainter;

public class DefaultResizableComponent extends AbstractResizableComponent
{
	public DefaultResizableComponent()
	{
		this(null);
	}

	public DefaultResizableComponent(ComponentModel componentModel)
	{
		super(componentModel);
		setComponentPainter(DefaultComponentPainter.getInstance());
	}

	@Override
	public void setLocation(double x, double y)
	{
		if (!isMovable())
			return;

		if (getX() == x && getY() == y)
			return;

		/* Remember the original bounds */
		Rectangle2D oldPreferredBounds = getPreferredBounds();

		super.setLocation(x, y);

		/*
		 * Invalidate the component now that the location has changed as this
		 * might effect layout or other properties (this will implicitly
		 * invalidate the parent).
		 */
		invalidate();

		/* Repaint the union of the old location and the new location */
		Rectangle2D preferredBounds = getPreferredBounds();
		Rectangle2D.union(preferredBounds, oldPreferredBounds, preferredBounds);
		repaint(preferredBounds);
	}

	@Override
	public void setSize(double width, double height)
	{
		if (!isResizable())
			return;

		if (getWidth() == width && getHeight() == height)
			return;

		/* Remember the original bounds */
		Rectangle2D oldPreferredBounds = getPreferredBounds();

		super.setSize(width, height);

		/*
		 * Invalidate the component now that the location has changed as this
		 * might effect layout or other properties (this will implicitly
		 * invalidate the parent).
		 */
		invalidate();

		/* Repaint the union of the old size and the new size */
		Rectangle2D preferredBounds = getPreferredBounds();
		Rectangle2D.union(preferredBounds, oldPreferredBounds, preferredBounds);
		repaint(preferredBounds);
	}

	@Override
	public void setBounds(double x, double y, double width, double height)
	{
		if (getX() == x && getY() == y && getWidth() == width
				&& getHeight() == height)
			return;

		if (!isMovable() && !isResizable())
			return;

		/* Remember the original bounds */
		Rectangle2D oldPreferredBounds = getPreferredBounds();

		super.setBounds(x, y, width, height);

		/*
		 * Invalidate the component now that the location has changed as this
		 * might effect layout or other properties (this will implicitly
		 * invalidate the parent).
		 */
		invalidate();

		/* Repaint the union of the old bounds and the new bounds */
		Rectangle2D preferredBounds = getPreferredBounds();
		Rectangle2D.union(preferredBounds, oldPreferredBounds, preferredBounds);
		repaint(preferredBounds);
	}
}