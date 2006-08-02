package com.kallasoft.avondale.component;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.layout.ContainerLayout;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.component.painter.DefaultComponentPainter;
import com.kallasoft.avondale.component.util.ComponentUtils;
import com.kallasoft.avondale.panel.ComponentPanel;

public class DefaultContainerComponent extends AbstractContainerComponent
{
	public DefaultContainerComponent()
	{
		this(null);
	}

	public DefaultContainerComponent(ComponentModel componentModel)
	{
		this(componentModel, null);
	}
	
	public DefaultContainerComponent(ComponentModel componentModel, ContainerComponent parentComponent)
	{
		super(componentModel, parentComponent);
		setComponentPainter(DefaultComponentPainter.getInstance());
	}

	@Override
	public boolean isRootComponent()
	{
		boolean result = false;
		ComponentPanel componentPanel = getComponentPanel();

		if (componentPanel == null)
			result = (getParentComponent() == null);
		else
			result = componentPanel.containsRootComponent(this);

		return result;
	}

	@Override
	public void setParentComponent(ContainerComponent parentComponent)
	{
		if (getParentComponent() == parentComponent)
			return;

		if (ComponentUtils.isDescendantOfComponent(this, parentComponent))
			throw new IllegalArgumentException(
					"parentComponent is already a descendant of this component, it cannot be made a parent of this component");

		super.setParentComponent(parentComponent);
	}

	@Override
	public void validate()
	{
		/* If this component is already valid don't do anything */
		if (isValid())
			return;

		/* Layout the container to bring it's state back to valid */
		layoutContainer();
		super.validate();
	}

	@Override
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

		/* Delegate to the super paint implementation */
		super.paint(g2d);

		/* Now paint the children of this component */
		paintChildren(g2d);
	}

	@Override
	public void processMouseEvent(MouseEvent evt)
	{
		/*
		 * Ignore the event if it is null or consumed (we don't check for
		 * rootComponent here because then the rootComponent would through out
		 * all mouseEvents instead of delivering them down to it's children like
		 * it should)
		 */
		if (evt == null || evt.isConsumed())
			return;

		/*
		 * Perform secondary short-circuit checks if the event should even be
		 * bothered with. More specifically, in the case of EXITED or RELEASED
		 * events this component or one of it's children should receive it even
		 * though testing for contains(x,y) would fail. However in the case of
		 * ENTERED, PRESSED or CLICKED the test for contains(x,y) has to pass in
		 * order for those events to be valid for this component or any of its
		 * children. In that case, short circuit any ENTERED, PRESSED or CLICKED
		 * events that come in that don't pass the contains(x,y) test.
		 */
		if ((evt.getID() == MouseEvent.MOUSE_ENTERED
				|| evt.getID() == MouseEvent.MOUSE_PRESSED || evt.getID() == MouseEvent.MOUSE_CLICKED)
				&& !contains(evt.getX(), evt.getY()))
			return;

		/* Translate the event to the coordinate space of this parent component */
		evt.translatePoint(-(int) getX(), -(int) getY());

		/*
		 * Deliver the event to all the children components first to see if they
		 * can handle it, stopping if it is consumed. Deliver in reverse order
		 * to honor the component's z-ordering.
		 */
		for (int i = getComponentCount() - 1; !evt.isConsumed() && i > -1; i--)
			getComponent(i).processMouseEvent(evt);

		/* Restore the original coordinate space of the event */
		evt.translatePoint((int) getX(), (int) getY());

		/*
		 * If the event wasn't consumed by a child and this isn't the
		 * rootComponent, process it.
		 */
		if (!evt.isConsumed() && !isRootComponent())
			super.processMouseEvent(evt);
	}

	@Override
	public void processMouseMotionEvent(MouseEvent evt)
	{
		/* Ignore the event if it is null or consumed */
		if (evt == null || evt.isConsumed())
			return;

		/* Translate the event to the coordinate space of this parent component */
		evt.translatePoint(-(int) getX(), -(int) getY());

		/*
		 * Deliver the event to all the children components first to see if they
		 * can handle it, stopping if it is consumed. Deliver in reverse order
		 * to honor the component's z-ordering.
		 */
		for (int i = getComponentCount() - 1; !evt.isConsumed() && i > -1; i--)
			getComponent(i).processMouseMotionEvent(evt);

		/* Restore the original coordinate space of the event */
		evt.translatePoint((int) getX(), (int) getY());

		/*
		 * If the event wasn't consumed by a child and this isn't the
		 * rootComponent, process it
		 */
		if (!evt.isConsumed() && !isRootComponent())
			super.processMouseMotionEvent(evt);
	}

	@Override
	public void processMouseWheelEvent(MouseWheelEvent evt)
	{
		/* Ignore the event if it is null or consumed */
		if (evt == null || evt.isConsumed())
			return;

		/* Translate the event to the coordinate space of this parent component */
		evt.translatePoint(-(int) getX(), -(int) getY());

		/*
		 * Deliver the event to all the children components first to see if they
		 * can handle it, stopping if it is consumed. Deliver in reverse order
		 * to honor the component's z-ordering.
		 */
		for (int i = getComponentCount() - 1; !evt.isConsumed() && i > -1; i--)
			getComponent(i).processMouseWheelEvent(evt);

		/* Restore the original coordinate space of the event */
		evt.translatePoint((int) getX(), (int) getY());

		/*
		 * If the event wasn't consumed by a child and this isn't the
		 * rootComponent, process it
		 */
		if (!evt.isConsumed() && !isRootComponent())
			super.processMouseWheelEvent(evt);
	}

	public void layoutContainer()
	{
		/*
		 * Don't layout the container if layout is either disabled or the
		 * container is already in a valid state.
		 */
		if (!isContainerLayoutEnabled() || isValid())
			return;

		/*
		 * First try and layout all the children, if they are valid they will
		 * return immediately, but since they can effect the layout of this
		 * component laying the children out must be done first (e.g. children
		 * moving position or size can effect the size of this component).
		 */
		for (int i = 0, size = getComponentCount(); i < size; i++)
		{
			Component component = getComponent(i);

			if (component instanceof ContainerComponent)
				((ContainerComponent) component).layoutContainer();
		}

		ContainerLayout containerLayout = getContainerLayout();
		ContainerComponent parentComponent = getParentComponent();

		/*
		 * If the layout of this container changes then the parentComponent must
		 * be layed back out as the changes will likely effect it. This
		 * recursion will continue occuring on up the chain until the
		 * rootComponent is revalidated if necessary (the recursion stops as
		 * soon as a revalidated component is not changed by relaying itself
		 * out.
		 */
		if (containerLayout != null && containerLayout.layoutContainer(this)
				&& parentComponent != null)
			parentComponent.layoutContainer();

		/*
		 * If the code above has recursed to the point that we reached the
		 * rootComponent, then as a last step we need to revalidate the
		 * componentPanel as the layout a rootComponent can effect the
		 * componentPanel that contains it (e.g. If the rootComponent got big
		 * enough, the componentPanel would need to expand so scroll bars would
		 * get throw up by the JScrollPane that contained it).
		 */
		if (isRootComponent())
		{
			ComponentPanel componentPanel = getComponentPanel();

			if (componentPanel != null)
				componentPanel.revalidate();
		}
	}

	public void paintChildren(Graphics2D g2d)
	{
		Rectangle clipBounds = g2d.getClipBounds();
		Rectangle2D newClipBounds = getPreferredBounds();
		Rectangle2D.intersect(clipBounds, newClipBounds, newClipBounds);

		/*
		 * The clip is defined as the intersection between the existing clip and
		 * the bounds of the parent component, if this was not done, then it
		 * would be possible to resize a component to size 0 and still see it's
		 * children painted ontop of top level components
		 */
		g2d.setClip(newClipBounds);

		/*
		 * Translate the 0,0 point to the parent's x/y coordinate before
		 * painting the children. We do this here to put the coordinate space in
		 * conjunction with the coordinate space of the child components (where
		 * 0,0 is the top left of this (it's parent's component).
		 */
		g2d.translate(getX(), getY());

		for (int i = 0, size = getComponentCount(); i < size; i++)
		{
			Component component = getComponent(i);

			/*
			 * Translate the 0,0 point of the Graphics2D context to the X/Y
			 * coordinate of the component so it paints from it's top left
			 * corner which is 0,0.
			 */
			g2d.translate(component.getX(), component.getY());

			/* Paint the component where 0,0 is it's top left corner */
			component.paint(g2d);

			/*
			 * Return the translated Graphics2D coordinate space back to that of
			 * the parent component's
			 */
			g2d.translate(-component.getX(), -component.getY());
		}

		/* Restore the original 0,0 point and clipBounds */
		g2d.translate(-getX(), -getY());
		g2d.setClip(clipBounds);
	}
}