package com.kallasoft.avondale.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.Spacer;
import com.kallasoft.avondale.component.border.ComponentBorder;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.DefaultComponentPainter;
import com.kallasoft.avondale.event.DefaultValidationEvent;
import com.kallasoft.avondale.event.ValidationEvent;
import com.kallasoft.avondale.panel.ComponentPanel;
import com.kallasoft.avondale.panel.ComponentPanelState;
import com.kallasoft.avondale.tooltip.ToolTip;
import com.kallasoft.avondale.tooltip.ToolTipManager;

/**
 * Class used to provide a default implementation of the <code>Component</code>
 * interface. This class extends the <code>AbstractComponent</code>
 * implementation to provide more specific functionality pertaining to the
 * component's functionality. The <code>AbstractComponent</code> is intended
 * to provide an implementation of all the boiler-plate code (e.g. firing
 * <code>PropertyChangeEvent</code>s when a property changes), but this class
 * provides the more specific custom implementation details (e.g. in addition to
 * firing the <code>PropertyChangeEvent</code> after the property is changed,
 * update some associated <code>ComponentPanel</code> state). This separation
 * allows implementors wishing to change default behavior easier by reusing the
 * dummy abstract implementations and replacing the default implementations with
 * custom handling code without needing to reimplement all the boiler plate code
 * from scratch.
 * <p>
 * This class also contains the logic used to process mouse events. Hierarchical
 * processing that honors the component embeddedness is contained inside of the
 * <code>ContainerComponent</code> class implementation.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 * @see AbstractComponent
 * @see ContainerComponent
 */
public class DefaultComponent extends AbstractComponent
{
	private boolean valid = false;
	private Cursor oldCursor;

	public DefaultComponent()
	{
		this(null);
	}

	public DefaultComponent(ComponentModel componentModel)
	{
		super(componentModel);
		setComponentPainter(DefaultComponentPainter.getInstance());
		
		/* Register the custom mouse listener to handle state accounting */
		addMouseListener(new ComponentStateMouseHandler());
	}

	@Override
	public void setFocused(boolean focused)
	{
		if (!isFocusable() || (isFocused() == focused))
			return;

		super.setFocused(focused);

		/* Update the ComponentPanelState */
		ComponentPanel componentPanel = getComponentPanel();

		if (componentPanel != null)
		{
			ComponentPanelState componentPanelState = componentPanel
					.getComponentPanelState();

			/* First unfocus the currently focused component if there is one */
			Component currentFocusedComponent = componentPanelState
					.getFocusedComponent();

			if (currentFocusedComponent != null)
				currentFocusedComponent.setFocused(false);

			/* Now set this component as the focused component */
			componentPanelState
					.setFocusedComponent((isFocused() ? this : null));
		}
	}

	@Override
	public void setMouseOver(boolean mouseOver)
	{
		if (isMouseOver() == mouseOver)
			return;

		super.setMouseOver(mouseOver);

		/* Update the ComponentPanelState */
		ComponentPanel componentPanel = getComponentPanel();

		if (componentPanel != null)
		{
			ComponentPanelState componentPanelState = componentPanel
					.getComponentPanelState();

			/*
			 * First unset the moused over state for the current moused over
			 * component if there is one
			 */
			Component currentMouseOverComponent = componentPanelState
					.getFocusedComponent();

			if (currentMouseOverComponent != null)
				currentMouseOverComponent.setMouseOver(false);

			componentPanelState.setMousedOverComponent((isMouseOver() ? this
					: null));

			/* Update the componentPanel's cursor */
			if (isMouseOver())
			{
				Cursor cursor = getCursor();

				if (cursor != null)
				{
					oldCursor = componentPanel.getCursor();
					componentPanel.setCursor(cursor);
				}
			}
			else
			{
				componentPanel.setCursor(oldCursor);
				oldCursor = null;
			}
		}
	}

	@Override
	public void setMousePressed(boolean mousePressed)
	{
		if (isMousePressed() == mousePressed)
			return;

		super.setMousePressed(mousePressed);

		/* Update the ComponentPanelState */
		ComponentPanel componentPanel = getComponentPanel();

		if (componentPanel != null)
		{
			ComponentPanelState componentPanelState = componentPanel
					.getComponentPanelState();

			/* First unset the existing mousePressed component if there is one */
			Component currentMousePressedComponent = componentPanelState
					.getMousePressedComponent();

			if (currentMousePressedComponent != null)
				currentMousePressedComponent.setMousePressed(false);

			componentPanelState
					.setMousePressedComponent((isMousePressed() ? this : null));
		}
	}

	@Override
	public void setCursor(Cursor cursor)
	{
		Cursor oldCursor = getCursor();

		if (oldCursor == cursor)
			return;

		super.setCursor(cursor);

		/* Update the componentPanel's cursor if mouse is over this component */
		if (isMouseOver())
		{
			ComponentPanel componentPanel = getComponentPanel();

			if (componentPanel != null)
				componentPanel.setCursor(getCursor());
		}
	}

	public Rectangle2D getBounds()
	{
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}

	public Rectangle2D getPreferredBounds()
	{
		ComponentBorder border = getComponentBorder();
		Rectangle2D bounds = getBounds();

		/* Add the border width to the repaint bounds of this component */
		if (border != null)
		{
			double width = border.getBorderWidth(this);
			bounds.setRect(bounds.getX() - width, bounds.getY() - width, bounds
					.getWidth()
					+ (2 * width), bounds.getHeight() + (2 * width));
		}

		return bounds;
	}

	// /* TODO: Name this method better or move it to a utility class, because
	// it
	// * sounds like it pertains to this component, but it actually provides the
	// * bounds relative to the root component of how much this component is
	// visible
	// * and that is not clear from the name or functioanlity from the rest of
	// the methods */
	// public Rectangle2D getVisibleBounds()
	// {
	// double xOffset = 0;
	// double yOffset = 0;
	// Rectangle2D bounds = getPreferredBounds();
	// ContainerComponent parentComponent = getParentComponent();
	//
	// while (parentComponent != null)
	// {
	// /* Increment the offsets */
	// xOffset += parentComponent.getX();
	// yOffset += parentComponent.getY();
	//
	// /* Adjust bounds to the parent's coordinate space */
	// bounds.setRect(bounds.getX() + parentComponent.getX(), bounds
	// .getY()
	// + parentComponent.getY(), bounds.getWidth(), bounds
	// .getHeight());
	//
	// /* Intersect the current bounds with the parent bounds */
	// Rectangle2D parentBounds = parentComponent.getPreferredBounds();
	// Rectangle2D.intersect(bounds, parentBounds, bounds);
	//
	// /* Process the next parent in the hierarchy */
	// parentComponent = parentComponent.getParentComponent();
	// }
	//
	// /* Return the bounds to the coordinate space of this component */
	// bounds.setRect(bounds.getX() - xOffset, bounds.getY() - yOffset, bounds
	// .getWidth(), bounds.getHeight());
	// return bounds;
	// }

	public Shape getComponentShape()
	{
		/*
		 * X and Y are 0,0 because components are painted from their top left
		 * corners being their 0,0 point.
		 */
		double x = 0;
		double y = 0;
		double width = getWidth();
		double height = getHeight();
		Spacer padding = getPadding();

		if (padding != null && !padding.isEmpty())
		{
			x += padding.getLeftSpace();
			y += padding.getTopSpace();
			width -= (padding.getLeftSpace() + padding.getRightSpace());
			height -= (padding.getTopSpace() + padding.getBottomSpace());
		}

		return new Rectangle2D.Double(x, y, width, height);
	}

	public boolean isValid()
	{
		return valid;
	}

	public void validate()
	{
		/* If this component is already valid don't do anything */
		if (isValid())
			return;

		/*
		 * We don't change the parent's state at all here as validating this
		 * component has no effect on the parent. The parent may have it's own
		 * algorithm to run.
		 */

		valid = true;
		fireValidationEvent(new DefaultValidationEvent(this,
				ValidationEvent.EventType.VALIDATED));
	}

	public void invalidate()
	{
		/* If this component is already invalid don't do anything */
		if (!isValid())
			return;

		ContainerComponent parentComponent = getParentComponent();

		/*
		 * When this child's state becomes invalid (most likely do to a position
		 * change) that change implicitly invalidates the layout of the parent
		 * component, so we must set that here.
		 */
		if (parentComponent != null)
			parentComponent.invalidate();

		valid = false;
		fireValidationEvent(new DefaultValidationEvent(this,
				ValidationEvent.EventType.INVALIDATED));
	}

	public void paint(Graphics2D g2d)
	{
		if (!isVisible())
			return;

		/*
		 * Make sure the component is in a valid state before calculating clip
		 * intersection.
		 */
		if (!isValid())
			validate();

		Rectangle2D clipBounds = g2d.getClipBounds();

		/*
		 * If the bounds of this component don't intersect the current clip then
		 * there is nothing to paint.
		 */
		if (!clipBounds.intersects(getPreferredBounds()))
			return;

		/* Paint the component */
		paintComponent(g2d);

		/* Paint the component border */
		paintBorder(g2d);

		if (ComponentPanel.DEBUG)
		{
			g2d.setColor(Color.RED);
			clipBounds.setRect(clipBounds.getX(), clipBounds.getY(), clipBounds
					.getWidth() - 1, clipBounds.getHeight() - 1);
			g2d.draw(clipBounds);
		}
	}

	/* TODO: Javadoc the logic behind these methods */
	public void processKeyEvent(KeyEvent evt)
	{
		/*
		 * Ignore the event if it is null, consumed, this component is not
		 * focused or occured over a rootComponent.
		 */
		if (evt == null || evt.isConsumed() || !isFocused()
				|| isRootComponent())
			return;

		/*
		 * Deliver the key event to this component.
		 */
		fireKeyEvent(evt);

		/* Consume the event */
		evt.consume();
	}

	public void processMouseEvent(MouseEvent evt)
	{
		/*
		 * Ignore the event if it is null, consumed or occured over a
		 * rootComponent
		 */
		if (evt == null || evt.isConsumed() || isRootComponent())
			return;

		/* Process the event based on it's type */
		switch (evt.getID())
		{
			case MouseEvent.MOUSE_ENTERED:
				/*
				 * Check if this component has already registered a mouse
				 * entered event (by checking mouseOver property) and ignore the
				 * event if it's not within the bounds of the component.
				 */
				if (isMouseOver() || !contains(evt.getX(), evt.getY()))
					return;

				fireMouseEvent(evt);

				/* Consume the event because it belonged to this component */
				evt.consume();
				break;

			case MouseEvent.MOUSE_EXITED:
				/*
				 * Check if this component has already registered a mouse exited
				 * event (by checking the !mouseOver property).
				 */
				if (!isMouseOver())
					return;

				fireMouseEvent(evt);

				/* Consume the event because it belonged to this component */
				evt.consume();
				break;

			case MouseEvent.MOUSE_PRESSED:
				/* Check that the event occured over this component */
				if (!contains(evt.getX(), evt.getY()))
					return;

				fireMouseEvent(evt);

				/* Consume the event because it belonged to this component */
				evt.consume();
				break;

			case MouseEvent.MOUSE_RELEASED:
				/*
				 * Check if the mouse was pressed ontop of this component to
				 * start with.
				 */
				if (!isMousePressed())
					return;

				fireMouseEvent(evt);

				/* Consume the event because it belonged to this component */
				evt.consume();
				break;

			case MouseEvent.MOUSE_CLICKED:
				/* Check that the event occured over this component */
				if (!contains(evt.getX(), evt.getY()))
					return;

				fireMouseEvent(evt);

				/* Consume the event because it belonged to this component */
				evt.consume();
				break;
		}
	}

	public void processMouseMotionEvent(MouseEvent evt)
	{
		/*
		 * Ignore the event if it is null, consumed or occured over a
		 * rootComponent
		 */
		if (evt == null || evt.isConsumed() || isRootComponent())
			return;

		switch (evt.getID())
		{
			case MouseEvent.MOUSE_MOVED:
				/*
				 * If this component contains the mouseMoveEvent, then that
				 * means this event has the potential of being a mouseEntered
				 * event for this component as well as a mouseExited event for
				 * the current moused over component as well. If the mouse over
				 * state for this component is already set, then this becomes a
				 * simple mouseMovedEvent.
				 */
				if (contains(evt.getX(), evt.getY()))
				{
					/*
					 * If the mouse is already over this component, then simply
					 * deliver the event as a mouseMoved event. If the mouse was
					 * not already over this component, that means we must
					 * generate a mouseEnteredEvent.
					 */
					if (isMouseOver())
						fireMouseMotionEvent(evt);
					else
					{
						ComponentPanel componentPanel = getComponentPanel();

						if (componentPanel != null)
						{
							Component component = componentPanel
									.getComponentPanelState()
									.getMousedOverComponent();

							/*
							 * Before firing a mouseEntered event to this
							 * component, fire a mouseExited event to the
							 * previous component that had the mousedOver state
							 * set. We can retreive that information from the
							 * ComponentPanelState instance.
							 */
							if (component != null)
								component
										.processMouseEvent(createMouseExitedEvent(evt));
						}

						/* Now notify this component of the updated mouse state */
						processMouseEvent(createMouseEnteredEvent(evt));
					}

					/* Consume the event because it belonged to this component */
					evt.consume();
				}
				/*
				 * If this component does not contain the mouseOverEvent, but it
				 * currently has it's mouseOver state set, then we need to fire
				 * a mouseExitedEvent to this component to clear it.
				 */
				else if (isMouseOver())
				{
					processMouseEvent(createMouseExitedEvent(evt));

					/* Consume the event because it belonged to this component */
					evt.consume();
				}
				break;

			case MouseEvent.MOUSE_DRAGGED:
				/*
				 * Only consider mouseDragged events for this component if the
				 * mousePressed state is set. No bounds checking is done because
				 * it is possible on slower machines (or with intense graphics)
				 * to drag the mouse faster than the component can repaint,
				 * effectively dragging it outside the bounds of the component,
				 * we wouldn't want to cancel a drag in that case until the
				 * person let go of the mouse button.
				 */
				if (isMousePressed())
				{
					/* Deliver the mouseDraggedEvent to the component. */
					fireMouseMotionEvent(evt);

					/* Consume the event because it belonged to this component */
					evt.consume();
				}
				break;
		}
	}

	public void processMouseWheelEvent(MouseWheelEvent evt)
	{
		/*
		 * Ignore the event if it is null, consumed or occured over a
		 * rootComponent
		 */
		if (evt == null || evt.isConsumed() || isRootComponent())
			return;

		/*
		 * Deliver the wheel event to this component as long as it occured
		 * within its bounds.
		 */
		if (contains(evt.getX(), evt.getY()))
		{
			fireMouseWheelEvent(evt);

			/* Consume the event because it belonged to this component */
			evt.consume();
		}
	}

	/**
	 * A class used to provide a <code>MouseListener</code> implementation to
	 * maintain the proper <code>ComponentPanel</code> state depending on the
	 * state of the mouse (mouse over, mouse pressed, etc.).
	 * <p>
	 * This listener is a non-consuming implementation, meaning it processes the
	 * <code>MouseEvent</code>s in order to know what is going on, but does
	 * not consume them so they can continue to be passed along to other
	 * listeners that may be registered.
	 * 
	 * @author Riyad Kalla
	 * @version 1.0
	 * @since 1.0
	 */
	private class ComponentStateMouseHandler implements MouseListener
	{
		public void mouseEntered(MouseEvent evt)
		{
			/* Update the mouseOver state */
			setMouseOver(true);

			/* Active the tooltip if we can */
			ToolTip toolTip = getToolTip();

			/*
			 * Trigger the toolTipManager if there is a tooltip for this
			 * component and if it is available
			 */
			if (toolTip != null)
			{
				ComponentPanel componentPanel = getComponentPanel();

				if (componentPanel != null)
				{
					ToolTipManager toolTipManager = componentPanel
							.getToolTipManager();

					if (toolTipManager != null)
						toolTipManager.showToolTip(toolTip);
				}
			}
		}

		public void mouseExited(MouseEvent evt)
		{
			/* Update the mouseOver state */
			setMouseOver(false);

			/*
			 * Tell the toolTipManager to stop showing the toolTip if one was
			 * available.
			 */
			ComponentPanel componentPanel = getComponentPanel();

			if (componentPanel != null)
			{
				ToolTipManager toolTipManager = componentPanel
						.getToolTipManager();

				if (toolTipManager != null)
					toolTipManager.hideToolTip(getToolTip());
			}
		}

		public void mousePressed(MouseEvent evt)
		{
			/* Update the mousePressed state */
			setMousePressed(true);
		}

		public void mouseReleased(MouseEvent evt)
		{
			/*
			 * Update the mousePressed state. */
			setMousePressed(false);

			/*
			 * If the mouse was not released over the component it was clicked
			 * on, we need to clear that state as well. This can happen if the
			 * user clicks over a component that either can't move or moves
			 * slowly then releases the mouse somewhere else outside of the
			 * component's bounds. We don't want the component to still show a
			 * mouseOver state in that case so we check for that and clear it
			 * here.
			 */
			if (!contains(evt.getX(), evt.getY()) && isMouseOver())
				setMouseOver(false);
		}

		public void mouseClicked(MouseEvent evt)
		{
			/* Update the focused state */
			setFocused(true);
		}
	}
}