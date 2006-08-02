package com.kallasoft.avondale.tooltip;

/**
 * Interface used to abstract out the details of the rendering of a tooltip
 * using either a lightweight or heavyweight renderer. Implementations of this
 * interface will handle all the logic necessary to display and hide the tooltip
 * regardless of the method. This allows the manager (or other custom code) to
 * call directly to show and hide methods without worrying about how it's done.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public interface ToolTipRenderer
{
	public void showToolTip(ToolTip toolTip);
	
	public void hideToolTip(ToolTip toolTip);
}