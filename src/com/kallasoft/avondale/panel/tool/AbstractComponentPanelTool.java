package com.kallasoft.avondale.panel.tool;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import com.kallasoft.avondale.panel.ComponentPanel;
import com.kallasoft.ext.bean.AbstractPropertyChangeSupport;

public abstract class AbstractComponentPanelTool extends
		AbstractPropertyChangeSupport implements ComponentPanelTool
{
	private boolean enabled = true;
	private boolean active = false;
	private Map<String, Object> valueMap;

	public AbstractComponentPanelTool(Object id, String name, Icon icon,
			Cursor cursor, String shortDescription, String longDescription)
	{
		if (id == null)
			throw new IllegalArgumentException("id cannot be null");

		valueMap = new HashMap<String, Object>();
		
		putValue(ID_KEY, id);
		putValue(NAME, name);
		putValue(SMALL_ICON, icon);
		putValue(CURSOR_KEY, cursor);
		putValue(SHORT_DESCRIPTION, shortDescription);
		putValue(LONG_DESCRIPTION, longDescription);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		boolean oldEnabled = isEnabled();
		
		if (oldEnabled == enabled)
			return;

		this.enabled = enabled;
		firePropertyChangeEvent(this, ENABLED_PROPERTY_NAME, oldEnabled, isEnabled());
	}

	public Object getValue(String key)
	{
		return valueMap.get(key);
	}

	public void putValue(String key, Object value)
	{
		valueMap.put(key, value);
	}

	public boolean isActive()
	{
		return active;
	}

	public void activate(ComponentPanel componentPanel)
	{
		if (componentPanel == null)
			throw new IllegalArgumentException("componentPanel cannot be null");

		active = true;
		firePropertyChangeEvent(this, ACTIVE_PROPERTY_NAME, Boolean.FALSE,
				Boolean.TRUE);
	}

	public void deactivate(ComponentPanel componentPanel)
	{
		if (componentPanel == null)
			throw new IllegalArgumentException("componentPanel cannot be null");

		/*
		 * If this is not the currently active tool on this componentPanel,
		 * then throw an exception.
		 */
		if (componentPanel.getActiveComponentPanelTool() != this)
			throw new IllegalStateException(
					"this ComponentPanelTool cannot be deactivated, it is not the current activeComponentPanelTool for the given componentPanel");

		active = false;
		firePropertyChangeEvent(this, ACTIVE_PROPERTY_NAME, Boolean.TRUE,
				Boolean.FALSE);
	}
}