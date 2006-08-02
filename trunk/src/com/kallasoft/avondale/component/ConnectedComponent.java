package com.kallasoft.avondale.component;

import com.kallasoft.avondale.component.connection.Connection;
import com.kallasoft.avondale.component.event.ConnectedComponentListener;

public interface ConnectedComponent extends ResizableComponent
{
	public static final String CONNECTED_COMPONENT_NOTIFICATION_ENABLED_PROPERTY_NAME = "connectedComponentNotificationEnabled";

	public boolean containsInboundConnection(Connection inboundConnection);

	public void addInboundConnection(Connection inboundConnection);

	public void addInboundConnection(int index, Connection inboundConnection);

	public int getInboundConnectionCount();

	public int getIndexOfInboundConnection(Connection inboundConnection);

	public Connection getInboundConnection(int index);

	public Connection[] getInboundConnections();

	public void removeInboundConnection(int index);

	public void removeInboundConnection(Connection inboundConnection);

	public boolean containsOutboundConnection(Connection outboundConnection);

	public void addOutboundConnection(Connection outboundConnection);

	public void addOutboundConnection(int index, Connection outboundConnection);

	public int getOutboundConnectionCount();

	public int getIndexOfOutboundConnection(Connection outboundConnection);

	public Connection getOutboundConnection(int index);

	public Connection[] getOutboundConnections();

	public void removeOutboundConnection(int index);

	public void removeOutboundConnection(Connection outboundConnection);

	public boolean isConnectedComponentNotificationEnabled();

	public void setConnectedComponentNotificationEnabled(
			boolean connectedComponentNotificationEnabled);

	public boolean containsConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener);

	public void addConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener);

	public void addConnectedComponentListener(
			int index, ConnectedComponentListener connectedComponentListener);

	public int getConnectedComponentListenerCount();

	public int getIndexOfConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener);

	public ConnectedComponentListener getConnectedComponentListener(int index);

	public ConnectedComponentListener[] getConnectedComponentListeners();

	public void removeConnectedComponentListener(int index);

	public void removeConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener);

	public void removeConnectedComponentListeners();
}