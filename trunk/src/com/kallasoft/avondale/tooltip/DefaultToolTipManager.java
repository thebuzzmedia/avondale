package com.kallasoft.avondale.tooltip;


/**
 * Class used to provide a default implementation of the
 * <code>ToolTipManager</code> interface. This implementation is written to
 * delegate all the logic of how the <code>ToolTip</code> is displayed to an
 * instance of <code>ToolTipRenderer</code>. This class handles the time
 * calculations of if the tip should be displayed or not, but to actually do the
 * displaying or hiding it delegates to the associated tip renderer.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public class DefaultToolTipManager extends AbstractToolTipManager
{
	public DefaultToolTipManager()
	{
		this(DEFAULT_INITIAL_DISPLAY_DELAY, DEFAULT_DISMISS_DISPLAY_DELAY,
				DEFAULT_RESHOW_DISPLAY_DELAY);
	}

	public DefaultToolTipManager(int initialDisplayDelay,
			int dismissDisplayDelay, int reshowDisplayDelay)
	{
		super(initialDisplayDelay, dismissDisplayDelay, reshowDisplayDelay);
		setToolTipRenderer(new DefaultToolTipRenderer());
	}

	@Override
	public void showToolTip(ToolTip toolTip)
	{
		super.showToolTip(toolTip);
		
		if (toolTip == null)
			return;

		/*
		 * If the request has come in within the time bounds of the
		 * reshowTimeLimit then reset the reshow timer and immediately show the
		 * tooltip, otherwise start the timer until it is shown.
		 */
		if (withinReshowTimeLimit)
		{
			reshowDisplayTimer.restart();
			
			ToolTipRenderer toolTipRenderer = getToolTipRenderer();
			
			if(toolTipRenderer != null)
				toolTipRenderer.showToolTip(toolTip);
		}
		else
			initialDisplayTimer.start();
	}

	@Override
	public void hideToolTip(ToolTip toolTip)
	{
		super.hideToolTip(toolTip);

		/* Hide the toolTop */
		ToolTipRenderer toolTipRenderer = getToolTipRenderer();
		
		if(toolTipRenderer != null)
			toolTipRenderer.hideToolTip(toolTip);

		/*
		 * When a tooltip is hidden, the initial and dismiss timers need to be
		 * stopped, they are only started when a new tooltip is requested to be
		 * shown.
		 */
		initialDisplayTimer.stop();
		dismissDisplayTimer.stop();

		/*
		 * We need to now set the withinReshowTimeLimit to true, incase another
		 * show request comes in before our reshow timer (now started) runs out.
		 */
		withinReshowTimeLimit = true;
		reshowDisplayTimer.start();
	}
}