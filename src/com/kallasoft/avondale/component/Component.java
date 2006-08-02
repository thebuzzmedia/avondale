package com.kallasoft.avondale.component;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
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
import java.beans.PropertyChangeListener;

import com.kallasoft.avondale.Spacer;
import com.kallasoft.avondale.component.border.ComponentBorder;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.ComponentPainter;
import com.kallasoft.avondale.event.ValidationListener;
import com.kallasoft.avondale.panel.ComponentPanel;
import com.kallasoft.avondale.tooltip.ToolTip;
import com.kallasoft.ext.bean.PropertyChangeSupport;

/**
 * Class used to define the base component in Avondale. Every component in
 * Avondale that has a visual representation will extend this base class that
 * represents the core features that every visual component should support,
 * wether it be a node, button, connection, etc.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public interface Component extends PropertyChangeListener,
		PropertyChangeSupport
{
	public static final String VIRTUAL_PROPERTY_NAME = "virtual";
	public static final String ENABLED_PROPERTY_NAME = "enabled";
	public static final String VISIBLE_PROPERTY_NAME = "visible";
	public static final String OPAQUE_PROPERTY_NAME = "opaque";

	public static final String FOCUSABLE_PROPERTY_NAME = "focusable";
	public static final String FOCUSED_PROPERTY_NAME = "focused";
	public static final String MOUSE_OVER_PROPERTY_NAME = "mouseOver";
	public static final String MOUSE_PRESSED_PROPERTY_NAME = "mousePressed";

	public static final String PADDING_PROPERTY_NAME = "padding";
	public static final String BACKGROUND_PAINT_PROPERTY_NAME = "backgroundPaint";
	public static final String FOREGROUND_PAINT_PROPERTY_NAME = "foregroundPaint";
	public static final String COMPONENT_BORDER_PROPERTY_NAME = "componentBorder";
	public static final String COMPONENT_PAINTER_PROPERTY_NAME = "componentPainter";
	public static final String CURSOR_PROPERTY_NAME = "cursor";
	public static final String LAYOUT_CONSTRAINTS_PROPERTY_NAME = "layoutConstraints";
	public static final String TOOL_TIP_PROPERTY_NAME = "toolTip";
	public static final String COMPONENT_MODEL_PROPERTY_NAME = "componentModel";
	public static final String PARENT_COMPONENT_PROPERTY_NAME = "parentComponent";
	public static final String COMPONENT_PANEL_PROPERTY_NAME = "componentPanel";

	public static final String KEY_NOTIFICATION_ENABLED_PROPERTY_NAME = "keyNotificationEnabled";
	public static final String MOUSE_NOTIFICATION_ENABLED_PROPERTY_NAME = "mouseNotificationEnabled";
	public static final String MOUSE_MOTION_NOTIFICATION_ENABLED_PROPERTY_NAME = "mouseMotionNotificationEnabled";
	public static final String MOUSE_WHEEL_NOTIFICATION_ENABLED_PROPERTY_NAME = "mouseWheelNotificationEnabled";
	public static final String VALIDATION_NOTIFICATION_ENABLED_PROPERTY_NAME = "validationNotificationEnabled";

	public boolean isVirtual();

	public void setVirtual(boolean virtual);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	public boolean isVisible();

	public void setVisible(boolean visible);

	public boolean isOpaque();

	public void setOpaque(boolean opaque);

	public boolean isFocusable();

	public void setFocusable(boolean focusable);

	public boolean isFocused();

	public void setFocused(boolean focused);

	public boolean isMouseOver();

	public void setMouseOver(boolean mouseOver);

	public boolean isMousePressed();

	public void setMousePressed(boolean mousePressed);

	public boolean contains(double x, double y);

	public boolean contains(Point2D location);

	public double getX();

	public double getCenterX();

	public double getY();

	public double getCenterY();

	public Point2D getLocation();

	public double getWidth();

	public double getHeight();

	public Dimension2D getSize();

	public Spacer getPadding();

	public void setPadding(Spacer padding);

	public Rectangle2D getBounds();

	public Rectangle2D getPreferredBounds();

//	public Rectangle2D getVisibleBounds();

	public Paint getBackgroundPaint();

	public void setBackgroundPaint(Paint backgroundPaint);

	public Paint getForegroundPaint();

	public void setForegroundPaint(Paint foregroundPaint);

	public ComponentBorder getComponentBorder();

	public void setComponentBorder(ComponentBorder border);

	public ComponentPainter getComponentPainter();

	public void setComponentPainter(ComponentPainter componentPainter);

	public Shape getComponentShape();

	public Cursor getCursor();

	public void setCursor(Cursor cursor);

	public Object getLayoutConstraints();

	public void setLayoutConstraints(Object layoutConstraints);

	public ToolTip getToolTip();

	public void setToolTip(ToolTip toolTip);

	public ComponentModel getComponentModel();

	public void setComponentModel(ComponentModel componentModel);

	public boolean isRootComponent();

	public ContainerComponent getRootComponent();

	public ContainerComponent getParentComponent();

	public void setParentComponent(ContainerComponent component);

	public ComponentPanel getComponentPanel();

	public void setComponentPanel(ComponentPanel componentPanel);

	public boolean isValid();

	public void validate();

	public void invalidate();

	public void revalidate();

	public void paint(Graphics2D g2d);

	public void paintBorder(Graphics2D g2d);

	public void paintComponent(Graphics2D g2d);

	public void repaint();

	public void repaint(double x, double y, double width, double height);

	public void repaint(Rectangle2D repaintBounds);

	public void processKeyEvent(KeyEvent evt);

	public void processMouseEvent(MouseEvent evt);

	public void processMouseMotionEvent(MouseEvent evt);

	public void processMouseWheelEvent(MouseWheelEvent evt);

	public boolean isKeyNotificationEnabled();

	public void setKeyNotificationEnabled(boolean keyNotificationEnabled);

	public boolean containsKeyListener(KeyListener keyListener);

	public void addKeyListener(KeyListener keyListener);

	public void addKeyListener(int index, KeyListener keyListener);

	public int getKeyListenerCount();

	public int getIndexOfKeyListener(KeyListener keyListener);

	public KeyListener getKeyListener(int index);

	public KeyListener[] getKeyListeners();

	public void removeKeyListener(int index);

	public void removeKeyListener(KeyListener keyListener);

	public void removeKeyListeners();

	public boolean isMouseNotificationEnabled();

	public void setMouseNotificationEnabled(boolean mouseNotificationEnabled);

	public boolean containsMouseListener(MouseListener mouseListener);

	public void addMouseListener(MouseListener mouseListener);

	public void addMouseListener(int index, MouseListener mouseListener);

	public int getMouseListenerCount();

	public int getIndexOfMouseListener(MouseListener mouseListener);

	public MouseListener getMouseListener(int index);

	public MouseListener[] getMouseListeners();

	public void removeMouseListener(int index);

	public void removeMouseListener(MouseListener mouseListener);

	public void removeMouseListeners();

	public boolean isMouseMotionNotificationEnabled();

	public void setMouseMotionNotificationEnabled(
			boolean mouseMotionNotificationEnabled);

	public boolean containsMouseMotionListener(
			MouseMotionListener mouseMotionListener);

	public void addMouseMotionListener(MouseMotionListener mouseMotionListener);

	public void addMouseMotionListener(int index,
			MouseMotionListener mouseMotionListener);

	public int getMouseMotionListenerCount();

	public int getIndexOfMouseMotionListener(
			MouseMotionListener mouseMotionListener);

	public MouseMotionListener getMouseMotionListener(int index);

	public MouseMotionListener[] getMouseMotionListeners();

	public void removeMouseMotionListener(int index);

	public void removeMouseMotionListener(
			MouseMotionListener mouseMotionListener);

	public void removeMouseMotionListeners();

	public boolean isMouseWheelNotificationEnabled();

	public void setMouseWheelNotificationEnabled(
			boolean mouseWheelNotificationEnabled);

	public boolean containsMouseWheelListener(
			MouseWheelListener mouseWheelListener);

	public void addMouseWheelListener(MouseWheelListener mouseWheelListener);

	public void addMouseWheelListener(int index,
			MouseWheelListener mouseWheelListener);

	public int getMouseWheelListenerCount();

	public int getIndexOfMouseWheelListener(
			MouseWheelListener mouseWheelListener);

	public MouseWheelListener getMouseWheelListener(int index);

	public MouseWheelListener[] getMouseWheelListeners();

	public void removeMouseWheelListener(int index);

	public void removeMouseWheelListener(MouseWheelListener mouseWheelListener);

	public void removeMouseWheelListeners();

	public boolean isValidationNotificationEnabled();

	public void setValidationNotificationEnabled(
			boolean ValidationNotificationEnabled);

	public boolean containsValidationListener(
			ValidationListener ValidationListener);

	public void addValidationListener(ValidationListener ValidationListener);

	public void addValidationListener(int index,
			ValidationListener ValidationListener);

	public int getValidationListenerCount();

	public int getIndexOfValidationListener(
			ValidationListener ValidationListener);

	public ValidationListener getValidationListener(int index);

	public ValidationListener[] getValidationListeners();

	public void removeValidationListener(int index);

	public void removeValidationListener(ValidationListener ValidationListener);

	public void removeValidationListeners();
}