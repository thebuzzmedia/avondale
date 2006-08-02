package com.kallasoft.avondale.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.VolatileImage;

import javax.swing.JPanel;

import com.kallasoft.avondale.component.Component;

/*
 * TODO: Possibly look into adding a mechanism that queues up clear events, and
 * every time a new one comes in the timer resets, if the timer reaches 0, then
 * the buffer is invalidated and repainted.
 */
public class OverviewPanel extends JPanel
{
	public static final Paint DEFAULT_VISIBLE_REGION_PAINT = Color.RED;
	public static final Stroke DEFAULT_VISIBLE_REGION_STROKE = new BasicStroke();

	public static final String SHOW_VISIBLE_REGION_PROPERTY_NAME = "showVisibleRegion";
	public static final String VISIBLE_REGION_PAINT_PROPERTY_NAME = "visibleRegionPaint";
	public static final String VISIBLE_REGION_STROKE_PROPERTY_NAME = "visibileRegionSroke";
	public static final String COMPONENT_PANEL_PROPERTY_NAME = "componentPanel";

	private static final long serialVersionUID = -3263477584989768467L;

	private boolean showVisibleRegion = true;
	private double[] mousePressedXYOffsets;
	private Paint visibleRegionPaint = DEFAULT_VISIBLE_REGION_PAINT;
	private Stroke visibleRegionStroke = DEFAULT_VISIBLE_REGION_STROKE;
	private ComponentPanel componentPanel;
	private Rectangle2D visibleRegionBounds;
	private VolatileImage buffer;

	public OverviewPanel()
	{
		this(null);
	}

	public OverviewPanel(ComponentPanel componentPanel)
	{
		this.visibleRegionBounds = new Rectangle2D.Double();
		this.mousePressedXYOffsets = new double[2];

		setOpaque(true);
		setComponentPanel(componentPanel);

		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{
				mousePressedXYOffsets[0] = evt.getX()
						- visibleRegionBounds.getX();
				mousePressedXYOffsets[1] = evt.getY()
						- visibleRegionBounds.getY();
			}

			public void mouseReleased(MouseEvent evt)
			{
				mousePressedXYOffsets[0] = -1;
				mousePressedXYOffsets[1] = -1;
			}
		});

		addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent evt)
			{
				ComponentPanel componentPanel = getComponentPanel();

				/* Don't do anything if there is no associated componentPanel */
				if (componentPanel == null)
					return;

				double x = evt.getX() - mousePressedXYOffsets[0];
				double y = evt.getY() - mousePressedXYOffsets[1];

				/*
				 * Calculate relative scale of this overviewPanel's size to the
				 * componentPanel's size and then determine the factor in order
				 * to scale the overviewPanel's coordinates up to the
				 * componentPanel's.
				 */
				double xScale = (double) getWidth()
						/ (double) componentPanel.getWidth();
				double yScale = (double) getHeight()
						/ (double) componentPanel.getHeight();
				double xFactor = 1.0 / xScale;
				double yFactor = 1.0 / yScale;

				/*
				 * Using the factor determine above and new x/y coordinates,
				 * determine the new visible location of the componentPanel that
				 * we should scale to.
				 */
				Rectangle visibleRectangle = componentPanel.getVisibleRect();
				visibleRectangle.setRect(xFactor * x, yFactor * y,
						visibleRectangle.getWidth(), visibleRectangle
								.getHeight());

				componentPanel.scrollRectToVisible(visibleRectangle);

				/*
				 * Repaint the whole overviewPanel. An optimization here would
				 * to only repaint the area that is the union between the old
				 * indicator region and new, but because the overviewPanel now
				 * is rendering an image of the graph and is typically quite
				 * small compared to the full graph, taking the time to compute
				 * the unions of the old and new indicator regions is likely a
				 * waste of time.
				 */
				repaint();
			}
		});
	}

	public void invalidateBuffer()
	{
		buffer = null;
	}

	public ComponentPanel getComponentPanel()
	{
		return componentPanel;
	}

	public void setComponentPanel(ComponentPanel componentPanel)
	{
		ComponentPanel oldComponentPanel = getComponentPanel();

		if (oldComponentPanel == componentPanel)
			return;

		if (oldComponentPanel != null)
			oldComponentPanel.setOverviewPanel(null);

		this.componentPanel = componentPanel;

		if (this.componentPanel != null
				&& this.componentPanel.getOverviewPanel() != this)
			this.componentPanel.setOverviewPanel(this);

		firePropertyChange(COMPONENT_PANEL_PROPERTY_NAME, oldComponentPanel,
				getComponentPanel());
	}

	public boolean isShowVisibleRegion()
	{
		return showVisibleRegion;
	}

	public void setShowVisibleRegion(boolean showVisibleRegion)
	{
		boolean oldShowVisibleRegion = isShowVisibleRegion();
		
		if (oldShowVisibleRegion == showVisibleRegion)
			return;

		this.showVisibleRegion = showVisibleRegion;
		firePropertyChange(SHOW_VISIBLE_REGION_PROPERTY_NAME, oldShowVisibleRegion,
				isShowVisibleRegion());

		/* Repaint overview */
		repaint();
	}

	public Paint getVisibleRegionPaint()
	{
		return visibleRegionPaint;
	}

	public void setVisibleRegionPaint(Paint visibleRegionPaint)
	{
		Paint oldVisibleRegionPaint = getVisibleRegionPaint();

		if (oldVisibleRegionPaint == visibleRegionPaint)
			return;

		this.visibleRegionPaint = visibleRegionPaint;
		firePropertyChange(VISIBLE_REGION_PAINT_PROPERTY_NAME,
				oldVisibleRegionPaint, getVisibleRegionPaint());

		/* Repaint overview */
		repaint();
	}

	public Stroke getVisibleRegionStroke()
	{
		return visibleRegionStroke;
	}

	public void setVisibleRegionStroke(Stroke visibleRegionStroke)
	{
		Stroke oldVisibleRegionStroke = getVisibleRegionStroke();

		if (oldVisibleRegionStroke == visibleRegionStroke)
			return;

		this.visibleRegionStroke = visibleRegionStroke;
		firePropertyChange(VISIBLE_REGION_STROKE_PROPERTY_NAME,
				oldVisibleRegionStroke, getVisibleRegionStroke());

		/* Repaint overview */
		repaint();
	}

	public void repaintComponentPanelRegion(double x, double y, double width,
			double height)
	{
		ComponentPanel componentPanel = getComponentPanel();

		if (componentPanel == null)
			return;

		double xScale = (double) getWidth()
				/ (double) componentPanel.getWidth();
		double yScale = (double) getHeight()
				/ (double) componentPanel.getHeight();

		repaint((int) Math.floor(xScale * x), (int) Math.floor(yScale * y),
				(int) Math.ceil(xScale * width), (int) Math.ceil(yScale
						* height));
	}

	public void repaintComponentPanelRegion(Rectangle2D regionBounds)
	{
		repaintComponentPanelRegion(regionBounds.getX(), regionBounds.getY(),
				regionBounds.getWidth(), regionBounds.getHeight());
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		ComponentPanel componentPanel = getComponentPanel();

		/*
		 * This overview has nothing to paint if it isn't connected with a
		 * componentPanel.
		 */
		if (componentPanel == null)
			return;

		/*
		 * Check if the buffer exists, if it does paint it, otherwise we need to
		 * regenerate it.
		 */
		if (buffer == null || buffer.contentsLost())
		{
			buffer = createVolatileImage(getWidth(), getHeight());

			/* Unable to create a volatileImage, probably running headless */
			if (buffer == null)
				return;

			Graphics2D g2d = buffer.createGraphics();
			g2d.setClip(g.getClip());

			/* Honor the opaque setting */
			if (isOpaque())
			{
				Color oldColor = g2d.getColor();
				g2d.setColor(componentPanel.getBackground());
				g2d.fill(getVisibleRect());
				g2d.setColor(oldColor);
			}

			/*
			 * Calculate relative scale of this overviewPanel's size to the
			 * componentPanel's size
			 */
			double xScale = (double) getWidth()
					/ (double) componentPanel.getWidth();
			double yScale = (double) getHeight()
					/ (double) componentPanel.getHeight();
			double componentPanelScale = componentPanel.getScale();
			Component[] rootComponents = componentPanel.getRootComponents();

			/*
			 * Scale the graphics appropriately, if we don't multiply by the
			 * componentPanel's scale here, the OverviewPanel begins to zoom in
			 * instead of staying at a constant level.
			 */
			g2d.scale(componentPanelScale * xScale, componentPanelScale
					* yScale);

			for (int i = 0; i < rootComponents.length; i++)
				rootComponents[i].paint(g2d);

			/* Dispose of the buffer's Graphics2D object */
			g2d.dispose();
		}

		if (buffer == null || buffer.contentsLost())
			return;

		g.drawImage(buffer, 0, 0, this);

		/* Paint the indicator showing the current visible region */
		paintVisibleRegionIndicator((Graphics2D) g);
	}

	protected void paintVisibleRegionIndicator(Graphics2D g2d)
	{
		/* Now paint the visible region indicator */
		Paint visibleRegionPaint = getVisibleRegionPaint();
		Stroke visibleRegionStroke = getVisibleRegionStroke();

		if (!isShowVisibleRegion() || visibleRegionPaint == null
				|| visibleRegionStroke == null)
			return;

		/*
		 * Calculate relative scale of this overviewPanel's size to the
		 * componentPanel's size
		 */
		double xScale = (double) getWidth()
				/ (double) componentPanel.getWidth();
		double yScale = (double) getHeight()
				/ (double) componentPanel.getHeight();

		Paint oldPaint = g2d.getPaint();
		Stroke oldStroke = g2d.getStroke();
		Rectangle visibleRectangle = componentPanel.getVisibleRect();

		/*
		 * Determine if the visibleRectangle is smaller than or equal to to the
		 * size of the componentPanel, in which case there is no need to paint
		 * an indicator of the visible region.
		 */
		if (visibleRectangle.getX() >= componentPanel.getX()
				&& visibleRectangle.getY() >= componentPanel.getY()
				&& visibleRectangle.getWidth() >= componentPanel.getWidth()
				&& visibleRectangle.getHeight() >= componentPanel.getHeight())
			return;

		visibleRegionBounds.setRect(xScale * visibleRectangle.getX(), yScale
				* visibleRectangle.getY(),
				xScale * visibleRectangle.getWidth(), yScale
						* visibleRectangle.getHeight());

		g2d.setPaint(visibleRegionPaint);
		g2d.setStroke(visibleRegionStroke);
		g2d.draw(visibleRegionBounds);
		g2d.setPaint(oldPaint);
		g2d.setStroke(oldStroke);
	}
}