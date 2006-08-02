package com.kallasoft.avondale.component.handle;

import com.kallasoft.avondale.component.AdjustableComponent;
import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.component.DefaultResizableComponent;
import com.kallasoft.avondale.component.model.ComponentModel;

public abstract class AbstractHandle extends DefaultResizableComponent
		implements Handle
{
	private Coordinate coordinate;

	public AbstractHandle(ComponentModel componentModel, Coordinate coordinate)
	{
		super(componentModel);
		this.coordinate = coordinate;
	}

	/**
	 * This method is overridden in order to check that the parentComponent is
	 * of type <code>AdjustableComponent</code>. <code>Handle</code> instances
	 * can not be added to any super class of <code>AdjustableComponent</code>.
	 * 
	 * @param parentComponent The <code>ContainerComponent</code> that contains
	 * this <code>Handle</code> instance.
	 */
	@Override
	public void setParentComponent(ContainerComponent parentComponent)
	{
		if (!(parentComponent instanceof AdjustableComponent))
			throw new IllegalArgumentException(
					"parentComponent must be of type AdjustableComponent");

		super.setParentComponent(parentComponent);
	}

	public Coordinate getCoordinate()
	{
		return coordinate;
	}
}