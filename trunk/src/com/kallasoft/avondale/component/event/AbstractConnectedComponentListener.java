package com.kallasoft.avondale.component.event;

public abstract class AbstractConnectedComponentListener implements
		ConnectedComponentListener
{
	public void inboundConnectionAdded(ConnectedComponentEvent evt)
	{
		/* no-op impl */
	}

	public void inboundConnectionRemoved(ConnectedComponentEvent evt)
	{
		/* no-op impl */
	}
	
	public void allInboundConnectionsRemoved(ConnectedComponentEvent evt)
	{
		/* no-op impl */
	}

	public void outboundConnectionAdded(ConnectedComponentEvent evt)
	{
		/* no-op impl */
	}

	public void outboundConnectionRemoved(ConnectedComponentEvent evt)
	{
		/* no-op impl */
	}
	
	public void allOutboundConnectionsRemoved(ConnectedComponentEvent evt)
	{
		/* no-op impl */
	}
}