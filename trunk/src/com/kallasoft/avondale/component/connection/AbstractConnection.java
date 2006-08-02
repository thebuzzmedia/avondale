package com.kallasoft.avondale.component.connection;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.logging.Logger;

import com.kallasoft.avondale.component.ConnectedComponent;
import com.kallasoft.avondale.component.DefaultComponent;
import com.kallasoft.avondale.component.model.ComponentModel;

public abstract class AbstractConnection extends DefaultComponent implements
		Connection
{
	private boolean directed = false;
	private double connectionWidth = DEFAULT_CONNECTION_WIDTH;
	private Stroke connectionStroke = SOLID_LINE_STROKE;
	private ConnectedComponent sourceComponent;
	private ConnectedComponent destinationComponent;

	public AbstractConnection(ComponentModel componentModel,
			ConnectedComponent sourceComponent,
			ConnectedComponent destinationComponent)
	{
		super(componentModel);
		setSourceComponent(sourceComponent);
		setDestinationComponent(destinationComponent);
	}

	public boolean isDirected()
	{
		return directed;
	}

	public void setDirected(boolean directed)
	{
		boolean oldDirected = isDirected();
		
		if (oldDirected == directed)
			return;

		this.directed = directed;
		firePropertyChangeEvent(this, DIRECTED_PROPERTY_NAME, oldDirected,
				isDirected());
	}

	public double getConnectionWidth()
	{
		return connectionWidth;
	}

	public Stroke getConnectionStroke()
	{
		return connectionStroke;
	}

	public void setConnectionStroke(Stroke connectionStroke)
	{
		if (connectionStroke instanceof BasicStroke)
			setConnectionStroke(
					((BasicStroke) connectionStroke).getLineWidth(),
					connectionStroke);
		else
			throw new IllegalArgumentException(
					"connectionStroke must of type BasicStroke in order to use this method, otherwise use setConnectionStroke(double, Stroke) and manually set the stroke width");
	}

	public void setConnectionStroke(double connectionWidth,
			Stroke connectionStroke)
	{
		if (connectionWidth < 0)
			throw new IllegalArgumentException(
					"connectionWidth must be >= 0 and should likely be equal to the width of the line defined by the connectionStroke");

		Stroke oldConnectionStroke = getConnectionStroke();

		if (oldConnectionStroke == connectionStroke)
			return;

		if (connectionStroke instanceof BasicStroke)
		{
			float value = ((BasicStroke) connectionStroke).getLineWidth();

			if (((float) connectionWidth) != value)
				Logger
						.getLogger(getClass().getName())
						.warning(
								"The value of connectionWidth specified does not equal the returned value from ((BasicStroke)connectionStroke).getLineWidth(), these values should be equal otherwise painting anomolies can occur");
		}

		this.connectionWidth = connectionWidth;
		this.connectionStroke = connectionStroke;
		firePropertyChangeEvent(this, CONNECTION_STROKE_PROPERTY_NAME,
				oldConnectionStroke, getConnectionStroke());
	}

	public ConnectedComponent getSourceComponent()
	{
		return sourceComponent;
	}

	public void setSourceComponent(ConnectedComponent sourceComponent)
	{
		ConnectedComponent oldSourceComponent = getSourceComponent();

		if (oldSourceComponent == sourceComponent)
			return;

		/*
		 * We do the assignment here first so the getSourceComponent checks in
		 * the connected component reflect the proper state of this connection,
		 * otherwise we end up in an infinite loop where Connection calls
		 * ConnectedComponent to update, which in turns calls Connection to
		 * update and on and on.
		 */
		this.sourceComponent = sourceComponent;

		/* Remove the outbound connection from the source component */
		if (oldSourceComponent != null)
			oldSourceComponent.removeOutboundConnection(this);

		/* Add an outbound connection to the new source component */
		if (this.sourceComponent != null)
			this.sourceComponent.addOutboundConnection(this);

		firePropertyChangeEvent(this, SOURCE_COMPONENT_PROPERTY_NAME,
				oldSourceComponent, getSourceComponent());
	}

	public ConnectedComponent getDestinationComponent()
	{
		return destinationComponent;
	}

	public void setDestinationComponent(ConnectedComponent destinationComponent)
	{
		ConnectedComponent oldDestinationComponent = getDestinationComponent();

		if (oldDestinationComponent == destinationComponent)
			return;

		/*
		 * We do the assignment here first so the getSourceComponent checks in
		 * the connected component reflect the proper state of this connection,
		 * otherwise we end up in an infinite loop where Connection calls
		 * ConnectedComponent to update, which in turns calls Connection to
		 * update and on and on.
		 */
		this.destinationComponent = destinationComponent;

		if (oldDestinationComponent != null)
			oldDestinationComponent.removeInboundConnection(this);

		if (this.destinationComponent != null)
			this.destinationComponent.addInboundConnection(this);

		firePropertyChangeEvent(this, DESTINATION_COMPONENT_PROPERTY_NAME,
				oldDestinationComponent, getDestinationComponent());
	}
}