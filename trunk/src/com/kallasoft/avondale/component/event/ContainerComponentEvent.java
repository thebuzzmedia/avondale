package com.kallasoft.avondale.component.event;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;

public interface ContainerComponentEvent
{
	public static enum EventType
	{
		COMPONENT_ADDED, COMPONENT_REMOVED
	}

	public EventType getEventType();
	
	public ContainerComponent getSource();
	
	public Component getComponent();
}