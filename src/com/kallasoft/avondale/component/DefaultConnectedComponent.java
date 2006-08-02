package com.kallasoft.avondale.component;

import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.DefaultComponentPainter;

public class DefaultConnectedComponent extends AbstractConnectedComponent
{
	public DefaultConnectedComponent()
	{
		this(null);
	}
	
	public DefaultConnectedComponent(ComponentModel componentModel)
	{
		super(componentModel);
		setComponentPainter(DefaultComponentPainter.getInstance());
	}

	@Override
	public void validate()
	{
		/* If this component is already valid don't do anything */
		if (isValid())
			return;

		/*
		 * We need to validate all the connections connected to this component
		 * because connection's state are directly tied to the state of the
		 * components they connect to (e.g. position).
		 */
		for (int i = 0, size = getInboundConnectionCount(); i < size; i++)
			getInboundConnection(i).validate();

		for (int i = 0, size = getOutboundConnectionCount(); i < size; i++)
			getOutboundConnection(i).validate();

		super.validate();
	}

	@Override
	public void invalidate()
	{
		/* If this component is already invalid don't do anything */
		if (!isValid())
			return;

		/*
		 * We need to invalidate all the connections connected to this component
		 * because connection's state are directly tied to the state of the
		 * components they connect to (e.g. position).
		 */
		for (int i = 0, size = getInboundConnectionCount(); i < size; i++)
			getInboundConnection(i).invalidate();

		for (int i = 0, size = getOutboundConnectionCount(); i < size; i++)
			getOutboundConnection(i).invalidate();

		super.invalidate();
	}
}