package com.kallasoft.avondale.component.event;

import com.kallasoft.avondale.component.ConnectedComponent;
import com.kallasoft.avondale.component.connection.Connection;

public interface ConnectedComponentEvent
{
	public static enum EventType
	{
		INBOUND_CONNECTION_ADDED, INBOUND_CONNECTION_REMOVED, ALL_INBOUND_CONNECTIONS_REMOVED, OUTBOUND_CONNECTION_ADDED, OUTBOUND_CONNECTION_REMOVED, ALL_OUTBOUND_CONNECTIONS_REMOVED
	}

	public EventType getEventType();
	
	public ConnectedComponent getSource();

	public Connection getConnection();
}