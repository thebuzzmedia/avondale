package com.kallasoft.avondale.component.connection;

import java.awt.BasicStroke;
import java.awt.Stroke;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ConnectedComponent;

public interface Connection extends Component
{
	public static final double DEFAULT_CONNECTION_WIDTH = 1;

	public static final Stroke SOLID_LINE_STROKE = new BasicStroke(
			(float) DEFAULT_CONNECTION_WIDTH);
	public static final Stroke DASHED_LINE_STROKE = new BasicStroke(
			(float) DEFAULT_CONNECTION_WIDTH, BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_MITER, 10, new float[] { 2, 2 }, 0);

	public static final String DIRECTED_PROPERTY_NAME = "directed";
	public static final String CONNECTION_STROKE_PROPERTY_NAME = "connectionStroke";
	public static final String SOURCE_COMPONENT_PROPERTY_NAME = "sourceComponent";
	public static final String DESTINATION_COMPONENT_PROPERTY_NAME = "destinationComponent";

	public boolean isDirected();

	public void setDirected(boolean directed);

	public double getConnectionWidth();

	public Stroke getConnectionStroke();

	public void setConnectionStroke(Stroke stroke);

	public void setConnectionStroke(double connectionWidth, Stroke stroke);

	public ConnectedComponent getSourceComponent();

	public void setSourceComponent(ConnectedComponent sourceComponent);

	public ConnectedComponent getDestinationComponent();

	public void setDestinationComponent(ConnectedComponent destinationComponent);
}