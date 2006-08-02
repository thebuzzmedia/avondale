package com.kallasoft.avondale.component;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import com.kallasoft.avondale.component.border.LineComponentBorder;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.panel.ComponentPanel;

public class DefaultDraggableComponent extends AbstractDraggableComponent
{
	private boolean snapToGridEnabled = false;
	private double horizontalGridSpacing = 0;
	private double verticalGridSpacing = 0;
	private double[] mousePressedXYOffsets;

	public DefaultDraggableComponent()
	{
		this(null);
	}

	public DefaultDraggableComponent(ComponentModel componentModel)
	{
		this(componentModel, null);
	}

	public DefaultDraggableComponent(ComponentModel componentModel,
			ContainerComponent parentComponent)
	{
		super(componentModel, parentComponent);
		
		mousePressedXYOffsets = new double[2];

		/* Setup the default drag shadow component */
		setDragShadowComponent(new DragShadowComponent());

		/* Register the custom mouse listeners to handle state accounting */
		addMouseListener(new DraggableComponentStateMouseHandler());
		addMouseMotionListener(new DraggableComponentStateMouseMotionHandler());
	}

	/**
	 * A class used to provide the default implementation of the drag shadow
	 * component. In this default implementation the component uses a simple
	 * style of a 1 pixel black border with a semitransparent gray fill color
	 * while the component is being dragged.
	 * 
	 * @author Riyad Kalla
	 * @version 1.0
	 * @since 1.0
	 */
	protected class DragShadowComponent extends DefaultResizableComponent
	{
		public DragShadowComponent()
		{
			setComponentBorder(new LineComponentBorder(Color.BLACK, 1,
					LineComponentBorder.DASHED_LINE_STROKE));
			setBackgroundPaint(new Color(200, 200, 200, 125));
		}
	}

	/**
	 * A class used to provide a <code>MouseListener</code> implementation to
	 * maintain the proper component dragging state in addition to keeping the
	 * necessary accounting to perform the dragging operation up to date.
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
	private class DraggableComponentStateMouseHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent evt)
		{
			/* Record the X and Y offsets of the mouse from the components edge */
			mousePressedXYOffsets[0] = (evt.getX() - getX());
			mousePressedXYOffsets[1] = (evt.getY() - getY());

			/* Setup the snapToGrid state before the dragging begings */
			ComponentPanel componentPanel = getComponentPanel();

			if (componentPanel != null)
			{
				snapToGridEnabled = componentPanel.isSnapToGridEnabled();

				/*
				 * Remember the horz and vert spacing of the componentPanel to
				 * perform accurate snapTo behavior while dragging.
				 */
				if (snapToGridEnabled)
				{
					horizontalGridSpacing = componentPanel
							.getHorizontalGridSpacing();
					verticalGridSpacing = componentPanel
							.getVerticalGridSpacing();
				}

				/*
				 * Initialize the component that represents the drag shadow if
				 * realTimeDragging is not enabled
				 */
				if (!isRealTimeDraggingEnabled())
				{
					ResizableComponent dragShadowComponent = getDragShadowComponent();

					/*
					 * Update the size of the drag shadow so it's the same size
					 * as this component. Then place it into the
					 * DRAG_ROOT_COMPONENT of the containing ComponentPanel.
					 */
					dragShadowComponent.setBounds(getX(), getY(), getWidth(),
							getHeight());
					componentPanel.DRAG_ROOT_COMPONENT
							.addComponent(dragShadowComponent);
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent evt)
		{
			/*
			 * Update the mouseDragging state.
			 */
			setMouseDragging(false);

			/* Clear the snapTo properties */
			snapToGridEnabled = false;
			horizontalGridSpacing = 0;
			verticalGridSpacing = 0;

			/* Unset the X/Y offset values */
			mousePressedXYOffsets[0] = -1;
			mousePressedXYOffsets[1] = -1;

			/*
			 * If real time dragging is not enabled, then we need to remove the
			 * drag shadow component from the drag layer of the componentPanel.
			 */
			if (!isRealTimeDraggingEnabled())
			{
				ResizableComponent dragShadowComponent = getDragShadowComponent();

				/*
				 * If the drag component isn't null, we can simply ask it for
				 * it's parent, which is most likely the DRAG_ROOT_COMPONENT of
				 * the ComponentPanel, but incase it's not using
				 * getParentComponent is more accurate.
				 */
				if (dragShadowComponent != null)
				{
					ContainerComponent containerComponent = dragShadowComponent
							.getParentComponent();

					if (containerComponent != null)
						containerComponent.removeComponent(dragShadowComponent);

					/*
					 * Because the drag shadow component was the one being
					 * dragged, it represents the new location of the component,
					 * so use it to reposition this component.
					 */
					setLocation(dragShadowComponent.getX(), dragShadowComponent
							.getY());
				}
			}
		}
	}

	/**
	 * A class used to provide a <code>MouseMotionListener</code>
	 * implementation to maintain the proper component dragging state in
	 * addition to performing the drag operation on either the current component
	 * (if real time dragging is enabled) or the drag shadow.
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
	private class DraggableComponentStateMouseMotionHandler extends
			MouseMotionAdapter
	{
		@Override
		public void mouseDragged(MouseEvent evt)
		{
			/*
			 * If the mouseDragging state wasn't already set, update it now. We
			 * do the check here because drag events are delivered during the
			 * entire drag session and there is no need to call the setter 100s
			 * or 1000s of times with the same value of 'true'.
			 */
			if (!isMouseDragging())
				setMouseDragging(true);

			/*
			 * Calculate how much the mouse has moved since it was pressed down
			 * on the component and dragged.
			 */
			double x = evt.getX() - mousePressedXYOffsets[0];
			double y = evt.getY() - mousePressedXYOffsets[1];

			/* Honor snapToGrid effect */
			if (snapToGridEnabled)
			{
				x -= (x % verticalGridSpacing);
				y -= (y % horizontalGridSpacing);
			}

			/*
			 * Update the position of this component if real time dragging is
			 * enabled, otherwise update the position of the shadow component.
			 */
			if (isRealTimeDraggingEnabled())
				setLocation(x, y);
			else
			{
				ResizableComponent realTimeDraggingComponent = getDragShadowComponent();

				if (realTimeDraggingComponent != null)
					realTimeDraggingComponent.setLocation(x, y);
			}
		}
	}
}