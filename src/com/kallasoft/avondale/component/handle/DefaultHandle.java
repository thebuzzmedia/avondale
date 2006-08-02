package com.kallasoft.avondale.component.handle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import com.kallasoft.avondale.component.AdjustableComponent;
import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.component.DraggableComponent;
import com.kallasoft.avondale.component.border.ComponentBorder;
import com.kallasoft.avondale.component.border.LineComponentBorder;
import com.kallasoft.avondale.component.handle.Handle.Coordinate;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.DefaultComponentPainter;

/**
 * Class that provides a default implementation for the <code>Handle</code> interface.
 * This class provides custom mouse cursor support for all the coordinates as defined
 * in the <code>Handle</code> interface as well as resizing code based on the adjustment
 * of those coordinates. The default rendering of these handles is as a 6x6 pixel white
 * box with a black border most comparable to the resize handles in any Microsoft product
 * like PowerPoint, Visio or Word even.
 * <p>
 * If an implementor wanted to add custom coordinates that in
 * turn would have custom cursors or custom resizing behavior they would most like
 * extend this class or replace it with a custom implementation. When working on
 * a custom implementation a custom mouse cursor is optional, if one is not
 * provided then the property will be ignored. An implementor would need to focus
 * first and foremost on the dragging behavior of the handle based on it's coordinate
 * and how that would effect the <code>Component</code> it was associated with.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public class DefaultHandle extends AbstractHandle
{
	public static final double DEFAULT_WIDTH = 6;
	public static final double DEFAULT_HEIGHT = 6;
	public static final ComponentBorder DEFAULT_BORDER = new LineComponentBorder();
	public static final Paint DEFAULT_BACKGROUND_PAINT = Color.WHITE;

	public DefaultHandle()
	{
		this(Coordinate.CENTER);
	}

	public DefaultHandle(Coordinate coordinate)
	{
		this(null, coordinate);
	}

	public DefaultHandle(ComponentModel componentModel, Coordinate coordinate)
	{
		super(componentModel, coordinate);

		/* Update the mouse cursor */
		switch (coordinate)
		{
			case CENTER:
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				break;

			case NORTH_WEST:setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
				break;

			case NORTH:setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				break;

			case NORTH_EAST:setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				break;

			case EAST:setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				break;

			case SOUTH_EAST:setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				break;

			case SOUTH:setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				break;

			case SOUTH_WEST:setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
				break;

			case WEST:setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				break;
		}

		/* Setup the default size and style information for the handle */
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setComponentBorder(DEFAULT_BORDER);
		setBackgroundPaint(DEFAULT_BACKGROUND_PAINT);
		setComponentPainter(DefaultComponentPainter.getInstance());
		
		/* Add custom listeners */
		addMouseListener(new HandleMouseHandler());
		addMouseMotionListener(new HandleMouseMotionHandler());
	}
	
	private class HandleMouseHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent evt)
		{
			AdjustableComponent parentComponent = (AdjustableComponent) getParentComponent();

			/* Notify the parent of the new active handle if the handle has a reference to it's parent */
			if (parentComponent != null)
				parentComponent.setActiveHandle(evt.getX(), evt.getY(),
						DefaultHandle.this);
		}

		@Override
		public void mouseReleased(MouseEvent evt)
		{
			AdjustableComponent parentComponent = (AdjustableComponent) getParentComponent();

			/* Clear the parent's active handle reference */
			if (parentComponent != null)
				parentComponent.setActiveHandle(
						DraggableComponent.UNSET_OFFSET_VALUE,
						DraggableComponent.UNSET_OFFSET_VALUE, null);
		}
	}

	/* TODO: Need to take a new approach on how to implement the resizing with dragging
	 * the handle */
	private class HandleMouseMotionHandler extends MouseMotionAdapter
	{
		@Override
		public void mouseDragged(MouseEvent evt)
		{
			double x = getX();
			double y = getY();
			double width = getWidth();
			double height = getHeight();
			double[] mousePressedXYOffsets = getMousePressedXYOffsets();
			double[] mousePressedWidthHeightOffsets = getMousePressedWidthHeightOffsets();
			Coordinate coordinate = activeHandle.getCoordinate();

			switch (coordinate)
			{
				case CENTER:
					x = evt.getX()
							- mousePressedXYOffsets[MOUSE_X_OFFSET_INDEX];
					y = evt.getY()
							- mousePressedXYOffsets[MOUSE_Y_OFFSET_INDEX];

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						x -= (x % verticalGridSpacing);
						y -= (y % horizontalGridSpacing);
					}
					break;
				case NORTH_WEST:
					x = evt.getX()
							- mousePressedXYOffsets[MOUSE_X_OFFSET_INDEX];
					y = evt.getY()
							- mousePressedXYOffsets[MOUSE_Y_OFFSET_INDEX];
					width = getWidth() + (getX() - x);
					height = getHeight() + (getY() - y);

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						double xDiff = (x % verticalGridSpacing);
						double yDiff = (y % horizontalGridSpacing);
						x -= xDiff;
						y -= yDiff;
						width += xDiff;
						height += yDiff;
					}
					break;
				case NORTH:
					y = evt.getY()
							- mousePressedXYOffsets[MOUSE_Y_OFFSET_INDEX];
					height = getHeight() + (getY() - y);

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						double yDiff = (y % horizontalGridSpacing);
						y -= yDiff;
						height += yDiff;
					}
					break;
				case NORTH_EAST:
					y = evt.getY()
							- mousePressedXYOffsets[MOUSE_Y_OFFSET_INDEX];
					width = evt.getX()
							- getX()
							+ mousePressedWidthHeightOffsets[MOUSE_WIDTH_OFFSET_INDEX];
					height = getHeight() + (getY() - y);

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						double yDiff = (y % horizontalGridSpacing);
						y -= yDiff;
						width -= (width % verticalGridSpacing);
						height += yDiff;
					}
					break;
				case EAST:
					width = evt.getX()
							- getX()
							+ mousePressedWidthHeightOffsets[MOUSE_WIDTH_OFFSET_INDEX];

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						width -= (width % verticalGridSpacing);
					}
					break;
				case SOUTH_EAST:
					width = evt.getX()
							- getX()
							+ mousePressedWidthHeightOffsets[MOUSE_WIDTH_OFFSET_INDEX];
					height = evt.getY()
							- getY()
							+ mousePressedWidthHeightOffsets[MOUSE_HEIGHT_OFFSET_INDEX];

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						width -= (width % verticalGridSpacing);
						height -= (height % horizontalGridSpacing);
					}
					break;
				case SOUTH:
					height = evt.getY()
							- getY()
							+ mousePressedWidthHeightOffsets[MOUSE_HEIGHT_OFFSET_INDEX];

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						height -= (height % horizontalGridSpacing);
					}
					break;
				case SOUTH_WEST:
					x = evt.getX()
							- mousePressedXYOffsets[MOUSE_X_OFFSET_INDEX];
					width = getWidth() + (getX() - x);
					height = evt.getY()
							- getY()
							+ mousePressedWidthHeightOffsets[MOUSE_HEIGHT_OFFSET_INDEX];

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						double xDiff = (x % verticalGridSpacing);
						x -= xDiff;
						width += xDiff;
						height -= (height % horizontalGridSpacing);
					}
					break;
				case WEST:
					x = evt.getX()
							- mousePressedXYOffsets[MOUSE_X_OFFSET_INDEX];
					width = getWidth() + (getX() - x);

					/* Honor snapToGrid effect */
					if (snapToGridEnabled)
					{
						double xDiff = (x % verticalGridSpacing);
						x -= xDiff;
						width += xDiff;
					}
					break;
			}

			/*
			 * Honor min/max bounds. Because x & width as well as y & height
			 * effect the size and location of the component together, they need
			 * to be checked at the same time, if either is in violation then
			 * both need to be reset. This helps to avoid very strange resizing
			 * behaviors.
			 */
			if (x < getMinX() || x > getMaxX() || width < getMinWidth()
					|| width > getMaxWidth())
			{
				x = getX();
				width = getWidth();
			}

			if (y < getMinY() || y > getMaxY() || height < getMinHeight()
					|| height > getMaxHeight())
			{
				y = getY();
				height = getHeight();
			}

			setBounds(x, y, width, height);
		}
	}
}