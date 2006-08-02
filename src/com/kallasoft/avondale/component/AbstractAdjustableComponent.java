package com.kallasoft.avondale.component;

import com.kallasoft.avondale.component.handle.Handle;
import com.kallasoft.avondale.component.model.ComponentModel;

public abstract class AbstractAdjustableComponent extends
		DefaultDraggableComponent implements AdjustableComponent
{
	private boolean showHandles = true;
	private Handle activeHandle;

	public AbstractAdjustableComponent(ComponentModel componentModel,
			ContainerComponent parentComponent)
	{
		super(componentModel, parentComponent);
	}

	public boolean isShowHandles()
	{
		return showHandles;
	}

	public void setShowHandles(boolean showHandles)
	{
		boolean oldShowHandles = isShowHandles();

		if (oldShowHandles == showHandles)
			return;

		this.showHandles = showHandles;
		firePropertyChangeEvent(this, SHOW_HANDLES_PROPERTY_NAME,
				oldShowHandles, isShowHandles());

		/* Repaint the component */
		repaint();
	}

	public Handle getActiveHandle()
	{
		return activeHandle;
	}

	public void setActiveHandle(double xOffset, double yOffset,
			Handle activeHandle)
	{
		Handle oldActiveHandle = getActiveHandle();

		if (oldActiveHandle == activeHandle)
			return;

		this.activeHandle = activeHandle;
		firePropertyChangeEvent(this, ACTIVE_HANDLE_PROPERTY_NAME,
				oldActiveHandle, getActiveHandle());
	}
}