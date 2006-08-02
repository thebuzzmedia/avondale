package com.kallasoft.avondale.component.model;

import com.kallasoft.ext.bean.AbstractPropertyChangeSupport;

public abstract class AbstractComponentModel extends
		AbstractPropertyChangeSupport implements ComponentModel
{
	private Object id;

	public AbstractComponentModel(Object id)
	{
		this.id = id;
	}

	public Object getID()
	{
		return id;
	}

	public void setID(Object id)
	{
		Object oldID = getID();

		if (oldID == id)
			return;

		this.id = id;
		firePropertyChangeEvent(this, ID_PROPERTY_NAME, oldID, getID());
	}
}