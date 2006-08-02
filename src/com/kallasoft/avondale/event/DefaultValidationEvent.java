package com.kallasoft.avondale.event;

public class DefaultValidationEvent extends
		AbstractValidationEvent
{
	private static final long serialVersionUID = -7986259787340263094L;

	public DefaultValidationEvent(Object source, EventType eventType)
	{
		super(source, eventType);
	}
}