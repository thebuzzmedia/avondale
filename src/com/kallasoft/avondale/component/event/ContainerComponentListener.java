package com.kallasoft.avondale.component.event;

import java.util.EventListener;

public interface ContainerComponentListener extends EventListener
{
	public void componentsAdded(ContainerComponentEvent evt);

	public void componentsRemoved(ContainerComponentEvent evt);
}