package com.kallasoft.avondale.component;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.kallasoft.avondale.DefaultSpacer;
import com.kallasoft.avondale.Spacer;
import com.kallasoft.avondale.component.border.ComponentBorder;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.ComponentPainter;
import com.kallasoft.avondale.event.ValidationEvent;
import com.kallasoft.avondale.event.ValidationListener;
import com.kallasoft.avondale.panel.ComponentPanel;
import com.kallasoft.avondale.tooltip.ToolTip;
import com.kallasoft.ext.bean.AbstractPropertyChangeSupport;
import com.kallasoft.ext.bean.PropertyChangeSupport;

public abstract class AbstractComponent extends AbstractPropertyChangeSupport
		implements Component
{
	protected Rectangle2D minBounds;
	protected Rectangle2D bounds;
	protected Rectangle2D maxBounds;

	private boolean opaque = true;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean virtual = false;
	private boolean focused = false;
	private boolean focusable = true;
	private boolean mouseOver = false;
	private boolean mousePressed = false;

	private boolean keyNotificationEnabled = true;
	private boolean mouseNotificationEnabled = true;
	private boolean mouseMotionNotificationEnabled = true;
	private boolean mouseWheelNotificationEnabled = true;
	private boolean validationNotificationEnabled = true;

	private Spacer padding;
	private ComponentBorder border;
	private Paint backgroundPaint;
	private Paint foregroundPaint;
	private ComponentPainter componentPainter;
	private Cursor cursor;
	private ComponentModel componentModel;
	private Object layoutConstraints;
	private ToolTip toolTip;
	private ContainerComponent parentComponent;
	private transient ComponentPanel componentPanel;

	private transient List<KeyListener> keyListenerList;
	private transient List<MouseListener> mouseListenerList;
	private transient List<MouseMotionListener> mouseMotionListenerList;
	private transient List<MouseWheelListener> mouseWheelListenerList;
	private transient List<ValidationListener> validationListenerList;

	public AbstractComponent(ComponentModel componentModel)
	{
		/* Initialize the component's initial default state */
		padding = new DefaultSpacer();
		minBounds = new Rectangle2D.Double(-Double.MAX_VALUE,
				-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
		bounds = new Rectangle2D.Double();
		maxBounds = new Rectangle2D.Double(Double.MAX_VALUE, Double.MAX_VALUE,
				Double.MAX_VALUE, Double.MAX_VALUE);

		/* Initialize the listener lists */
		keyListenerList = new ArrayList<KeyListener>(0);
		mouseListenerList = new ArrayList<MouseListener>(0);
		mouseMotionListenerList = new ArrayList<MouseMotionListener>(0);
		mouseWheelListenerList = new ArrayList<MouseWheelListener>(0);
		validationListenerList = new ArrayList<ValidationListener>(0);

		/* Set the componentModel */
		setComponentModel(componentModel);
	}

	/**
	 * Used to react to <code>PropertyChangeEvent</code>s from the
	 * component's <code>ComponentModel</code> by repainting itself.
	 * 
	 * @param evt The <code>PropertyChangeEvent</code> fired by the
	 *            component's <code>ComponentModel</code>.
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		/* React to model changes by repainting self */
		repaint();
	}

	public boolean isVirtual()
	{
		return virtual;
	}

	public void setVirtual(boolean virtual)
	{
		boolean oldVirtual = isVirtual();
		
		if (oldVirtual == virtual)
			return;

		this.virtual = virtual;
		firePropertyChangeEvent(this, VIRTUAL_PROPERTY_NAME, oldVirtual,
				isVirtual());

		/* Repaint the component */
		repaint();
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		boolean oldEnabled = isEnabled();
		
		if (oldEnabled == enabled)
			return;

		this.enabled = enabled;
		firePropertyChangeEvent(this, ENABLED_PROPERTY_NAME, oldEnabled,
				isEnabled());

		/* Repaint the component */
		repaint();
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		boolean oldVisible = isVisible();
		
		if (oldVisible == visible)
			return;

		this.visible = visible;
		firePropertyChangeEvent(this, VISIBLE_PROPERTY_NAME, oldVisible,
				isVisible());

		/* Repaint the component */
		repaint();
	}

	public boolean isOpaque()
	{
		return opaque;
	}

	public void setOpaque(boolean opaque)
	{
		boolean oldOpaque = isOpaque();
		
		if (oldOpaque == opaque)
			return;

		this.opaque = opaque;
		firePropertyChangeEvent(this, OPAQUE_PROPERTY_NAME, oldOpaque,
				isOpaque());

		/* Repaint the component */
		repaint();
	}

	public boolean isFocusable()
	{
		return focusable;
	}

	public void setFocusable(boolean focusable)
	{
		boolean oldFocusable = isFocusable();
		
		if (oldFocusable == focusable)
			return;
		
		/* Update the focus state if this is no longer a focusable component 
		 * and it is currently focused. */
		if (!isFocusable() && isFocused())
			setFocused(false);

		this.focusable = focusable;
		firePropertyChangeEvent(this, FOCUSABLE_PROPERTY_NAME, oldFocusable,
				isFocusable());
	}

	public boolean isFocused()
	{
		return focused;
	}

	public void setFocused(boolean focused)
	{
		if(focused && !isFocusable())
			throw new IllegalStateException("This component cannot be focused while it's focusable property is false");
		
		boolean oldFocused = isFocused();
		
		if (!isFocusable() || (oldFocused == focused))
			return;

		this.focused = focused;
		firePropertyChangeEvent(this, FOCUSED_PROPERTY_NAME, oldFocused,
				isFocused());
	}

	public boolean isMouseOver()
	{
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver)
	{
		boolean oldMouseOver = isMouseOver();
		
		if (oldMouseOver == mouseOver)
			return;

		this.mouseOver = mouseOver;
		firePropertyChangeEvent(this, MOUSE_OVER_PROPERTY_NAME, oldMouseOver,
				isMouseOver());
	}

	public boolean isMousePressed()
	{
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed)
	{
		boolean oldMousePressed = isMousePressed();
		
		if (oldMousePressed == mousePressed)
			return;

		this.mousePressed = mousePressed;
		firePropertyChangeEvent(this, MOUSE_PRESSED_PROPERTY_NAME, oldMousePressed,
				isMousePressed());
	}

	public boolean contains(double x, double y)
	{
		return (isVisible() && getBounds().contains(x, y));
	}

	public boolean contains(Point2D location)
	{
		return contains(location.getX(), location.getY());
	}

	public double getX()
	{
		return bounds.getX();
	}

	public double getCenterX()
	{
		return (getX() + getWidth()) / 2;
	}

	public double getY()
	{
		return bounds.getY();
	}

	public double getCenterY()
	{
		return (getY() + getHeight()) / 2;
	}

	public Point2D getLocation()
	{
		return new Point2D.Double(getX(), getY());
	}

	public double getWidth()
	{
		return bounds.getWidth();
	}

	public double getHeight()
	{
		return bounds.getHeight();
	}

	public Dimension2D getSize()
	{
		Dimension size = new Dimension();
		size.setSize(getWidth(), getHeight());
		return size;
	}

	public Spacer getPadding()
	{
		return new DefaultSpacer(padding);
	}

	public void setPadding(Spacer padding)
	{
		Spacer oldPadding = getPadding();

		if (oldPadding == padding)
			return;

		this.padding.setSpaces(padding);
		firePropertyChangeEvent(this, PADDING_PROPERTY_NAME, oldPadding,
				getPadding());

		/* Repaint the component */
		repaint();
	}

	public Paint getBackgroundPaint()
	{
		return backgroundPaint;
	}

	public void setBackgroundPaint(Paint backgroundPaint)
	{
		Paint oldBackgroundPaint = getBackgroundPaint();

		if (oldBackgroundPaint == backgroundPaint)
			return;

		this.backgroundPaint = backgroundPaint;
		firePropertyChangeEvent(this, BACKGROUND_PAINT_PROPERTY_NAME,
				oldBackgroundPaint, getBackgroundPaint());

		/* Repaint the component */
		repaint();
	}

	public Paint getForegroundPaint()
	{
		return foregroundPaint;
	}

	public void setForegroundPaint(Paint foregroundPaint)
	{
		Paint oldForegroundPaint = getForegroundPaint();

		if (oldForegroundPaint == foregroundPaint)
			return;

		this.foregroundPaint = foregroundPaint;
		firePropertyChangeEvent(this, FOREGROUND_PAINT_PROPERTY_NAME,
				oldForegroundPaint, getForegroundPaint());

		/* Repaint the component */
		repaint();
	}

	public ComponentBorder getComponentBorder()
	{
		return border;
	}

	public void setComponentBorder(ComponentBorder componentBorder)
	{
		ComponentBorder oldBorder = getComponentBorder();

		if (oldBorder == componentBorder)
			return;

		this.border = componentBorder;
		firePropertyChangeEvent(this, COMPONENT_BORDER_PROPERTY_NAME,
				oldBorder, getComponentBorder());

		/* Repaint the component */
		repaint();
	}

	public ComponentPainter getComponentPainter()
	{
		return componentPainter;
	}

	public void setComponentPainter(ComponentPainter componentPainter)
	{
		ComponentPainter oldComponentPainter = getComponentPainter();

		if (oldComponentPainter == componentPainter)
			return;

		this.componentPainter = componentPainter;
		firePropertyChangeEvent(this, COMPONENT_PAINTER_PROPERTY_NAME,
				oldComponentPainter, getComponentPainter());

		/* Repaint the component */
		repaint();
	}

	public Cursor getCursor()
	{
		return cursor;
	}

	public void setCursor(Cursor cursor)
	{
		Cursor oldCursor = getCursor();

		if (oldCursor == cursor)
			return;

		this.cursor = cursor;
		firePropertyChangeEvent(this, CURSOR_PROPERTY_NAME, oldCursor,
				getCursor());
	}

	public ComponentModel getComponentModel()
	{
		return componentModel;
	}

	public void setComponentModel(ComponentModel componentModel)
	{
		PropertyChangeSupport oldComponentModel = getComponentModel();

		if (oldComponentModel == componentModel)
			return;

		/* Remove property change listener */
		if (oldComponentModel != null)
			oldComponentModel.removePropertyChangeListener(this);

		this.componentModel = componentModel;

		/* Add property change listener */
		if (this.componentModel != null)
			this.componentModel.addPropertyChangeListener(this);

		firePropertyChangeEvent(this, COMPONENT_MODEL_PROPERTY_NAME,
				oldComponentModel, getComponentModel());

		/* Repaint the component */
		repaint();
	}

	public Object getLayoutConstraints()
	{
		return layoutConstraints;
	}

	public void setLayoutConstraints(Object layoutConstraints)
	{
		Object oldLayoutConstraints = getLayoutConstraints();

		if (oldLayoutConstraints == layoutConstraints)
			return;

		this.layoutConstraints = layoutConstraints;
		firePropertyChangeEvent(this, LAYOUT_CONSTRAINTS_PROPERTY_NAME,
				oldLayoutConstraints, getLayoutConstraints());

		/* Invalidate this component */
		invalidate();

		// ContainerComponent parentComponent = getParentComponent();
		//
		// if (parentComponent != null)
		// parentComponent.layoutContainer();
	}

	public ToolTip getToolTip()
	{
		return toolTip;
	}

	public void setToolTip(ToolTip toolTip)
	{
		ToolTip oldToolTip = getToolTip();

		if (oldToolTip == toolTip)
			return;

		this.toolTip = toolTip;
		firePropertyChangeEvent(this, TOOL_TIP_PROPERTY_NAME, oldToolTip,
				getToolTip());
	}
	
	public boolean isRootComponent()
	{
		return (getParentComponent() == null);
	}

	public ContainerComponent getRootComponent()
	{
		ContainerComponent rootComponent = getParentComponent();

		/*
		 * If there is no parent and this component is an instance of
		 * ContainerComponent, then this component is the rootComponent, so
		 * return it.
		 */
		if (rootComponent == null && this instanceof ContainerComponent)
			rootComponent = (ContainerComponent) this;
		else
		{
			/* Otherwise cycle up the chain of parentComponents to find the root */
			while (rootComponent != null
					&& rootComponent.getParentComponent() != null)
				rootComponent = rootComponent.getParentComponent();
		}

		return rootComponent;
	}

	public ContainerComponent getParentComponent()
	{
		return parentComponent;
	}

	public void setParentComponent(ContainerComponent parentComponent)
	{
		ContainerComponent oldParentComponent = getParentComponent();

		if (oldParentComponent == parentComponent)
			return;

		this.parentComponent = parentComponent;
		firePropertyChangeEvent(this, PARENT_COMPONENT_PROPERTY_NAME,
				oldParentComponent, getParentComponent());
	}

	public ComponentPanel getComponentPanel()
	{
		/*
		 * If this component has no componentPanel set and it does have a
		 * parentComponent, go up the hierarchy asking the parentComponents for
		 * their componentPanel reference.
		 */
		if (componentPanel == null && getParentComponent() != null)
			return getParentComponent().getComponentPanel();

		return componentPanel;
	}

	public void setComponentPanel(ComponentPanel componentPanel)
	{
		if (componentPanel != null && !isRootComponent())
			throw new RuntimeException(
					"componentPanel property can only be set on the root component");

		ComponentPanel oldComponentPanel = getComponentPanel();

		if (oldComponentPanel == componentPanel)
			return;

		this.componentPanel = componentPanel;
		firePropertyChangeEvent(this, COMPONENT_PANEL_PROPERTY_NAME,
				oldComponentPanel, getComponentPanel());

		/* Repaint the component onto the new panel */
		repaint();
	}

	public void revalidate()
	{
		invalidate();
		validate();
	}

	public void paintBorder(Graphics2D g2d)
	{
		ComponentBorder componentBorder = getComponentBorder();

		if (componentBorder != null)
			componentBorder.paintBorder(g2d, this);
	}

	public void paintComponent(Graphics2D g2d)
	{
		ComponentPainter componentPainter = getComponentPainter();

		if (componentPainter != null)
			componentPainter.paintComponent(g2d, this);
	}

	public void repaint()
	{
		repaint(getPreferredBounds());
	}

	public void repaint(double x, double y, double width, double height)
	{
		ContainerComponent parentComponent = getParentComponent();

		/*
		 * If there is no parent then this component will need to make the call
		 * to component panel with no translation of x/y coordinates to repaint
		 * (this is likely the rootComponent). Otherwise call up the stack of
		 * parent components translating the repaint coordinates as we go.
		 */
		if (parentComponent == null)
		{
			ComponentPanel componentPanel = getComponentPanel();

			/*
			 * Ignore the repaint request if we don't have access to the
			 * component panel or the width/height of the repaint bounds will be
			 * 0.
			 */
			if (componentPanel == null || width == 0 || height == 0)
				return;

			/*
			 * We use the floor/ceil here to make sure the rouding error from
			 * double to int doesn't loose important repaint bounds information.
			 */
			componentPanel.repaint((int) Math.floor(x), (int) Math.floor(y),
					(int) Math.ceil(width), (int) Math.ceil(height));
		}
		else
		{
			/*
			 * Translate the paint coordinates to the parent's coordinate space
			 * and ask it to repaint.
			 */
			parentComponent.repaint(parentComponent.getX() + x, parentComponent
					.getY()
					+ y, Math.min(parentComponent.getWidth(), width), Math.min(
					parentComponent.getHeight(), height));
		}
	}

	public void repaint(Rectangle2D repaintBounds)
	{
		repaint(repaintBounds.getX(), repaintBounds.getY(), repaintBounds
				.getWidth(), repaintBounds.getHeight());
	}

	public boolean isKeyNotificationEnabled()
	{
		return keyNotificationEnabled;
	}

	public void setKeyNotificationEnabled(boolean keyNotificationEnabled)
	{
		boolean oldKeyNotificationEnabled = isKeyNotificationEnabled();
		
		if (oldKeyNotificationEnabled == keyNotificationEnabled)
			return;

		this.keyNotificationEnabled = keyNotificationEnabled;
		firePropertyChangeEvent(this, KEY_NOTIFICATION_ENABLED_PROPERTY_NAME,
				oldKeyNotificationEnabled, isKeyNotificationEnabled());
	}

	public boolean containsKeyListener(KeyListener keyListener)
	{
		return keyListenerList.contains(keyListener);
	}

	public void addKeyListener(KeyListener keyListener)
	{
		addKeyListener(getKeyListenerCount(), keyListener);
	}

	public void addKeyListener(int index, KeyListener keyListener)
	{
		if (keyListener == null)
			return;

		if (!containsKeyListener(keyListener))
			keyListenerList.add(index, keyListener);
	}

	public int getKeyListenerCount()
	{
		return keyListenerList.size();
	}

	public int getIndexOfKeyListener(KeyListener keyListener)
	{
		return keyListenerList.indexOf(keyListener);
	}

	public KeyListener getKeyListener(int index)
	{
		return keyListenerList.get(index);
	}

	public KeyListener[] getKeyListeners()
	{
		return keyListenerList.toArray(new KeyListener[getKeyListenerCount()]);
	}

	public void removeKeyListener(int index)
	{
		keyListenerList.remove(index);
	}

	public void removeKeyListener(KeyListener keyListener)
	{
		if (keyListener == null)
			return;

		int index = getIndexOfKeyListener(keyListener);
		
		if(index > -1)
			removeKeyListener(index);
	}

	public void removeKeyListeners()
	{
		keyListenerList.clear();
	}

	public boolean isMouseNotificationEnabled()
	{
		return mouseNotificationEnabled;
	}

	public void setMouseNotificationEnabled(boolean mouseNotificationEnabled)
	{
		boolean oldMouseNotificationEnabled = isMouseNotificationEnabled();
		
		if (oldMouseNotificationEnabled == mouseNotificationEnabled)
			return;

		this.mouseNotificationEnabled = mouseNotificationEnabled;
		firePropertyChangeEvent(this, MOUSE_NOTIFICATION_ENABLED_PROPERTY_NAME,
				oldMouseNotificationEnabled, isMouseNotificationEnabled());
	}

	public boolean containsMouseListener(MouseListener mouseListener)
	{
		return mouseListenerList.contains(mouseListener);
	}

	public void addMouseListener(MouseListener mouseListener)
	{
		addMouseListener(getMouseListenerCount(), mouseListener);
	}

	public void addMouseListener(int index, MouseListener mouseListener)
	{
		if (mouseListener == null)
			return;

		if (!containsMouseListener(mouseListener))
			mouseListenerList.add(index, mouseListener);
	}

	public int getMouseListenerCount()
	{
		return mouseListenerList.size();
	}

	public int getIndexOfMouseListener(MouseListener mouseListener)
	{
		return mouseListenerList.indexOf(mouseListener);
	}

	public MouseListener getMouseListener(int index)
	{
		return mouseListenerList.get(index);
	}

	public MouseListener[] getMouseListeners()
	{
		return mouseListenerList
				.toArray(new MouseListener[getMouseListenerCount()]);
	}

	public void removeMouseListener(int index)
	{
		mouseListenerList.remove(index);
	}

	public void removeMouseListener(MouseListener mouseListener)
	{
		if (mouseListener == null)
			return;

		int index = getIndexOfMouseListener(mouseListener);
		
		if(index > -1)
			removeMouseListener(index);
	}

	public void removeMouseListeners()
	{
		mouseListenerList.clear();
	}

	public boolean isMouseMotionNotificationEnabled()
	{
		return mouseMotionNotificationEnabled;
	}

	public void setMouseMotionNotificationEnabled(
			boolean mouseMotionNotificationEnabled)
	{
		boolean oldMouseMotionNotificationEnabled = isMouseMotionNotificationEnabled();
		
		if (oldMouseMotionNotificationEnabled == mouseMotionNotificationEnabled)
			return;

		this.mouseMotionNotificationEnabled = mouseMotionNotificationEnabled;
		firePropertyChangeEvent(this,
				MOUSE_MOTION_NOTIFICATION_ENABLED_PROPERTY_NAME, oldMouseMotionNotificationEnabled,
				isMouseMotionNotificationEnabled());
	}

	public boolean containsMouseMotionListener(
			MouseMotionListener mouseMotionListener)
	{
		return mouseMotionListenerList.contains(mouseMotionListener);
	}

	public void addMouseMotionListener(MouseMotionListener mouseMotionListener)
	{
		addMouseMotionListener(getMouseMotionListenerCount(),
				mouseMotionListener);
	}

	public void addMouseMotionListener(int index,
			MouseMotionListener mouseMotionListener)
	{
		if (mouseMotionListener == null)
			return;

		if (!containsMouseMotionListener(mouseMotionListener))
			mouseMotionListenerList.add(index, mouseMotionListener);
	}

	public int getMouseMotionListenerCount()
	{
		return mouseMotionListenerList.size();
	}

	public int getIndexOfMouseMotionListener(
			MouseMotionListener mouseMotionListener)
	{
		return mouseMotionListenerList.indexOf(mouseMotionListener);
	}

	public MouseMotionListener getMouseMotionListener(int index)
	{
		return mouseMotionListenerList.get(index);
	}

	public MouseMotionListener[] getMouseMotionListeners()
	{
		return mouseMotionListenerList
				.toArray(new MouseMotionListener[getMouseMotionListenerCount()]);
	}

	public void removeMouseMotionListener(int index)
	{
		mouseMotionListenerList.remove(index);
	}

	public void removeMouseMotionListener(
			MouseMotionListener mouseMotionListener)
	{
		if (mouseMotionListener == null)
			return;

		int index = getIndexOfMouseMotionListener(mouseMotionListener);
		
		if(index > -1)
			removeMouseMotionListener(index);
	}

	public void removeMouseMotionListeners()
	{
		mouseMotionListenerList.clear();
	}

	public boolean isMouseWheelNotificationEnabled()
	{
		return mouseWheelNotificationEnabled;
	}

	public void setMouseWheelNotificationEnabled(
			boolean mouseWheelNotificationEnabled)
	{
		boolean oldMouseWheelNotificationEnabled = isMouseWheelNotificationEnabled();
		
		if (oldMouseWheelNotificationEnabled == mouseWheelNotificationEnabled)
			return;

		this.mouseWheelNotificationEnabled = mouseWheelNotificationEnabled;
		firePropertyChangeEvent(this,
				MOUSE_WHEEL_NOTIFICATION_ENABLED_PROPERTY_NAME, oldMouseWheelNotificationEnabled,
				isMouseWheelNotificationEnabled());
	}

	public boolean containsMouseWheelListener(
			MouseWheelListener mouseWheelListener)
	{
		return mouseWheelListenerList.contains(mouseWheelListener);
	}

	public void addMouseWheelListener(MouseWheelListener mouseWheelListener)
	{
		addMouseWheelListener(getMouseWheelListenerCount(), mouseWheelListener);
	}

	public void addMouseWheelListener(int index,
			MouseWheelListener mouseWheelListener)
	{
		if (mouseWheelListener == null)
			return;

		if (!containsMouseWheelListener(mouseWheelListener))
			mouseWheelListenerList.add(index, mouseWheelListener);
	}

	public int getMouseWheelListenerCount()
	{
		return mouseWheelListenerList.size();
	}

	public int getIndexOfMouseWheelListener(
			MouseWheelListener mouseWheelListener)
	{
		return mouseWheelListenerList.indexOf(mouseWheelListener);
	}

	public MouseWheelListener getMouseWheelListener(int index)
	{
		return mouseWheelListenerList.get(index);
	}

	public MouseWheelListener[] getMouseWheelListeners()
	{
		return mouseWheelListenerList
				.toArray(new MouseWheelListener[getMouseWheelListenerCount()]);
	}

	public void removeMouseWheelListener(int index)
	{
		mouseWheelListenerList.remove(index);
	}

	public void removeMouseWheelListener(MouseWheelListener mouseWheelListener)
	{
		if (mouseWheelListener == null)
			return;

		int index = getIndexOfMouseWheelListener(mouseWheelListener);
		
		if(index > -1)
			removeMouseWheelListener(index);
	}

	public void removeMouseWheelListeners()
	{
		mouseWheelListenerList.clear();
	}

	public boolean isValidationNotificationEnabled()
	{
		return validationNotificationEnabled;
	}

	public void setValidationNotificationEnabled(
			boolean validationNotificationEnabled)
	{
		boolean oldValidationNotificationEnabled = isValidationNotificationEnabled();
		
		if (oldValidationNotificationEnabled == validationNotificationEnabled)
			return;

		this.validationNotificationEnabled = validationNotificationEnabled;
		firePropertyChangeEvent(this,
				VALIDATION_NOTIFICATION_ENABLED_PROPERTY_NAME, oldValidationNotificationEnabled,
				isValidationNotificationEnabled());
	}

	public boolean containsValidationListener(
			ValidationListener validationListener)
	{
		return validationListenerList.contains(validationListener);
	}

	public void addValidationListener(ValidationListener validationListener)
	{
		addValidationListener(getValidationListenerCount(), validationListener);
	}

	public void addValidationListener(int index,
			ValidationListener validationListener)
	{
		if (validationListener == null)
			return;

		if (!containsValidationListener(validationListener))
			validationListenerList.add(index, validationListener);
	}

	public int getValidationListenerCount()
	{
		return validationListenerList.size();
	}

	public int getIndexOfValidationListener(
			ValidationListener validationListener)
	{
		return validationListenerList.indexOf(validationListener);
	}

	public ValidationListener getValidationListener(int index)
	{
		return validationListenerList.get(index);
	}

	public ValidationListener[] getValidationListeners()
	{
		return validationListenerList
				.toArray(new ValidationListener[getValidationListenerCount()]);
	}

	public void removeValidationListener(int index)
	{
		validationListenerList.remove(index);
	}

	public void removeValidationListener(ValidationListener validationListener)
	{
		if (validationListener == null)
			return;

		int index = getIndexOfValidationListener(validationListener);
		
		if(index > -1)
			removeValidationListener(index);
	}

	public void removeValidationListeners()
	{
		validationListenerList.clear();
	}

	// public VolatileImage renderOffscreenBuffer(Graphics2D g2d, VolatileImage
	// offscreenBuffer)
	// {
	// int attempts = 0;
	// VolatileImage currentOffscreenBuffer = null;
	// GraphicsConfiguration graphicsConfiguration =
	// g2d.getDeviceConfiguration();
	//		
	// do
	// {
	// /* If the offscreen buffer passed in is null or incompatible with this
	// * graphicsConfiguration, then that means we need to recreate it,
	// otherwise
	// * just use it as he current buffer.
	// */
	// if(offscreenBuffer == null ||
	// offscreenBuffer.validate(graphicsConfiguration) ==
	// VolatileImage.IMAGE_INCOMPATIBLE)
	// {
	// Rectangle2D preferredBounds = getPreferredBounds();
	// int width = (int)Math.ceil(preferredBounds.getWidth());
	// int height = (int)Math.ceil(preferredBounds.getHeight());
	// currentOffscreenBuffer =
	// graphicsConfiguration.createCompatibleVolatileImage(width, height);
	// offscreenBuffer = currentOffscreenBuffer;
	// }
	// else
	// currentOffscreenBuffer = offscreenBuffer;
	//			
	// /* Now that we have the buffer squared away, render to it */
	// /* Double buffering wasn't enabled, so render the old fashion way */
	// Graphics2D scratchG2D = currentOffscreenBuffer.createGraphics();
	// paintComponent(scratchG2D);
	// scratchG2D.dispose();
	//	
	// scratchG2D = currentOffscreenBuffer.createGraphics();
	// paintBorder(scratchG2D);
	// scratchG2D.dispose();
	//			
	// /* If the contents were lost for some reason start the whole
	// * process over, but don't try more than 3 times before giving
	// * up.
	// */
	// attempts++;
	// } while(attempts < 3 && currentOffscreenBuffer.contentsLost());
	//		
	// /* Incase the caller needs a reference to the buffer that was created
	// * and rendered to, return it.
	// */
	// return currentOffscreenBuffer;
	// }

	/**
	 * Helper method used to create a <code>MouseEvent</code> that represents
	 * a <code>MouseEvent.MOUSE_ENTERED</code> state from an existing
	 * <code>MouseEvent</code> instance. This is used by the
	 * <code>processMouseXXX</code> methods to simulate the cursor entering
	 * the component's bounds.
	 * 
	 * @param evt
	 *            The existing <code>MouseEvent</code> that will be used to
	 *            create the new <code>MouseEvent</code> instance.
	 * @return a <code>MouseEvent</code> that represents a
	 *         <code>MouseEvent.MOUSE_ENTERED</code> state.
	 */
	protected MouseEvent createMouseEnteredEvent(MouseEvent evt)
	{
		return new MouseEvent((JComponent) evt.getSource(),
				MouseEvent.MOUSE_ENTERED, evt.getWhen(), evt.getModifiers(),
				evt.getX(), evt.getY(), evt.getClickCount(), evt
						.isPopupTrigger(), evt.getButton());
	}

	/**
	 * Helper method used to create a <code>MouseEvent</code> that represents
	 * a <code>MouseEvent.MOUSE_EXITED</code> state from an existing
	 * <code>MouseEvent</code> instance. This is used by the
	 * <code>processMouseXXX</code> methods to simulate the cursor exiting the
	 * component's bounds.
	 * 
	 * @param evt
	 *            The existing <code>MouseEvent</code> that will be used to
	 *            create the new <code>MouseEvent</code> instance.
	 * @return a <code>MouseEvent</code> that represents a
	 *         <code>MouseEvent.MOUSE_EXITED</code> state.
	 */
	protected MouseEvent createMouseExitedEvent(MouseEvent evt)
	{
		return new MouseEvent((JComponent) evt.getSource(),
				MouseEvent.MOUSE_EXITED, evt.getWhen(), evt.getModifiers(), evt
						.getX(), evt.getY(), evt.getClickCount(), evt
						.isPopupTrigger(), evt.getButton());
	}

	protected void fireKeyEvent(KeyEvent evt)
	{
		/* Ignore the event if any of the criteria below are true */
		if (evt == null || !isKeyNotificationEnabled() || evt.isConsumed())
			return;

		/* Deliver the event to each listener as long as it isn't consumed */
		for (int i = 0, size = getKeyListenerCount(); !evt.isConsumed()
				&& i < size; i++)
		{
			KeyListener keyListener = getKeyListener(i);

			switch (evt.getID())
			{
				case KeyEvent.KEY_PRESSED:
					keyListener.keyPressed(evt);
					break;

				case KeyEvent.KEY_RELEASED:
					keyListener.keyReleased(evt);
					break;

				case KeyEvent.KEY_TYPED:
					keyListener.keyTyped(evt);
					break;
			}
		}
	}

	protected void fireMouseEvent(MouseEvent evt)
	{
		/* Ignore the event if any of the criteria below are true */
		if (evt == null || !isMouseNotificationEnabled() || evt.isConsumed())
			return;

		/* Deliver the event to each listener as long as it isn't consumed */
		for (int i = 0, size = getMouseListenerCount(); !evt.isConsumed()
				&& i < size; i++)
		{
			MouseListener mouseListener = getMouseListener(i);

			switch (evt.getID())
			{
				case MouseEvent.MOUSE_ENTERED:
					mouseListener.mouseEntered(evt);
					break;

				case MouseEvent.MOUSE_EXITED:
					mouseListener.mouseExited(evt);
					break;

				case MouseEvent.MOUSE_PRESSED:
					mouseListener.mousePressed(evt);
					break;

				case MouseEvent.MOUSE_RELEASED:
					mouseListener.mouseReleased(evt);
					break;

				case MouseEvent.MOUSE_CLICKED:
					mouseListener.mouseClicked(evt);
					break;
			}
		}
	}

	protected void fireMouseMotionEvent(MouseEvent evt)
	{
		/* Ignore the event if any of the criteria below are true */
		if (evt == null || !isMouseMotionNotificationEnabled()
				|| evt.isConsumed())
			return;

		/* Deliver the event to each listener as long as it isn't consumed */
		for (int i = 0, size = getMouseMotionListenerCount(); !evt.isConsumed()
				&& i < size; i++)
		{
			MouseMotionListener mouseMotionListener = getMouseMotionListener(i);

			switch (evt.getID())
			{
				case MouseEvent.MOUSE_MOVED:
					mouseMotionListener.mouseMoved(evt);
					break;

				case MouseEvent.MOUSE_DRAGGED:
					mouseMotionListener.mouseDragged(evt);
					break;
			}
		}
	}

	protected void fireMouseWheelEvent(MouseWheelEvent evt)
	{
		/* Ignore the event if any of the criteria below are true */
		if (evt == null || !isMouseWheelNotificationEnabled()
				|| evt.isConsumed())
			return;

		/* Deliver the event to each listener as long as it isn't consumed */
		for (int i = 0, size = getMouseWheelListenerCount(); !evt.isConsumed()
				&& i < size; i++)
			getMouseWheelListener(i).mouseWheelMoved(evt);
	}

	protected void fireValidationEvent(ValidationEvent evt)
	{
		if (evt == null || !isValidationNotificationEnabled())
			return;

		for (int i = 0, size = getValidationListenerCount(); i < size; i++)
		{
			ValidationListener validationListener = getValidationListener(i);

			switch (evt.getEventType())
			{
				case VALIDATED:
					validationListener.validated(evt);
					break;

				case INVALIDATED:
					validationListener.invalidated(evt);
					break;
			}
		}
	}
}