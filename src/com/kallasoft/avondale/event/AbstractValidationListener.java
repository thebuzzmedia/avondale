package com.kallasoft.avondale.event;

public abstract class AbstractValidationListener implements
		ValidationListener
{
	public void validated(ValidationEvent evt)
	{
		/* no-op impl */
	}

	public void invalidated(ValidationEvent evt)
	{
		/* no-op impl */
	}
}