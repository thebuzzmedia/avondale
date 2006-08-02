package com.kallasoft.avondale.component.connection;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.ConnectedComponent;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.DefaultConnectionPainter;
import com.kallasoft.avondale.component.util.ComponentUtils;

public class DefaultConnection extends AbstractConnection
{
	private Line2D line;

	public DefaultConnection()
	{
		this(null);
	}

	public DefaultConnection(ComponentModel componentModel)
	{
		this(componentModel, null, null);
	}

	public DefaultConnection(ConnectedComponent sourceComponent,
			ConnectedComponent destinationComponent)
	{
		this(null, sourceComponent, destinationComponent);
	}
	
	public DefaultConnection(ComponentModel componentModel, ConnectedComponent sourceComponent,
			ConnectedComponent destinationComponent)
	{
		super(componentModel, sourceComponent, destinationComponent);
		setComponentPainter(DefaultConnectionPainter.getInstance());
	}

	@Override
	public boolean contains(double x, double y)
	{
		boolean result = false;

		if (isVisible() && line != null)
		{
			double width = getConnectionWidth();
			double distance = line.ptSegDist(x, y);
			result = (distance <= width);
		}

		return result;
	}

	@Override
	public Rectangle2D getBounds()
	{
		double connectionWidth = getConnectionWidth();

		/* Get the bounds from the line if possible */
		Rectangle2D bounds = (line == null ? new Rectangle2D.Double() : line
				.getBounds2D());

		/*
		 * Since a line has no area inside of it, we factor in the width of the
		 * line here.
		 */
		bounds.setRect(bounds.getX() - connectionWidth, bounds.getY()
				- connectionWidth, bounds.getWidth() + (2 * connectionWidth),
				bounds.getHeight() + (2 * connectionWidth));

		return bounds;
	}

	@Override
	public Shape getComponentShape()
	{
		return line;
	}

	@Override
	public void validate()
	{
		/* If this component is already valid don't do anything */
		if (isValid())
			return;

		ConnectedComponent sourceComponent = getSourceComponent();
		ConnectedComponent destinationComponent = getDestinationComponent();

		/* Don't calculate anything if there aren't components on each end of
		 * this connection.
		 */
		if (sourceComponent == null || destinationComponent == null)
			return;

		Rectangle2D oldPreferredBounds = getPreferredBounds();
		
		/* Find the center of the source and destination components */
		Point2D sourceCenterLocation = ComponentUtils
				.getLocationOnComponentPanel(sourceComponent, sourceComponent
						.getCenterX(), sourceComponent.getCenterY());
		Point2D destinationCenterLocation = ComponentUtils
				.getLocationOnComponentPanel(destinationComponent,
						destinationComponent.getCenterX(), destinationComponent
								.getCenterY());

		if (line == null)
			line = new Line2D.Double();

		/* Connections are always drawn from the center of a component to the
		 * center of a another.
		 */
		line.setLine(sourceCenterLocation.getX(), sourceCenterLocation.getY(),
				destinationCenterLocation.getX(), destinationCenterLocation
						.getY());
		
		super.validate();

		/* Calculate the repaint area which is the union of the old bounds and the new one. */
		Rectangle2D preferredBounds = getPreferredBounds();
		Rectangle2D.union(preferredBounds, oldPreferredBounds, preferredBounds);

		/* Repaint the connection */
		repaint(preferredBounds);
	}
	
	@Override
	public void setSourceComponent(ConnectedComponent sourceComponent)
	{
		ConnectedComponent oldSourceComponent = getSourceComponent();

		if (oldSourceComponent == sourceComponent)
			return;
		
		/* Delegate to the super implementation */
		super.setSourceComponent(sourceComponent);

		/* Revalidate and repaint the connection */
		revalidate();
		repaint();
	}
	
	@Override
	public void setDestinationComponent(ConnectedComponent destinationComponent)
	{
		ConnectedComponent oldDestinationComponent = getDestinationComponent();

		if (oldDestinationComponent == destinationComponent)
			return;
		
		/* Delegate to the super implementation */
		super.setDestinationComponent(destinationComponent);

		/* Revalidate and repaint the connection */
		revalidate();
		repaint();
	}
}