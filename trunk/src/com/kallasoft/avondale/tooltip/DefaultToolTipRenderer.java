package com.kallasoft.avondale.tooltip;

import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.kallasoft.avondale.component.Component;

/**
 * Class used to provide a default implementation of the
 * <code>ToolTipRenderer</code> interface. This class is implemented to use
 * either a lightweight (JPanel) renderer or a heavyweight (JWindow) renderer
 * for displaying tooltips. The algorithm to determine which renderer is used
 * checks to see if the tooltip is small enough to display within the bounds of
 * the containing window that holds the componentPanel that the toolTip's
 * component refers to (even if that means adjusting it slightly offset from the
 * mouse location). If the toolTip is too large and cannot be displayed inside
 * of the containing top level window, then the heavyweight renderer is used and
 * simply positions as a floating window in relation to the mouse where it
 * should be. Both renderers are lazily-instantiated so as not to use any
 * resources that are not necessary for some applications that won't make use of
 * toolTips. This class allows the implementation of the
 * <code>ToolTipManager</code> to be simpler and abstract out the details of
 * displaying the toolTip. It also allows implementors to easily create
 * alternative logic for displaying toolTips and plug that into the manager if
 * they so desire.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public class DefaultToolTipRenderer implements ToolTipRenderer
{
	private static final int OVERLAP_OFFSET_ADJUSTMENT = 4;

	private boolean useLightWeightRenderer = true;
	/*
	 * Instances of the lightweight and heavyweight renderers are
	 * lazy-initialized
	 */
	private ToolTipDisplayPanel lightWeightRenderer;
	private HeavyWeightRenderer heavyWeightRenderer;

	/**
	 * The implementation of this method is synchronized because the lightweight
	 * and heavyweight renderers are initialized lazily and we want to avoid a
	 * case where two separate threads cause instantiation twice. Additionally
	 * showing/hiding tooltips is not a performance-intensive task so
	 * synchronizing this method to avoid excess memory usage made sense.
	 * 
	 * @param toolTip
	 *            The toolTip that will be displayed.
	 */
	public synchronized void showToolTip(ToolTip toolTip)
	{
		/* If the toolTip is null, do nothing */
		if (toolTip == null)
			return;

		/*
		 * Create an instance of the lightweight renderer if it doesn't exist.
		 * We will use this instance no matter what, either by itself in
		 * lightweight mode or as the contentPane of the heavyweight renderer.
		 */
		if (lightWeightRenderer == null)
			lightWeightRenderer = new ToolTipDisplayPanel();

		/* Initialize the panel with the tooltip's properties */
		lightWeightRenderer.setToolTip(toolTip);

		/* Determine which renderer to use */
		Window window = null;
		Component component = toolTip.getComponent();
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

		/*
		 * Calculate based on the mouse location and the offsets for the tooltip
		 * where the tooltip is going to want to display itself.
		 */
		int displayX = (int) mouseLocation.getX() + toolTip.getXOffset();
		int displayY = (int) mouseLocation.getY() + toolTip.getYOffset();

		/*
		 * If the component reference is null or the component has no reference
		 * to it's componentPanel, then immediately use the heavyWeight
		 * renderer. Otherwise get a reference to the containing window of the
		 * componentPanel that is referenced by this component. We will need to
		 * see if we can use the lightweight renderer to display inside the
		 * GlassPane of the top level window.
		 */
		if (component != null && component.getComponentPanel() != null)
		{
			window = SwingUtilities.getWindowAncestor(component
					.getComponentPanel());

			/*
			 * Check if we have a window reference for the parent window for the
			 * componentPanel.
			 */
			if (window != null)
			{
				/*
				 * Check if the tooltip can be displayed given the size of the
				 * window at all. If it can, then further check if the offsets
				 * of the tooltip need to be adjusted in order to display within
				 * the bounds of the window.
				 */
				if (window.getWidth() < lightWeightRenderer.getWidth()
						|| window.getHeight() < lightWeightRenderer.getHeight())
					useLightWeightRenderer = false;
				else
				{
					/*
					 * Check to see if we can display the tooltip unchanged
					 * given the mouse's location
					 */
					int excessX = window.getWidth()
							- (displayX + lightWeightRenderer.getWidth());
					int excessY = window.getHeight()
							- (displayY + lightWeightRenderer.getHeight());

					/*
					 * If the tooltip was overlapping any edges of the window
					 * Adjust the x-coordinate by the amount that the tooltip
					 * hung over the window's edge.
					 */
					if (excessX < 0)
						displayX -= (Math.abs(excessX) - OVERLAP_OFFSET_ADJUSTMENT);

					/*
					 * Adjust the x-coordinate by the amount that the tooltip
					 * hung over the window's edge if necessary
					 */
					if (excessY < 0)
						displayY -= (Math.abs(excessY) - OVERLAP_OFFSET_ADJUSTMENT);
				}
			}
			else
				useLightWeightRenderer = false;
		}
		else
			useLightWeightRenderer = false;

		/*
		 * Make sure that window is an instance of JWindow (so we can get a
		 * reference to the rootPane and the glassPane). If it's not, fall back
		 * to use the heavyweight renderer.
		 */
		if (useLightWeightRenderer && !(window instanceof JWindow))
			useLightWeightRenderer = false;

		/*
		 * By now if useLightWeight is true, that means we have calculated the
		 * displayX and displayY coordinates necessary to display this tooltip
		 * inside of the bounds of the parent window to the component's
		 * componentPanel. If useLightWeight is false, then we need to use the
		 * heavy weight renderer.
		 */
		if (useLightWeightRenderer)
		{
			/*
			 * We now need to add the lightweight renderer to the GlassPane of
			 * the top level window.
			 */
			@SuppressWarnings(value = { "null" })
			JComponent glassPane = (JComponent) ((JWindow) window)
					.getRootPane().getGlassPane();
			glassPane.add(lightWeightRenderer);
		}
		else
		{
			/* See if we need to instantiate the heavyweight renderer */
			if (heavyWeightRenderer == null)
				heavyWeightRenderer = new HeavyWeightRenderer(
						lightWeightRenderer);

			/*
			 * Update the position of the heavyweight renderer, pack it so it's
			 * the right size based on the lightweight renderer that it contains
			 * then display it.
			 */
			heavyWeightRenderer.setLocation(displayX, displayY);
			heavyWeightRenderer.pack();
			heavyWeightRenderer.setVisible(true);
		}
	}

	/**
	 * This method is implemented to hide the toolTip that is currently being
	 * displayed even if the argument passed in is null. This method is immune
	 * to a <code>null</code> argument because of a situation like the
	 * following: (1) a component shows it's toolTip, (2) some code removes the
	 * toolTip from the component, (3) the component tries to hide it's toolTip.
	 * In this case, by step #3 the component will attempt to hide a
	 * <code>null</code> toolTip argument. If this method returned and did
	 * nothing that would leave either the lightweight or heavyweight renderers
	 * in a "hung" state on the screen. To avoid being effected by a
	 * <code>null</code> argument this method hides the renderer (light or
	 * heavy) using the following approach: (1) if the lightweight renderer is
	 * being used, ask it for it's parent container (should be the GlassPane of
	 * the containing Window), then remove itself from the container, (2) if the
	 * heavyweight renderer is being used, simply set it's visibility to false.
	 * 
	 * @param toolTip
	 *            A reference to the toolTip that is being hidden. This is
	 *            mainly passed for convenience, this implementation of the hide
	 *            method does not use this reference for anything.
	 */
	public void hideToolTip(ToolTip toolTip)
	{
		/*
		 * If we are using the lightweight renderer, we need to remove it from
		 * the glassPane of the top level window, otherwise we just need to hide
		 * the heavyweight renderer.
		 */
		if (useLightWeightRenderer)
		{
			/*
			 * Since the lightweight renderer is contained inside of the
			 * glassPane, we can simply ask the lightweight renderer for it's
			 * parent.
			 */
			Container parentContainer = lightWeightRenderer.getParent();

			/*
			 * And now remove the lightweight renderer from the glassPane which
			 * is the parentContainer.
			 */
			parentContainer.remove(lightWeightRenderer);
		}
		else
			heavyWeightRenderer.setVisible(false);
	}

	/**
	 * Class used to implement a heavy-weight renderer for the tooltip. This is
	 * done by extending the JWindow class. There is no comparable
	 * LightWeightRenderer class because that is simply the ToolTipDisplayPanel
	 * itself.
	 * <p>
	 * This class is used to display the tooltip when it cannot be shown within
	 * the bounds of the current window either due to size or location,
	 * otherwise the tooltip is placed into the GlassPane of the current window.
	 */
	private class HeavyWeightRenderer extends JWindow
	{
		private static final long serialVersionUID = 1L;

		public HeavyWeightRenderer(ToolTipDisplayPanel toolTipDisplayPanel)
		{
			setContentPane(toolTipDisplayPanel);
		}
	}
}