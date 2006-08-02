package com.kallasoft.avondale.component.event;

import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.panel.ComponentPanel;

public interface RootComponentEvent
{
	public static enum EventType
	{
		ROOT_COMPONENT_ADDED, ROOT_COMPONENT_REMOVED
	}
	
	public EventType getEventType();
	
	public ComponentPanel getSource();

	public ContainerComponent getRootComponent();
}