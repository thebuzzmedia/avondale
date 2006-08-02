package com.kallasoft.avondale.component.event;

import java.util.EventObject;

import com.kallasoft.avondale.component.ConnectedComponent;
import com.kallasoft.avondale.component.connection.Connection;

public abstract class AbstractConnectedComponentEvent extends EventObject
		implements ConnectedComponentEvent
{
	private EventType eventType;
	private Connection connection;

	public AbstractConnectedComponentEvent(ConnectedComponent source, EventType eventType,
			Connection connection)
	{
		super(source);

		if (eventType == null)
			throw new IllegalArgumentException("eventType cannot be null");

		if (connection == null)
			throw new IllegalArgumentException("connection cannot be null");
		
		this.eventType = eventType;
		this.connection = connection;
	}
	
	@Override
	public ConnectedComponent getSource()
	{
		return (ConnectedComponent)super.getSource();
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public Connection getConnection()
	{
		return connection;
	}
}