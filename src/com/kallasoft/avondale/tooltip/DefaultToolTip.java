package com.kallasoft.avondale.tooltip;

import javax.swing.Icon;

import com.kallasoft.avondale.component.Component;

public class DefaultToolTip extends AbstractToolTip
{
	private static final long serialVersionUID = -4653158542953079980L;

	public DefaultToolTip()
	{
		this(null);
	}

	public DefaultToolTip(String text)
	{
		this(text, null);
	}

	public DefaultToolTip(String text, Icon icon)
	{
		this(null, text, icon);
	}
	
	public DefaultToolTip(Component component, String text, Icon icon)
	{
		super(component, text, icon);
	}
}