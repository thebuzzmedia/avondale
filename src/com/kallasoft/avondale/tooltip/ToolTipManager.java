package com.kallasoft.avondale.tooltip;

public interface ToolTipManager
{
	public static final int DEFAULT_INITIAL_DISPLAY_DELAY = 1000;
	public static final int DEFAULT_DISMISS_DISPLAY_DELAY = 5000;
	public static final int DEFAULT_RESHOW_DISPLAY_DELAY = 500;

	public int getInitialDisplayDelay();

	public void setInitialDisplayDelay(int initialDisplayDelay);

	public int getDismissDisplayDelay();

	public void setDismissDisplayDelay(int dismissDisplayDelay);

	public int getReshowDisplayDelay();

	public void setReshowDisplayDelay(int reshowDisplayDelay);
	
	public ToolTipRenderer getToolTipRenderer();
	
	public void setToolTipRenderer(ToolTipRenderer toolTipRenderer);

	public boolean isShowingToolTip();

	public void showToolTip(ToolTip toolTip);

	public void hideToolTip(ToolTip toolTip);
}