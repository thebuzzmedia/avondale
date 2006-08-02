package com.kallasoft.avondale.component;

import com.kallasoft.avondale.component.handle.Handle;

public interface AdjustableComponent extends DraggableComponent
{
	public static final String SHOW_HANDLES_PROPERTY_NAME = "showHandles";
	public static final String ACTIVE_HANDLE_PROPERTY_NAME = "activeHandle";

	public boolean isShowHandles();

	public void setShowHandles(boolean showHandles);

	public Handle getActiveHandle();

	public void setActiveHandle(double xOffset, double yOffset,
			Handle activeHandle);
}