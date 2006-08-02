package com.kallasoft.avondale.event;

public interface ValidationEvent
{
	public static enum EventType
	{
		VALIDATED, INVALIDATED
	}

	public Object getSource();
	
	public EventType getEventType();
}