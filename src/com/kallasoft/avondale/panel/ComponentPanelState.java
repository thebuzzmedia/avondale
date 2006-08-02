package com.kallasoft.avondale.panel;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.ext.bean.PropertyChangeSupport;

/**
 * Interface used to describe methods that can be used to determine the current
 * state of the <code>ComponentPanel</code>. Implementations of this class
 * will provides a means by which the <code>ComponentPanel</code> or custom
 * implementations of it can keep track of the state of the panel.
 * <p>
 * Custom implementations of the <code>ComponentPanel</code> may allow
 * component selection or even multiple selection (or other functionalities
 * specific to the implementation), in that case it is suggested that a custom
 * implementation of this interface that keeps track of selections, along with
 * components that know how to update their selected state be included in this
 * process as well. This is not provided in the core implementation of the state
 * strategy here, only the basic well-defined states will be kept track of.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 * @see ComponentPanel
 */
public interface ComponentPanelState extends PropertyChangeSupport
{
	public static final String FOCUSED_COMPONENT_PROPERTY_NAME = "focusedComponent";
	public static final String MOUSED_OVER_COMPONENT_PROPERTY_NAME = "mousedOverComponent";
	public static final String MOUSE_PRESSED_COMPONENT_PROPERTY_NAME = "mousePressedComponent";

	public Component getFocusedComponent();

	public void setFocusedComponent(Component focusedComponent);

	public Component getMousedOverComponent();

	public void setMousedOverComponent(Component mousedOverComponent);

	public Component getMousePressedComponent();

	public void setMousePressedComponent(Component mousePressedComponent);
}