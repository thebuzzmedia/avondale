package com.kallasoft.avondale.component;

import com.kallasoft.avondale.component.model.ComponentModel;

public abstract class AbstractDraggableComponent extends
		DefaultContainerComponent implements DraggableComponent
{
	private boolean draggable = true;
	private boolean mouseDragging = false;
	private boolean realTimeDraggingEnabled = false;
	private ResizableComponent dragShadowComponent;

	public AbstractDraggableComponent(ComponentModel componentModel,
			ContainerComponent parentComponent)
	{
		super(componentModel, parentComponent);
	}

	public boolean isDraggable()
	{
		return draggable;
	}

	public void setDraggable(boolean draggable)
	{
		boolean oldDraggable = isDraggable();
		
		if(oldDraggable == draggable)
			return;
		
		/* Update the mouseDragging state if this is no longer a draggable component 
		 * and it is currently dragging. */
		if(!isDraggable() && isMouseDragging())
			setMouseDragging(false);
		
		this.draggable = draggable;
		firePropertyChangeEvent(this, DRAGGABLE_PROPERTY_NAME, oldDraggable, isDraggable());
	}

	public boolean isMouseDragging()
	{
		return mouseDragging;
	}

	public void setMouseDragging(boolean mouseDragging)
	{
		if(mouseDragging && !isDraggable())
			throw new IllegalStateException("This component's mouseDragging state cannot be set while it's draggable property is set to false");
		
		boolean oldMouseDragging = isMouseDragging();

		if (oldMouseDragging == mouseDragging)
			return;

		this.mouseDragging = mouseDragging;
		firePropertyChangeEvent(this, MOUSE_DRAGGING_PROPERTY_NAME,
				oldMouseDragging, isMouseDragging());
	}

	public boolean isRealTimeDraggingEnabled()
	{
		return realTimeDraggingEnabled;
	}

	public void setRealTimeDraggingEnabled(boolean realTimeDraggingEnabled)
	{
		if (isMouseDragging())
			throw new IllegalStateException(
					"realTimeDraggingEnabled property cannot be set while this component has it's mouseDragging property set to true. This property can only be changed before or after a drag operation.");

		boolean oldRealTimeDraggingEnabled = isRealTimeDraggingEnabled();

		if (oldRealTimeDraggingEnabled == realTimeDraggingEnabled)
			return;

		this.realTimeDraggingEnabled = realTimeDraggingEnabled;
		firePropertyChangeEvent(this, REAL_TIME_DRAGGING_ENABLED_PROPERTY_NAME,
				oldRealTimeDraggingEnabled, isRealTimeDraggingEnabled());
	}

	public ResizableComponent getDragShadowComponent()
	{
		return dragShadowComponent;
	}

	public void setDragShadowComponent(ResizableComponent dragShadowComponent)
	{
		ResizableComponent oldDragShadowComponent = getDragShadowComponent();

		if (oldDragShadowComponent == dragShadowComponent)
			return;

		this.dragShadowComponent = dragShadowComponent;
		firePropertyChangeEvent(this, DRAG_SHADOW_COMPONENT_PROPERTY_NAME,
				oldDragShadowComponent, getDragShadowComponent());
	}

//	public double[] getMousePressedXYOffsets()
//	{
//		return mousePressedXYOffsets;
//	}
//
//	public void setMousePressedXYOffsets(double xOffset, double yOffset)
//	{
//		mousePressedXYOffsets[MOUSE_X_OFFSET_INDEX] = xOffset;
//		mousePressedXYOffsets[MOUSE_Y_OFFSET_INDEX] = yOffset;
//	}
}