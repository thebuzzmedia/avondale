package com.kallasoft.avondale.component;

import java.util.ArrayList;
import java.util.List;

import com.kallasoft.avondale.component.handle.Handle;
import com.kallasoft.avondale.component.handle.Handle.Coordinate;
import com.kallasoft.avondale.component.model.ComponentModel;

public class DefaultAdjustableComponent extends AbstractAdjustableComponent
{
	private List<Handle> handleList;

	public DefaultAdjustableComponent()
	{
		this(null);
	}

	public DefaultAdjustableComponent(ComponentModel componentModel)
	{
		this(componentModel, null);
	}

	public DefaultAdjustableComponent(ComponentModel componentModel,
			ContainerComponent parentComponent)
	{
		super(componentModel, parentComponent);

		handleList = new ArrayList<Handle>(0);
	}

	/**
	 * This method is overridden in order to update the positions of the handles
	 * after the layout (which can effect bounds) of this containing
	 * <code>Component</code> changes.
	 */
	@Override
	public void layoutContainer()
	{
		super.layoutContainer();
		updateHandlePositions();
	}

	/**
	 * This method is overridden in order to update the positions of the handles
	 * after the size of this containing <code>Component</code> changes.
	 * 
	 * @param width
	 *            The new width of this <code>Component</code>.
	 * @param height
	 *            The new height of this <code>Component</code>.
	 */
	@Override
	public void setSize(double width, double height)
	{
		super.setSize(width, height);
		updateHandlePositions();
	}

	/**
	 * This method is overridden in order to update the positions of the handles
	 * after the bounds of this containing <code>Component</code> changes.
	 * 
	 * @param x
	 *            The new x coordinate of this <code>Component</code>.
	 * @param y
	 *            The new y coordinate of this <code>Component</code>.
	 * @param width
	 *            The new width of this <code>Component</code>.
	 * @param height
	 *            The new height of this <code>Component</code>.
	 */
	@Override
	public void setBounds(double x, double y, double width, double height)
	{
		super.setBounds(x, y, width, height);
		updateHandlePositions();
	}

	/**
	 * This method is overridden in order to examin the <code>Component</code>
	 * being added to see if it is an instance of a <code>Handle</code> or
	 * not. If it is an instance of <code>Handle</code> then the instance is
	 * added to an internal list of handles in this class that is used for
	 * faster access when working with handles.
	 * 
	 * @param index
	 *            The location in the list where the <code>Component</code>
	 *            will be inserted.
	 * @param component
	 *            The <code>Component</code> instance that will be added to
	 *            this <code>Component</code>.
	 */
	@Override
	public void addComponent(int index, Component component)
	{
		if (component == null)
			throw new NullPointerException("component cannot be null");

		super.addComponent(index, component);

		if (component instanceof Handle)
		{
			handleList.add((Handle) component);
			updateHandlePosition((Handle) component);
		}
	}

	/**
	 * This method is overridden in order to examin the <code>Component</code>
	 * being removed to see if it is an instance of a <code>Handle</code> or
	 * not. If it is an instance of <code>Handle</code> then the instance is
	 * removed to an internal list of handles in this class that is used for
	 * faster access when working with handles.
	 * 
	 * @param index
	 *            The location in the list of the <code>Component</code> that
	 *            will be removed.
	 */
	@Override
	public void removeComponent(int index)
	{
		Object object = getComponent(index);

		if (object == null)
			return;

		super.removeComponent(index);

		if (object instanceof Handle)
			handleList.remove(object);
	}

	/**
	 * Method used to update the positions of all the handles associated with
	 * this component. This method delegates to the
	 * <code>updateHandlePosition(Handle handle)</code> method for each
	 * <code>Handle</code> associated with this component.
	 */
	protected void updateHandlePositions()
	{
		for (int i = 0, size = handleList.size(); i < size; i++)
			updateHandlePosition(handleList.get(i));
	}

	/**
	 * Method used to update the position of a single handle.
	 * <p>
	 * A handle's position is determined by it's coordinate. Given it's
	 * coordinate and the dimensions of the parentComponent the handle is given
	 * it's new location within that component. For example along an edge, in
	 * the center, etc. If an implemented defined more coordinates then what are
	 * already defined by a <code>Handle</code>, they would also want to add
	 * a custom implementation of this method which would look at those custom
	 * coordinates and then provide a location for the handles based on it.
	 * 
	 * @param handle
	 *            The handle whose position will be updated according to this
	 *            component.
	 */
	protected void updateHandlePosition(Handle handle)
	{
		if (handle == null)
			return;

		Coordinate coordinate = handle.getCoordinate();

		if (coordinate == null)
			return;

		double centerX = getCenterX() - getX();
		double centerY = getCenterY() - getY();

		switch (coordinate)
		{
			case CENTER:
				handle.setLocation(centerX - handle.getWidth() / 2, centerY
						- handle.getHeight() / 2);
				break;
			case NORTH_WEST:
				handle.setLocation(0, 0);
				break;
			case NORTH:
				handle.setLocation(centerX - handle.getWidth() / 2, 0);
				break;
			case NORTH_EAST:
				handle.setLocation(getWidth() - handle.getWidth(), 0);
				break;
			case EAST:
				handle.setLocation(getWidth() - handle.getWidth(), centerY
						- handle.getHeight() / 2);
				break;
			case SOUTH_EAST:
				handle.setLocation(getWidth() - handle.getWidth(), getHeight()
						- handle.getHeight());
				break;
			case SOUTH:
				handle.setLocation(centerX - handle.getWidth() / 2, getHeight()
						- handle.getHeight());
				break;
			case SOUTH_WEST:
				handle.setLocation(0, getHeight() - handle.getHeight());
				break;
			case WEST:
				handle.setLocation(0, centerY - handle.getHeight() / 2);
				break;
		}
	}
}