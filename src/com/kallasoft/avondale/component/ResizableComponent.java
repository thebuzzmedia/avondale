package com.kallasoft.avondale.component;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface ResizableComponent extends Component
{
	public static final String MOVABLE_PROPERTY_NAME = "movable";
	public static final String RESIZABLE_PROPERTY_NAME = "resizable";
	public static final String LOCATION_PROPERTY_NAME = "location";
	public static final String SIZE_PROPERTY_NAME = "size";
	public static final String BOUNDS_PROPERTY_NAME = "bounds";

	public boolean isMovable();

	public void setMovable(boolean movable);

	public boolean isResizable();

	public void setResizable(boolean resizable);

	public void setLocation(double x, double y);

	public void setLocation(Point2D location);

	public void setSize(double width, double height);

	public void setSize(Dimension2D size);

	public void setBounds(double x, double y, double width, double height);

	public void setBounds(Rectangle2D bounds);
}