package com.kallasoft.avondale.component.event;

import java.util.EventObject;

import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.panel.ComponentPanel;

public abstract class AbstractRootComponentEvent extends EventObject implements
		RootComponentEvent
{
	private EventType eventType;
	private ContainerComponent rootComponent;

	public AbstractRootComponentEvent(ComponentPanel source,
			EventType eventType, ContainerComponent rootComponent)
	{
		super(source);

		if (eventType == null)
			throw new IllegalArgumentException("eventType cannot be null");

		if (rootComponent == null)
			throw new IllegalArgumentException("rootComponent cannot be null");

		this.eventType = eventType;
		this.rootComponent = rootComponent;
	}

	public EventType getEventType()
	{
		return eventType;
	}

	@Override
	public ComponentPanel getSource()
	{
		return (ComponentPanel) super.getSource();
	}

	public ContainerComponent getRootComponent()
	{
		return rootComponent;
	}
}