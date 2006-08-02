package com.kallasoft.avondale.panel;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.component.RootComponent;
import com.kallasoft.avondale.component.event.DefaultRootComponentEvent;
import com.kallasoft.avondale.component.event.RootComponentEvent;
import com.kallasoft.avondale.component.event.RootComponentListener;
import com.kallasoft.avondale.panel.tool.ComponentPanelTool;
import com.kallasoft.avondale.tooltip.DefaultToolTipManager;
import com.kallasoft.avondale.tooltip.ToolTipManager;

public class ComponentPanel extends JPanel implements Scrollable
{
	public static final boolean DEBUG = false;

	public static final String SNAP_TO_GRID_ENABLED_PROPERTY_NAME = "snapToGridEnabled";
	public static final String HORIZONTAL_GRID_ENABLED_PROPERTY_NAME = "horizontalGridEnabled";
	public static final String HORIZONTAL_GRID_SPACING_PROPERTY_NAME = "horizontalGridSpacing";
	public static final String HORIZONTAL_GRID_PAINT_PROPERTY_NAME = "horizontalGridPaint";
	public static final String HORIZONTAL_GRID_STROKE_PROPERTY_NAME = "horizontalGridStroke";
	public static final String VERTICAL_GRID_ENABLED_PROPERTY_NAME = "verticalGridEnabled";
	public static final String VERTICAL_GRID_SPACING_PROPERTY_NAME = "verticalGridSpacing";
	public static final String VERTICAL_GRID_PAINT_PROPERTY_NAME = "verticalGridPaint";
	public static final String VERTICAL_GRID_STROKE_PROPERTY_NAME = "verticalGridStroke";
	public static final String SCALE_PROPERTY_NAME = "scale";
	public static final String COMPONENT_PANEL_STATE_PROPERTY_NAME = "componentPanelState";
	public static final String TOOL_TIP_MANAGER_PROPERTY_NAME = "toolTipManager";
	public static final String OVERVIEW_PANEL_UPDATE_ENABLED_PROPERTY_NAME = "overviewPanelUpdateEnabled";
	public static final String OVERVIEW_PANEL_PROPERTY_NAME = "overviewPanel";
	public static final String ACTIVE_COMPONENT_PANEL_TOOL_PROPERTY_NAME = "activeComponentPanelTool";
	public static final String ROOT_COMPONENT_NOTIFICATION_ENABLED_PROPERTY_NAME = "rootComponentNotificationEnabled";

	public static final double DEFAULT_SCALE = 1.0;
	public static final double DEFAULT_GRID_SPACING = 16;
	public static final Paint DEFAULT_GRID_PAINT = new Color(230, 230, 230);
	public static final Stroke DEFAULT_GRID_STROKE = new BasicStroke();
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

	public final ContainerComponent BACKGROUND_ROOT_COMPONENT = new RootComponent();
	public final ContainerComponent COMPONENT_ROOT_COMPONENT = new RootComponent();
	public final ContainerComponent CONNECTION_ROOT_COMPONENT = new RootComponent();
	public final ContainerComponent ANNOTATION_ROOT_COMPONENT = new RootComponent();
	public final ContainerComponent DRAG_ROOT_COMPONENT = new RootComponent();

	private static final long serialVersionUID = 7712548014925799320L;

	private boolean overviewPanelUpdateEnabled = true;
	private boolean snapToGridEnabled = true;
	private boolean horizontalGridEnabled = true;
	private boolean verticalGridEnabled = true;
	private boolean rootComponentNotificationEnabled = true;
	private double scale = DEFAULT_SCALE;
	private double horizontalGridSpacing = DEFAULT_GRID_SPACING;
	private double verticalGridSpacing = DEFAULT_GRID_SPACING;
	private Paint horizontalGridPaint = DEFAULT_GRID_PAINT;
	private Paint verticalGridPaint = DEFAULT_GRID_PAINT;
	private Stroke horizontalGridStroke = DEFAULT_GRID_STROKE;
	private Stroke verticalGridStroke = DEFAULT_GRID_STROKE;
	private Line2D gridLine;
	private Dimension preferredSize;
	private Point2D scaledLocation;
	private ComponentPanelState componentPanelState;
	private ToolTipManager toolTipManager;
	private OverviewPanel overviewPanel;
	private ComponentPanelTool activeComponentPanelTool;
	private List<ContainerComponent> rootComponentList;
	private transient List<RootComponentListener> rootComponentListenerList;

	public ComponentPanel()
	{
		gridLine = new Line2D.Double();
		preferredSize = new Dimension();
		scaledLocation = new Point2D.Double();
		componentPanelState = new DefaultComponentPanelState();
		toolTipManager = new DefaultToolTipManager();
		rootComponentList = new ArrayList<ContainerComponent>(0);
		rootComponentListenerList = new ArrayList<RootComponentListener>(0);

		/* Add all the default rootComponents */
		addRootComponent(BACKGROUND_ROOT_COMPONENT);
		addRootComponent(COMPONENT_ROOT_COMPONENT);
		addRootComponent(CONNECTION_ROOT_COMPONENT);
		addRootComponent(ANNOTATION_ROOT_COMPONENT);
		addRootComponent(DRAG_ROOT_COMPONENT);

		/*
		 * Null the panel's layout so components can be absolutely positioned
		 * easily
		 */
		setLayout(null);

		/* Set the opaque property so the background is painted */
		setOpaque(true);
		setBackground(DEFAULT_BACKGROUND_COLOR);

		/* Enable key & mouse events to this panel */
		enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK
				| AWTEvent.MOUSE_MOTION_EVENT_MASK
				| AWTEvent.MOUSE_WHEEL_EVENT_MASK);
	}

	@Override
	public Dimension getPreferredSize()
	{
		Container parent = getParent();

		/*
		 * The parent's width/height is used so the panel atleast always fills
		 * it's parent's view
		 */
		double width = (parent == null ? getWidth() : parent.getWidth());
		double height = (parent == null ? getHeight() : parent.getHeight());

		double value = 0;

		for (int i = 0, size = getRootComponentCount(); i < size; i++)
		{
			ContainerComponent containerComponent = getRootComponent(i);

			/*
			 * Make sure the rootComponent is in a valid state before we take
			 * it's dimension for preferredSize calculation
			 */
			if (!containerComponent.isValid())
				containerComponent.validate();

			value = containerComponent.getWidth();

			if (value > width)
				width = value;

			value = containerComponent.getHeight();

			if (value > height)
				height = value;
		}

		double scale = getScale();

		/* Scale the preferredSize appropriately */
		preferredSize.setSize(scale * width, scale * height);
		return preferredSize;
	}

	@Override
	public void repaint(long tm, int x, int y, int width, int height)
	{
		double scale = getScale();

		/*
		 * Scale the repaint request to Swing based on the current scale of the
		 * panel.
		 */
		super.repaint(tm, (int) Math.floor(scale * x), (int) Math.floor(scale
				* y), (int) Math.ceil(scale * width), (int) Math.ceil(scale
				* height));
	}

	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}

	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		int value = 0;

		switch (orientation)
		{
			case SwingConstants.HORIZONTAL:
				value = (int) (visibleRect.getWidth() / 10);
				break;

			case SwingConstants.VERTICAL:
				value = (int) (visibleRect.getHeight() / 10);
				break;
		}

		return value;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		int value = 0;

		switch (orientation)
		{
			case SwingConstants.HORIZONTAL:
				value = (int) (visibleRect.getWidth());
				break;

			case SwingConstants.VERTICAL:
				value = (int) (visibleRect.getHeight());
				break;
		}

		return value;
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public boolean isSnapToGridEnabled()
	{
		return snapToGridEnabled;
	}

	public void setSnapToGridEnabled(boolean snapToGridEnabled)
	{
		boolean oldSnapToGridEnabled = isSnapToGridEnabled();

		if (oldSnapToGridEnabled == snapToGridEnabled)
			return;

		this.snapToGridEnabled = snapToGridEnabled;
		firePropertyChange(SNAP_TO_GRID_ENABLED_PROPERTY_NAME,
				oldSnapToGridEnabled, isSnapToGridEnabled());
	}

	public boolean isHorizontalGridEnabled()
	{
		return horizontalGridEnabled;
	}

	public void setHorizontalGridEnabled(boolean horizontalGridEnabled)
	{
		boolean oldHorizontalGridEnabled = isHorizontalGridEnabled();

		if (oldHorizontalGridEnabled == horizontalGridEnabled)
			return;

		this.horizontalGridEnabled = horizontalGridEnabled;
		firePropertyChange(HORIZONTAL_GRID_ENABLED_PROPERTY_NAME,
				oldHorizontalGridEnabled, isHorizontalGridEnabled());

		/* Repaint the panel */
		repaint();
	}

	public double getHorizontalGridSpacing()
	{
		return horizontalGridSpacing;
	}

	public void setHorizontalGridSpacing(double horizontalGridSpacing)
	{
		double oldHorizontalGridSpacing = getHorizontalGridSpacing();

		if (oldHorizontalGridSpacing == horizontalGridSpacing)
			return;

		this.horizontalGridSpacing = horizontalGridSpacing;
		firePropertyChange(HORIZONTAL_GRID_SPACING_PROPERTY_NAME,
				oldHorizontalGridSpacing, getHorizontalGridSpacing());

		/* Repaint the panel */
		repaint();
	}

	public Paint getHorizontalGridPaint()
	{
		return horizontalGridPaint;
	}

	public void setHorizontalGridPaint(Paint horizontalGridPaint)
	{
		Paint oldHorizontalGridPaint = getHorizontalGridPaint();

		if (oldHorizontalGridPaint == horizontalGridPaint)
			return;

		this.horizontalGridPaint = horizontalGridPaint;
		firePropertyChange(HORIZONTAL_GRID_PAINT_PROPERTY_NAME,
				oldHorizontalGridPaint, getHorizontalGridPaint());

		/* Repaint the panel */
		repaint();
	}

	public Stroke getHorizontalGridStroke()
	{
		return horizontalGridStroke;
	}

	public void setHorizontalGridStroke(Stroke horizontalGridStroke)
	{
		Stroke oldHorizontalGridStroke = getHorizontalGridStroke();

		if (oldHorizontalGridStroke == horizontalGridStroke)
			return;

		this.horizontalGridStroke = horizontalGridStroke;
		firePropertyChange(HORIZONTAL_GRID_STROKE_PROPERTY_NAME,
				oldHorizontalGridStroke, getHorizontalGridStroke());

		/* Repaint the panel */
		repaint();
	}

	public boolean isVerticalGridEnabled()
	{
		return verticalGridEnabled;
	}

	public void setVerticalGridEnabled(boolean verticalGridEnabled)
	{
		boolean oldVerticalGridEnabled = isVerticalGridEnabled();

		if (oldVerticalGridEnabled == verticalGridEnabled)
			return;

		this.verticalGridEnabled = verticalGridEnabled;
		firePropertyChange(VERTICAL_GRID_ENABLED_PROPERTY_NAME,
				oldVerticalGridEnabled, isVerticalGridEnabled());

		/* Repaint the panel */
		repaint();
	}

	public double getVerticalGridSpacing()
	{
		return verticalGridSpacing;
	}

	public void setVerticalGridSpacing(double verticalGridSpacing)
	{
		double oldVerticalGridSpacing = getVerticalGridSpacing();

		if (oldVerticalGridSpacing == verticalGridSpacing)
			return;

		this.verticalGridSpacing = verticalGridSpacing;
		firePropertyChange(VERTICAL_GRID_SPACING_PROPERTY_NAME,
				oldVerticalGridSpacing, getVerticalGridSpacing());

		/* Repaint the panel */
		repaint();
	}

	public Paint getVerticalGridPaint()
	{
		return verticalGridPaint;
	}

	public void setVerticalGridPaint(Paint verticalGridPaint)
	{
		Paint oldVerticleGridPaint = getVerticalGridPaint();

		if (oldVerticleGridPaint == verticalGridPaint)
			return;

		this.verticalGridPaint = verticalGridPaint;
		firePropertyChange(VERTICAL_GRID_PAINT_PROPERTY_NAME,
				oldVerticleGridPaint, getVerticalGridPaint());

		/* Repaint the panel */
		repaint();
	}

	public Stroke getVerticalGridStroke()
	{
		return verticalGridStroke;
	}

	public void setVerticalGridStroke(Stroke verticalGridStroke)
	{
		Stroke oldVerticalGridStroke = getVerticalGridStroke();

		if (oldVerticalGridStroke == verticalGridStroke)
			return;

		this.verticalGridStroke = verticalGridStroke;
		firePropertyChange(VERTICAL_GRID_STROKE_PROPERTY_NAME,
				oldVerticalGridStroke, getVerticalGridStroke());

		/* Repaint the panel */
		repaint();
	}

	public double getScale()
	{
		return scale;
	}

	public void setScale(double scale)
	{
		double oldScale = getScale();

		if (oldScale == scale)
			return;

		this.scale = scale;
		firePropertyChange(SCALE_PROPERTY_NAME, oldScale, getScale());

		/* Repaint the panel */
		repaint();
	}

	public ComponentPanelState getComponentPanelState()
	{
		return componentPanelState;
	}

	public void setComponentPanelState(ComponentPanelState componentPanelState)
	{
		if (componentPanelState == null)
			throw new IllegalArgumentException(
					"componentPanelState cannot be null");

		ComponentPanelState oldComponentPanelState = getComponentPanelState();

		if (oldComponentPanelState == componentPanelState)
			return;

		this.componentPanelState = componentPanelState;
		firePropertyChange(COMPONENT_PANEL_STATE_PROPERTY_NAME,
				oldComponentPanelState, getComponentPanelState());
	}

	public ToolTipManager getToolTipManager()
	{
		return toolTipManager;
	}

	public void setToolTipManager(ToolTipManager toolTipManager)
	{
		ToolTipManager oldToolTipManager = getToolTipManager();

		if (oldToolTipManager == toolTipManager)
			return;

		this.toolTipManager = toolTipManager;
		firePropertyChange(TOOL_TIP_MANAGER_PROPERTY_NAME, oldToolTipManager,
				getToolTipManager());
	}

	public boolean isOverviewPanelUpdateEnabled()
	{
		return overviewPanelUpdateEnabled;
	}

	public void setOverviewPanelUpdateEnabled(boolean overviewPanelUpdateEnabled)
	{
		boolean oldOverviewPanelUpdateEnabled = isOverviewPanelUpdateEnabled();

		if (oldOverviewPanelUpdateEnabled == overviewPanelUpdateEnabled)
			return;

		this.overviewPanelUpdateEnabled = overviewPanelUpdateEnabled;
		firePropertyChange(OVERVIEW_PANEL_UPDATE_ENABLED_PROPERTY_NAME,
				oldOverviewPanelUpdateEnabled, isOverviewPanelUpdateEnabled());
	}

	public OverviewPanel getOverviewPanel()
	{
		return overviewPanel;
	}

	public void setOverviewPanel(OverviewPanel overviewPanel)
	{
		OverviewPanel oldOverviewPanel = getOverviewPanel();

		if (oldOverviewPanel == overviewPanel)
			return;

		if (oldOverviewPanel != null)
			oldOverviewPanel.setComponentPanel(null);

		this.overviewPanel = overviewPanel;

		if (this.overviewPanel != null
				&& this.overviewPanel.getComponentPanel() != this)
			this.overviewPanel.setComponentPanel(this);

		firePropertyChange(OVERVIEW_PANEL_PROPERTY_NAME, oldOverviewPanel,
				getOverviewPanel());
	}

	public ComponentPanelTool getActiveComponentPanelTool()
	{
		return activeComponentPanelTool;
	}

	public void setActiveComponentPanelTool(
			ComponentPanelTool activeComponentPanelTool)
	{
		ComponentPanelTool oldComponentPanelTool = getActiveComponentPanelTool();

		if (oldComponentPanelTool == activeComponentPanelTool)
			return;

		if (oldComponentPanelTool != null)
			oldComponentPanelTool.deactivate(this);

		this.activeComponentPanelTool = activeComponentPanelTool;

		if (this.activeComponentPanelTool != null)
			this.activeComponentPanelTool.activate(this);

		firePropertyChange(ACTIVE_COMPONENT_PANEL_TOOL_PROPERTY_NAME,
				oldComponentPanelTool, getActiveComponentPanelTool());
	}

	public boolean containsRootComponent(ContainerComponent rootComponent)
	{
		return rootComponentList.contains(rootComponent);
	}

	public void addRootComponent(ContainerComponent rootComponent)
	{
		addRootComponent(getRootComponentCount(), rootComponent);
	}

	public void addRootComponent(int index, ContainerComponent rootComponent)
	{
		if (rootComponent == null)
			throw new NullPointerException("rootComponent cannot be null");

		boolean fireEvent = false;

		if (!containsRootComponent(rootComponent))
		{
			rootComponentList.add(index, rootComponent);
			rootComponent.setComponentPanel(this);
			fireEvent = true;
		}

		if (fireEvent)
			fireRootComponentEvent(new DefaultRootComponentEvent(this,
					RootComponentEvent.EventType.ROOT_COMPONENT_ADDED,
					rootComponent));
	}

	public int getRootComponentCount()
	{
		return rootComponentList.size();
	}

	public int getIndexOfRootComponent(ContainerComponent rootComponent)
	{
		return rootComponentList.indexOf(rootComponent);
	}

	public ContainerComponent getRootComponent(int index)
	{
		return rootComponentList.get(index);
	}

	public Component[] getRootComponents()
	{
		return rootComponentList
				.toArray(new Component[getRootComponentCount()]);
	}

	public void removeRootComponent(int index)
	{
		boolean fireEvent = false;
		ContainerComponent rootComponent = rootComponentList.remove(index);

		if (rootComponent != null)
		{
			rootComponent.setComponentPanel(null);
			fireEvent = true;
		}

		if (fireEvent)
			fireRootComponentEvent(new DefaultRootComponentEvent(this,
					RootComponentEvent.EventType.ROOT_COMPONENT_REMOVED,
					rootComponent));
	}

	public void removeRootComponent(ContainerComponent rootComponent)
	{
		if (rootComponent == null)
			return;

		int index = getIndexOfRootComponent(rootComponent);
		
		if(index > -1)
			removeRootComponent(index);
	}

	public boolean isRootComponentNotificationEnabled()
	{
		return rootComponentNotificationEnabled;
	}

	public void setRootComponentNotificationEnabled(
			boolean componentLayerNotificationEnabled)
	{
		boolean oldRootComponentNotificationEnabled = isRootComponentNotificationEnabled();

		if (isRootComponentNotificationEnabled() == componentLayerNotificationEnabled)
			return;

		this.rootComponentNotificationEnabled = componentLayerNotificationEnabled;
		firePropertyChange(ROOT_COMPONENT_NOTIFICATION_ENABLED_PROPERTY_NAME,
				oldRootComponentNotificationEnabled,
				isRootComponentNotificationEnabled());
	}

	public boolean containsRootComponentListener(
			RootComponentListener rootComponentListener)
	{
		return rootComponentListenerList.contains(rootComponentListener);
	}

	public void addRootComponentListener(
			RootComponentListener rootComponentListener)
	{
		addRootComponentListener(getRootComponentListenerCount(),
				rootComponentListener);
	}

	public void addRootComponentListener(int index,
			RootComponentListener rootComponentListener)
	{
		if (rootComponentListener == null)
			return;

		if (!containsRootComponentListener(rootComponentListener))
			rootComponentListenerList.add(index, rootComponentListener);
	}

	public int getRootComponentListenerCount()
	{
		return rootComponentListenerList.size();
	}

	public int getIndexOfRootComponentListener(
			RootComponentListener rootComponentListener)
	{
		return rootComponentListenerList.indexOf(rootComponentListener);
	}

	public RootComponentListener getRootComponentListener(int index)
	{
		return rootComponentListenerList.get(index);
	}

	public RootComponentListener[] getRootComponentListeners()
	{
		return rootComponentListenerList
				.toArray(new RootComponentListener[getRootComponentCount()]);
	}

	public void removeRootComponentListener(int index)
	{
		rootComponentListenerList.remove(index);
	}

	public void removeRootComponentListener(
			RootComponentListener rootComponentListener)
	{
		if (rootComponentListener == null)
			return;

		int index = getIndexOfRootComponentListener(rootComponentListener);
		
		if(index > -1)
			removeRootComponentListener(index);
	}

	public void removeRootComponentListeners()
	{
		rootComponentListenerList.clear();
	}

	/* TODO: Consider removing this so wheel operations can be used to zoom and
	 * are processed by the componentPanel, not sure of another application that
	 * uses wheel for the individual components or what it would even do if it did. */
	@Override
	protected void processMouseWheelEvent(MouseWheelEvent evt)
	{
		super.processMouseWheelEvent(evt);
	
		Point2D scaledLocation = getScaledLocation(evt.getX(), evt.getY());
		int xDiff = (int) (scaledLocation.getX() - evt.getX());
		int yDiff = (int) (scaledLocation.getY() - evt.getY());
	
		/* Scale the event's coordinates appropriately */
		evt.translatePoint(xDiff, yDiff);
	
		for (int i = getRootComponentCount() - 1; !evt.isConsumed() && i > -1; i--)
			getRootComponent(i).processMouseWheelEvent(evt);
	
		/* Translate the event's coordinates back to their original location */
		evt.translatePoint(-xDiff, -yDiff);
	}

	@Override
	protected void processMouseMotionEvent(MouseEvent evt)
	{
		super.processMouseMotionEvent(evt);
	
		Point2D scaledLocation = getScaledLocation(evt.getX(), evt.getY());
		int xDiff = (int) (scaledLocation.getX() - evt.getX());
		int yDiff = (int) (scaledLocation.getY() - evt.getY());
	
		/* Scale the event's coordinates appropriately */
		evt.translatePoint(xDiff, yDiff);
	
		for (int i = getRootComponentCount() - 1; !evt.isConsumed() && i > -1; i--)
			getRootComponent(i).processMouseMotionEvent(evt);
	
		/* Translate the event's coordinates back to their original location */
		evt.translatePoint(-xDiff, -yDiff);
	}

	@Override
	protected void processMouseEvent(MouseEvent evt)
	{
		super.processMouseEvent(evt);
	
		Point2D scaledLocation = getScaledLocation(evt.getX(), evt.getY());
		int xDiff = (int) (scaledLocation.getX() - evt.getX());
		int yDiff = (int) (scaledLocation.getY() - evt.getY());
	
		/* Scale the event's coordinates appropriately */
		evt.translatePoint(xDiff, yDiff);
	
		for (int i = getRootComponentCount() - 1; !evt.isConsumed() && i > -1; i--)
			getRootComponent(i).processMouseEvent(evt);
	
		/* Translate the event's coordinates back to their original location */
		evt.translatePoint(-xDiff, -yDiff);
	}

	@Override
	protected void processKeyEvent(KeyEvent evt)
	{
		super.processKeyEvent(evt);
	
		Component focusedComponent = getComponentPanelState()
				.getFocusedComponent();
	
		if (focusedComponent != null)
			focusedComponent.processKeyEvent(evt);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		double scale = getScale();
		Graphics2D g2d = (Graphics2D) g;

		/* Honor the opaque setting */
		if (isOpaque())
		{
			Color oldColor = g2d.getColor();
			g2d.setColor(getBackground());
			g2d.fill(getVisibleRect());
			g2d.setColor(oldColor);
		}

		/* Paint the grid */
		paintHorizontalGridLines(g2d);
		paintVerticalGridLines(g2d);

		/* Remember the Graphic's original transform so it can be restored later */
		AffineTransform transform = g2d.getTransform();

		/* Scale the graphics appropriately */
		g2d.scale(scale, scale);

		/* Paint the normal rootComponent hierarchy */
		for (int i = 0, size = getRootComponentCount(); i < size; i++)
			getRootComponent(i).paint(g2d);

		/* Return the graphics original transform */
		g2d.setTransform(transform);

		/* Repaint the OverviewPanel. This probably isn't necessary as simply
		 * dragging a window over the ComponentPanel will cause it to repaint
		 * but doesn't necessarily mean the OverviewPanel needs to repaint. The
		 * problem is that we don't want the ComponentPanel listening for
		 * *ever* possible event that could occur in the component hierarchy to
		 * know when it should and shouldn't repaint the OverviewPanel. So to 
		 * play it safe, we repaint it every time the ComponentPanel repaints. */
		OverviewPanel overviewPanel = getOverviewPanel();

		if (overviewPanel != null)
			overviewPanel.repaint();
	}
	
	protected void paintHorizontalGridLines(Graphics2D g2d)
	{
		Paint gridPaint = getHorizontalGridPaint();
		Stroke gridStroke = getHorizontalGridStroke();

		if (isHorizontalGridEnabled() && gridPaint != null
				&& gridStroke != null)
		{
			double horizontalGridSpacing = getHorizontalGridSpacing();
			Paint oldPaint = g2d.getPaint();
			Stroke oldStroke = g2d.getStroke();
			Rectangle visibleRectangle = getVisibleRect();
			g2d.setPaint(gridPaint);
			g2d.setStroke(gridStroke);

			if (horizontalGridSpacing > 0)
			{
				int width = getWidth();
				int maxY = (int) (visibleRectangle.getY() + visibleRectangle
						.getHeight());
				int numberOfLines = (int) (getHeight() / horizontalGridSpacing);

				for (int i = (int) (visibleRectangle.getY() / horizontalGridSpacing); i < numberOfLines; i++)
				{
					double y = i * horizontalGridSpacing;

					if (y > maxY)
						break;

					gridLine.setLine(0, y, width, y);
					g2d.draw(gridLine);
				}
			}

			g2d.setPaint(oldPaint);
			g2d.setStroke(oldStroke);
		}
	}
	
	protected void paintVerticalGridLines(Graphics2D g2d)
	{
		Paint gridPaint = getVerticalGridPaint();
		Stroke gridStroke = getVerticalGridStroke();

		if (isVerticalGridEnabled() && gridPaint != null && gridStroke != null)
		{
			double verticalGridSpacing = getVerticalGridSpacing();
			Paint oldPaint = g2d.getPaint();
			Stroke oldStroke = g2d.getStroke();
			Rectangle visibleRectangle = getVisibleRect();
			g2d.setPaint(gridPaint);
			g2d.setStroke(gridStroke);

			if (verticalGridSpacing > 0)
			{
				int height = getHeight();
				int maxX = (int) (visibleRectangle.getX() + visibleRectangle
						.getWidth());
				int numberOfLines = (int) (getWidth() / verticalGridSpacing);

				for (int i = (int) (visibleRectangle.getX() / verticalGridSpacing); i < numberOfLines; i++)
				{
					double x = i * verticalGridSpacing;

					if (x > maxX)
						break;

					gridLine.setLine(x, 0, x, height);
					g2d.draw(gridLine);
				}
			}

			g2d.setPaint(oldPaint);
			g2d.setStroke(oldStroke);
		}
	}

	protected void fireRootComponentEvent(RootComponentEvent evt)
	{
		if (evt == null || !isRootComponentNotificationEnabled())
			return;

		for (int i = 0, size = getRootComponentListenerCount(); i < size; i++)
		{
			switch (evt.getEventType())
			{
				case ROOT_COMPONENT_ADDED:
					getRootComponentListener(i).rootComponentAdded(evt);
					break;

				case ROOT_COMPONENT_REMOVED:
					getRootComponentListener(i).rootComponentRemoved(evt);
					break;
			}
		}
	}

	private Point2D getScaledLocation(double x, double y)
	{
		double factor = 1.0 / getScale();
		scaledLocation.setLocation(factor * x, factor * y);
	
		return scaledLocation;
	}
}