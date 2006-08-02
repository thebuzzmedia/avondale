package com.kallasoft.avondale.panel.tool;

import javax.swing.Action;

import com.kallasoft.avondale.panel.ComponentPanel;
import com.kallasoft.ext.bean.PropertyChangeSupport;

public interface ComponentPanelTool extends Action, PropertyChangeSupport
{
	public static final String ENABLED_PROPERTY_NAME = "enabled";
	public static final String ACTIVE_PROPERTY_NAME = "active";

	public static final String ID_KEY = "id";
	public static final String CURSOR_KEY = "cursor";

	public boolean isActive();

	public void activate(ComponentPanel componentPanel);

	public void deactivate(ComponentPanel componentPanel);
}