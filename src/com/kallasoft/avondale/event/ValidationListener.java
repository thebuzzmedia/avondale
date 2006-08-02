package com.kallasoft.avondale.event;

import java.util.EventListener;

public interface ValidationListener extends EventListener
{
	public void validated(ValidationEvent evt);

	public void invalidated(ValidationEvent evt);
}