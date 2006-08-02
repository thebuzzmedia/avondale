package com.kallasoft.avondale.component.event;

import java.util.EventListener;

public interface ConnectedComponentListener extends EventListener
{
	public void inboundConnectionAdded(ConnectedComponentEvent evt);

	public void inboundConnectionRemoved(ConnectedComponentEvent evt);
	
	public void allInboundConnectionsRemoved(ConnectedComponentEvent evt);

	public void outboundConnectionAdded(ConnectedComponentEvent evt);

	public void outboundConnectionRemoved(ConnectedComponentEvent evt);
	
	public void allOutboundConnectionsRemoved(ConnectedComponentEvent evt);
}