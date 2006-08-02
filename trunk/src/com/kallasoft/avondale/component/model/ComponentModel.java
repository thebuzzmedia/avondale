package com.kallasoft.avondale.component.model;

import java.io.Serializable;

import com.kallasoft.ext.bean.PropertyChangeSupport;

public interface ComponentModel extends PropertyChangeSupport, Serializable
{
	public static final String ID_PROPERTY_NAME = "id";

	public Object getID();

	public void setID(Object id);
}