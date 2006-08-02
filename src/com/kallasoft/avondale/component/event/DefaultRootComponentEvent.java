package com.kallasoft.avondale.component.event;

import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.panel.ComponentPanel;

public class DefaultRootComponentEvent extends AbstractRootComponentEvent
{
	private static final long serialVersionUID = 6930944506003774913L;

	public DefaultRootComponentEvent(ComponentPanel source,
			EventType eventType, ContainerComponent rootComponent)
	{
		super(source, eventType, rootComponent);
	}
}