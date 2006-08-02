package com.kallasoft.avondale.component;

public interface DraggableComponent extends ContainerComponent
{
//	public static final int MOUSE_X_OFFSET_INDEX = 0;
//	public static final int MOUSE_Y_OFFSET_INDEX = 1;
//	public static final double UNSET_OFFSET_VALUE = -1;
	
	public static final String DRAGGABLE_PROPERTY_NAME = "draggable";
	public static final String MOUSE_DRAGGING_PROPERTY_NAME = "mouseDragging";
	public static final String REAL_TIME_DRAGGING_ENABLED_PROPERTY_NAME = "realTimeDraggingEnabled";
	public static final String DRAG_SHADOW_COMPONENT_PROPERTY_NAME = "dragShadowComponent";
	
	public boolean isDraggable();
	
	public void setDraggable(boolean draggable);
	
	public boolean isMouseDragging();

	public void setMouseDragging(boolean mouseDragging);
	
	public boolean isRealTimeDraggingEnabled();

	public void setRealTimeDraggingEnabled(boolean realTimeDraggingEnabled);
	
	public ResizableComponent getDragShadowComponent();
	
	public void setDragShadowComponent(ResizableComponent dragShadowComponent);

//	public double[] getMousePressedXYOffsets();

	/* TODO: See if we can get by without this method, it is a hack as all it does
	 * is simply subtraction and create the need for a new method */
//	public double[] getMousePressedWidthHeightOffsets();

//	public void setMousePressedXYOffsets(double xOffset, double yOffset);
}