package com.kallasoft.avondale.component;

import java.util.ArrayList;
import java.util.List;

import com.kallasoft.avondale.component.connection.Connection;
import com.kallasoft.avondale.component.event.ConnectedComponentEvent;
import com.kallasoft.avondale.component.event.ConnectedComponentListener;
import com.kallasoft.avondale.component.event.DefaultConnectedComponentEvent;
import com.kallasoft.avondale.component.model.ComponentModel;

public abstract class AbstractConnectedComponent extends
		DefaultResizableComponent implements ConnectedComponent
{
	private boolean connectedComponentNotificationEnabled = true;
	private List<Connection> inboundConnectionList;
	private List<Connection> outboundConnectionList;
	private transient List<ConnectedComponentListener> connectedComponentListenerList;

	public AbstractConnectedComponent(ComponentModel componentModel)
	{
		super(componentModel);
		
		inboundConnectionList = new ArrayList<Connection>(0);
		outboundConnectionList = new ArrayList<Connection>(0);
		connectedComponentListenerList = new ArrayList<ConnectedComponentListener>(
				0);
	}

	public boolean containsInboundConnection(Connection inboundConnection)
	{
		return inboundConnectionList.contains(inboundConnection);
	}

	public void addInboundConnection(Connection inboundConnection)
	{
		addInboundConnection(getInboundConnectionCount(), inboundConnection);
	}

	public void addInboundConnection(int index, Connection inboundConnection)
	{
		if (inboundConnection == null)
			throw new IllegalArgumentException(
					"inboundConnection cannot be null");

		if (!containsInboundConnection(inboundConnection))
		{
			inboundConnectionList.add(index, inboundConnection);

			if (inboundConnection.getDestinationComponent() != this)
				inboundConnection.setDestinationComponent(this);

			fireConnectedComponentEvent(new DefaultConnectedComponentEvent(
					this,
					ConnectedComponentEvent.EventType.INBOUND_CONNECTION_ADDED,
					inboundConnection));
		}

		/* Repaint the component */
		repaint();
	}

	public int getInboundConnectionCount()
	{
		return inboundConnectionList.size();
	}

	public int getIndexOfInboundConnection(Connection inboundConnection)
	{
		return inboundConnectionList.indexOf(inboundConnection);
	}

	public Connection getInboundConnection(int index)
	{
		return inboundConnectionList.get(index);
	}

	public Connection[] getInboundConnections()
	{
		return inboundConnectionList
				.toArray(new Connection[getInboundConnectionCount()]);
	}

	public void removeInboundConnection(int index)
	{
		Connection connection = inboundConnectionList.remove(index);

		if (connection != null)
		{
			/* Only clear the destinationComponent if it's this component */
			if (connection.getDestinationComponent() == this)
				connection.setDestinationComponent(null);

			fireConnectedComponentEvent(new DefaultConnectedComponentEvent(
					this,
					ConnectedComponentEvent.EventType.INBOUND_CONNECTION_REMOVED,
					connection));
		}

		/* Repaint the component */
		repaint();
	}

	public void removeInboundConnection(Connection inboundConnection)
	{
		if (inboundConnection == null)
			return;

		int index = getIndexOfInboundConnection(inboundConnection);
		
		if(index > -1)
			removeInboundConnection(index);
	}

	public boolean containsOutboundConnection(Connection outboundConnection)
	{
		return outboundConnectionList.contains(outboundConnection);
	}

	public void addOutboundConnection(Connection outboundConnection)
	{
		addOutboundConnection(getOutboundConnectionCount(), outboundConnection);
	}

	public void addOutboundConnection(int index, Connection outboundConnection)
	{
		if (outboundConnection == null)
			throw new IllegalArgumentException(
					"outboundConnection cannot be null");

		if (!containsOutboundConnection(outboundConnection))
		{
			outboundConnectionList.add(index, outboundConnection);

			if (outboundConnection.getSourceComponent() != this)
				outboundConnection.setSourceComponent(this);

			fireConnectedComponentEvent(new DefaultConnectedComponentEvent(
					this,
					ConnectedComponentEvent.EventType.OUTBOUND_CONNECTION_ADDED,
					outboundConnection));
		}

		/* Repaint the component */
		repaint();
	}

	public int getOutboundConnectionCount()
	{
		return outboundConnectionList.size();
	}

	public int getIndexOfOutboundConnection(Connection outboundConnection)
	{
		return outboundConnectionList.indexOf(outboundConnection);
	}

	public Connection getOutboundConnection(int index)
	{
		return outboundConnectionList.get(index);
	}

	public Connection[] getOutboundConnections()
	{
		return outboundConnectionList
				.toArray(new Connection[getOutboundConnectionCount()]);
	}

	public void removeOutboundConnection(int index)
	{
		Connection connection = outboundConnectionList.remove(index);

		if (connection != null)
		{
			/* Only clear the sourceComponent if it's this component */
			if (connection.getSourceComponent() == this)
				connection.setSourceComponent(null);

			fireConnectedComponentEvent(new DefaultConnectedComponentEvent(
					this,
					ConnectedComponentEvent.EventType.OUTBOUND_CONNECTION_REMOVED,
					connection));
		}

		/* Repaint the component */
		repaint();
	}

	public void removeOutboundConnection(Connection outboundConnection)
	{
		if (outboundConnection == null)
			return;

		int index = getIndexOfOutboundConnection(outboundConnection);
		
		if(index > -1)
			removeOutboundConnection(index);
	}

	public boolean isConnectedComponentNotificationEnabled()
	{
		return connectedComponentNotificationEnabled;
	}

	public void setConnectedComponentNotificationEnabled(
			boolean connectedComponentNotificationEnabled)
	{
		boolean oldConnectedComponentNotificationEnabled = isConnectedComponentNotificationEnabled();
		
		if (oldConnectedComponentNotificationEnabled == connectedComponentNotificationEnabled)
			return;

		this.connectedComponentNotificationEnabled = connectedComponentNotificationEnabled;
		firePropertyChangeEvent(this,
				CONNECTED_COMPONENT_NOTIFICATION_ENABLED_PROPERTY_NAME,
				oldConnectedComponentNotificationEnabled, isConnectedComponentNotificationEnabled());
	}

	public boolean containsConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener)
	{
		return connectedComponentListenerList
				.contains(connectedComponentListener);
	}

	public void addConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener)
	{
		addConnectedComponentListener(getConnectedComponentListenerCount(),
				connectedComponentListener);
	}

	public void addConnectedComponentListener(int index,
			ConnectedComponentListener connectedComponentListener)
	{
		if (connectedComponentListener == null)
			return;

		if (!containsConnectedComponentListener(connectedComponentListener))
			connectedComponentListenerList.add(index,
					connectedComponentListener);
	}

	public int getConnectedComponentListenerCount()
	{
		return connectedComponentListenerList.size();
	}

	public int getIndexOfConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener)
	{
		return connectedComponentListenerList
				.indexOf(connectedComponentListener);
	}

	public ConnectedComponentListener getConnectedComponentListener(int index)
	{
		return connectedComponentListenerList.get(index);
	}

	public ConnectedComponentListener[] getConnectedComponentListeners()
	{
		return connectedComponentListenerList
				.toArray(new ConnectedComponentListener[getConnectedComponentListenerCount()]);
	}

	public void removeConnectedComponentListener(int index)
	{
		connectedComponentListenerList.remove(index);
	}

	public void removeConnectedComponentListener(
			ConnectedComponentListener connectedComponentListener)
	{
		if (connectedComponentListener == null)
			return;

		int index = getIndexOfConnectedComponentListener(connectedComponentListener);
		
		if(index > -1)
			removeConnectedComponentListener(index);
	}

	public void removeConnectedComponentListeners()
	{
		connectedComponentListenerList.clear();
	}

	protected void fireConnectedComponentEvent(ConnectedComponentEvent evt)
	{
		if (evt == null || !isConnectedComponentNotificationEnabled())
			return;

		for (int i = 0, size = getConnectedComponentListenerCount(); i < size; i++)
		{
			switch (evt.getEventType())
			{
				case INBOUND_CONNECTION_ADDED:
					getConnectedComponentListener(i)
							.inboundConnectionAdded(evt);
					break;

				case INBOUND_CONNECTION_REMOVED:
					getConnectedComponentListener(i).inboundConnectionRemoved(
							evt);
					break;

				case ALL_INBOUND_CONNECTIONS_REMOVED:
					getConnectedComponentListener(i)
							.allInboundConnectionsRemoved(evt);
					break;

				case OUTBOUND_CONNECTION_ADDED:
					getConnectedComponentListener(i).outboundConnectionAdded(
							evt);
					break;

				case OUTBOUND_CONNECTION_REMOVED:
					getConnectedComponentListener(i).outboundConnectionRemoved(
							evt);
					break;

				case ALL_OUTBOUND_CONNECTIONS_REMOVED:
					getConnectedComponentListener(i)
							.allOutboundConnectionsRemoved(evt);
					break;
			}
		}
	}
}