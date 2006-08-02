package com.kallasoft.avondale.component;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.panel.ComponentPanel;

public abstract class AbstractResizableComponent extends DefaultComponent
		implements ResizableComponent
{
	private boolean movable = true;
	private boolean resizable = true;

	public AbstractResizableComponent(ComponentModel componentModel)
	{
		super(componentModel);
	}

	public boolean isMovable()
	{
		return movable;
	}

	public void setMovable(boolean movable)
	{
		boolean oldMovable = isMovable();

		if (oldMovable == movable)
			return;

		this.movable = movable;
		firePropertyChangeEvent(this, MOVABLE_PROPERTY_NAME, oldMovable,
				isMovable());
	}

	public boolean isResizable()
	{
		return resizable;
	}

	public void setResizable(boolean resizable)
	{
		boolean oldResizable = isResizable();

		if (oldResizable == resizable)
			return;

		this.resizable = resizable;
		firePropertyChangeEvent(this, RESIZABLE_PROPERTY_NAME, oldResizable,
				isResizable());
	}

	public void setLocation(double x, double y)
	{
		if (!isMovable())
			return;

		if (getX() == x && getY() == y)
			return;

		Point2D oldLocation = getLocation();
		bounds.setRect(x, y, getWidth(), getHeight());
		firePropertyChangeEvent(this, LOCATION_PROPERTY_NAME, oldLocation,
				getLocation());
	}

	public void setLocation(Point2D location)
	{
		setLocation(location.getX(), location.getY());
	}

	public void setSize(double width, double height)
	{
		if (!isResizable())
			return;

		if (getWidth() == width && getHeight() == height)
			return;

		Dimension2D oldSize = getSize();
		bounds.setRect(getX(), getY(), width, height);
		firePropertyChangeEvent(this, SIZE_PROPERTY_NAME, oldSize, getSize());
	}

	public void setSize(Dimension2D size)
	{
		setSize(size.getWidth(), size.getHeight());
	}

	public void setBounds(double x, double y, double width, double height)
	{
		if (getX() == x && getY() == y && getWidth() == width
				&& getHeight() == height)
			return;

		if (!isMovable() && !isResizable())
			return;

		if (!isMovable())
		{
			x = getX();
			y = getY();
		}
		else if (!isResizable())
		{
			width = getWidth();
			height = getHeight();
		}

		Rectangle2D oldBounds = getBounds();
		bounds.setRect(x, y, width, height);
		firePropertyChangeEvent(this, BOUNDS_PROPERTY_NAME, oldBounds,
				getBounds());
	}

	public void setBounds(Rectangle2D bounds)
	{
		setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds
				.getHeight());
	}

	protected KeyListener createInitialKeyListener()
	{
		return new DirectionKeyHandler();
	}

	protected class DirectionKeyHandler implements KeyListener
	{
		public void keyPressed(KeyEvent evt)
		{
			int keyCode = evt.getKeyCode();

			if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_RIGHT
					|| keyCode == KeyEvent.VK_DOWN
					|| keyCode == KeyEvent.VK_LEFT)
			{
				boolean snapToGridEnabled = false;
				double x = getX();
				double y = getY();
				ComponentPanel componentPanel = getComponentPanel();

				if (componentPanel != null)
				{
					snapToGridEnabled = componentPanel.isSnapToGridEnabled();

					switch (evt.getKeyCode())
					{
						case KeyEvent.VK_UP:
							y--;

							/* Honor snapToGrid effect */
							if (snapToGridEnabled)
								y -= (y % componentPanel
										.getHorizontalGridSpacing());
							break;

						case KeyEvent.VK_RIGHT:
							/* Honor snapToGrid effect */
							if (snapToGridEnabled)
							{
								double distance = (x % componentPanel
										.getVerticalGridSpacing());

								if (distance == 0)
									distance = componentPanel
											.getVerticalGridSpacing();

								x += distance;
							}
							else
								x++;
							break;

						case KeyEvent.VK_DOWN:
							/* Honor snapToGrid effect */
							if (snapToGridEnabled)
							{
								double distance = (y % componentPanel
										.getHorizontalGridSpacing());

								if (distance == 0)
									distance = componentPanel
											.getHorizontalGridSpacing();

								y += distance;
							}
							else
								y++;
							break;

						case KeyEvent.VK_LEFT:
							x--;

							/* Honor snapToGrid effect */
							if (snapToGridEnabled)
								x -= (x % componentPanel
										.getVerticalGridSpacing());
							break;
					}
				}

				/* Update the component's location */
				setLocation(x, y);
			}
		}

		public void keyReleased(KeyEvent evt)
		{
			/* no-op impl */
		}

		public void keyTyped(KeyEvent evt)
		{
			/* no-op impl */
		}
	}
}