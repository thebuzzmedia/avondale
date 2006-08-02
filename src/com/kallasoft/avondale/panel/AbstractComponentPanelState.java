package com.kallasoft.avondale.panel;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.ext.bean.AbstractPropertyChangeSupport;

/**
 * Abstract class used to provide a basic implementation of the 
 * <code>ComponentPanelState</code> interface.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractComponentPanelState extends
		AbstractPropertyChangeSupport implements ComponentPanelState
{
	private Component focusedComponent;
	private Component mousedOverComponent;
	private Component mousePressedComponent;

	public Component getFocusedComponent()
	{
		return focusedComponent;
	}

	public void setFocusedComponent(Component focusedComponent)
	{
		Component oldFocusedComponent = getFocusedComponent();

		if (oldFocusedComponent == focusedComponent)
			return;

		this.focusedComponent = focusedComponent;

		/*
		 * In order for a focusedComponent to get focused-based input like
		 * keyEvents, the parentComponent's ComponentPanel must be focused to 
		 * receive those events.
		 */
		if (this.focusedComponent != null)
		{
			ComponentPanel componentPanel = this.focusedComponent
					.getComponentPanel();

			/* Try and have the componentPanel associated with this focusedComponent
			 * aquire the focus if it doesn't already have it */
			if (componentPanel != null && !componentPanel.hasFocus())
				componentPanel.requestFocus();
		}

		firePropertyChangeEvent(this, FOCUSED_COMPONENT_PROPERTY_NAME,
				oldFocusedComponent, getFocusedComponent());
	}

	public Component getMousedOverComponent()
	{
		return mousedOverComponent;
	}

	public void setMousedOverComponent(Component mousedOverComponent)
	{
		Component oldMousedOverComponent = getMousedOverComponent();

		if (oldMousedOverComponent == mousedOverComponent)
			return;

		this.mousedOverComponent = mousedOverComponent;
		firePropertyChangeEvent(this, MOUSED_OVER_COMPONENT_PROPERTY_NAME,
				oldMousedOverComponent, getMousedOverComponent());
	}

	public Component getMousePressedComponent()
	{
		return mousePressedComponent;
	}

	public void setMousePressedComponent(Component mousePressedComponent)
	{
		Object oldMousePressedComponent = getMousePressedComponent();

		if (oldMousePressedComponent == mousePressedComponent)
			return;

		this.mousePressedComponent = mousePressedComponent;
		firePropertyChangeEvent(this, MOUSE_PRESSED_COMPONENT_PROPERTY_NAME,
				oldMousePressedComponent, getMousePressedComponent());
	}
}