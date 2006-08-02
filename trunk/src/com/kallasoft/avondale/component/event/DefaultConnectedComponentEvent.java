package com.kallasoft.avondale.component.event;

import com.kallasoft.avondale.component.ConnectedComponent;
import com.kallasoft.avondale.component.connection.Connection;

public class DefaultConnectedComponentEvent extends
		AbstractConnectedComponentEvent
{
	private static final long serialVersionUID = 4875355012991152660L;

	public DefaultConnectedComponentEvent(ConnectedComponent source, EventType eventType,
			Connection connection)
	{
		super(source, eventType, connection);
	}
}