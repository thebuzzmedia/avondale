package com.kallasoft.avondale.component.event;

import java.util.EventListener;

public interface RootComponentListener extends EventListener
{
	public void rootComponentAdded(RootComponentEvent evt);

	public void rootComponentRemoved(RootComponentEvent evt);
}