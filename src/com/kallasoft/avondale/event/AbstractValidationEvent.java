package com.kallasoft.avondale.event;

import java.util.EventObject;

public abstract class AbstractValidationEvent extends EventObject
		implements ValidationEvent
{
	private EventType eventType;

	public AbstractValidationEvent(Object source, EventType eventType)
	{
		super(source);

		if (eventType == null)
			throw new IllegalArgumentException("eventType cannot be null");

		this.eventType = eventType;
	}

	public EventType getEventType()
	{
		return eventType;
	}
}