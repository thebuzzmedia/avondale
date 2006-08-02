package com.kallasoft.avondale.panel.tool;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.Icon;

import com.kallasoft.avondale.panel.ComponentPanel;

public class DefaultComponentPanelTool extends AbstractComponentPanelTool
{
	private Cursor oldCursor;
	
	public DefaultComponentPanelTool(Object id, String name, Icon icon,
			Cursor cursor, String shortDescription, String longDescription)
	{
		super(id, name, icon, cursor, shortDescription, longDescription);
	}
	
	@Override
	public void activate(ComponentPanel componentPanel)
	{
		if (componentPanel == null)
			throw new IllegalArgumentException("componentPanel cannot be null");

		Cursor cursor = (Cursor) getValue(CURSOR_KEY);

		/* Update the componentPanel cursor if it's not null */
		if (cursor != null)
		{
			oldCursor = componentPanel.getCursor();
			componentPanel.setCursor(cursor);
		}

		/* Perform the actual activation implementation */
		super.activate(componentPanel);
	}

	@Override
	public void deactivate(ComponentPanel componentPanel)
	{
		if (componentPanel == null)
			throw new IllegalArgumentException("componentPanel cannot be null");

		/*
		 * This this is not the currently active tool on this componentPanel,
		 * then throw an exception.
		 */
		if (componentPanel.getActiveComponentPanelTool() != this)
			throw new IllegalStateException(
					"this ComponentPanelTool cannot be deactivated, it is not the current activeComponentPanelTool for the given componentPanel");

		/* Restore the old cursor */
		if (oldCursor != null)
			componentPanel.setCursor(oldCursor);

		oldCursor = null;

		/* Perform the actual deactivation implementation */
		super.deactivate(componentPanel);
	}

	public void actionPerformed(ActionEvent evt)
	{
		/* no-op impl */
	}
}