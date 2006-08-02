package com.kallasoft.avondale.tooltip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/* TODO: Too much specific impl in this class that should be moved to Default */
public abstract class AbstractToolTipManager implements ToolTipManager
{
	private boolean showing = false;
	private ToolTipRenderer toolTipRenderer;
	
	protected boolean withinReshowTimeLimit = false;
	protected Timer initialDisplayTimer;
	protected Timer dismissDisplayTimer;
	protected Timer reshowDisplayTimer;
	protected ToolTip currentToolTip;
	
	public AbstractToolTipManager(int initialDisplayDelay,
			int dismissDisplayDelay, int reshowDisplayDelay)
	{
		initialDisplayTimer = new Timer(initialDisplayDelay,
				new InitialDisplayHandler());
		dismissDisplayTimer = new Timer(dismissDisplayDelay,
				new DismissDisplayHandler());
		reshowDisplayTimer = new Timer(reshowDisplayDelay,
				new ReshowDisplayHandler());
	}

	public int getInitialDisplayDelay()
	{
		return initialDisplayTimer.getDelay();
	}

	public void setInitialDisplayDelay(int initialDisplayDelay)
	{
		initialDisplayTimer.setDelay(initialDisplayDelay);
	}

	public int getDismissDisplayDelay()
	{
		return dismissDisplayTimer.getDelay();
	}

	public void setDismissDisplayDelay(int dismissDisplayDelay)
	{
		dismissDisplayTimer.setDelay(dismissDisplayDelay);
	}

	public int getReshowDisplayDelay()
	{
		return reshowDisplayTimer.getDelay();
	}

	public void setReshowDisplayDelay(int reshowDisplayDelay)
	{
		reshowDisplayTimer.setDelay(reshowDisplayDelay);
	}
	
	public ToolTipRenderer getToolTipRenderer()
	{
		return toolTipRenderer;
	}
	
	public void setToolTipRenderer(ToolTipRenderer toolTipRenderer)
	{
		this.toolTipRenderer = toolTipRenderer;
	}

	public boolean isShowingToolTip()
	{
		return showing;
	}
	
	public void showToolTip(ToolTip toolTip)
	{
		currentToolTip = toolTip;
		showing = true;
	}

	public void hideToolTip(ToolTip toolTip)
	{
		showing = false;
		currentToolTip = null;
	}

	protected class InitialDisplayHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			/*
			 * Once the initial display timer goes off, stop it, and show the
			 * tooltip
			 */
			initialDisplayTimer.stop();
			showToolTip(currentToolTip);

			/* Start the dismiss timer now that the tooltip is showing */
			dismissDisplayTimer.start();
		}
	}

	protected class DismissDisplayHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			/* Stop the dismiss timer once it fires and hide the tooltip */
			dismissDisplayTimer.stop();
			hideToolTip(currentToolTip);
		}
	}

	protected class ReshowDisplayHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			/*
			 * First stop the timer so it only fires once until the next hide
			 * occurs and unset the withinReshowTimeLimit state because tooltip
			 * show requests now comming in will no longer be within the reshow
			 * time bounds.
			 */
			reshowDisplayTimer.stop();
			withinReshowTimeLimit = false;
		}
	}
}