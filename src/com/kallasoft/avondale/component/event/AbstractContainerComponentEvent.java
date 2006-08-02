package com.kallasoft.avondale.component.event;

import java.util.EventObject;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;

public abstract class AbstractContainerComponentEvent extends EventObject
		implements ContainerComponentEvent
{
	private EventType eventType;
	private Component component;

	public AbstractContainerComponentEvent(ContainerComponent source, EventType eventType,
			Component component)
	{
		super(source);

		if (eventType == null)
			throw new IllegalArgumentException("eventType cannot be null");
		
		if(component == null)
			throw new IllegalArgumentException("component cannot be null");

		this.eventType = eventType;
		this.component = component;
	}
	
	@Override
	public ContainerComponent getSource()
	{
		return (ContainerComponent)super.getSource();
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public Component getComponent()
	{
		return component;
	}
}