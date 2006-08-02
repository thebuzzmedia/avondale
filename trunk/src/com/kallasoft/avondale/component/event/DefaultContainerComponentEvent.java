package com.kallasoft.avondale.component.event;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;

public class DefaultContainerComponentEvent extends
		AbstractContainerComponentEvent
{
	private static final long serialVersionUID = 5424340999539398852L;

	public DefaultContainerComponentEvent(ContainerComponent source, EventType eventType,
			Component component)
	{
		super(source, eventType, component);
	}
}