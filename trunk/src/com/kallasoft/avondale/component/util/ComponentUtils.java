package com.kallasoft.avondale.component.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.ContainerComponent;
import com.kallasoft.avondale.panel.ComponentPanel;

/**
 * Utility class used to provide common utility methods to the Avondale
 * framework when dealing with <code>Component</code>s.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public class ComponentUtils
{
	/**
	 * Used to determine if <code>ancestorComponent</code> is an ancestor of
	 * <code>component</code>
	 * 
	 * @param component
	 *            The <code>Component</code> whose ancestry is being checked.
	 * @param ancestorComponent
	 *            The <code>Component</code> who is being checked to see if it
	 *            is an ancest of <code>component</code>.
	 * @return <code>true</code> if <code>ancestorComponent</code> is an
	 *         ancestor of <code>component</code>, otherwise returns
	 *         <code>false</code>.
	 */
	public static boolean isAncestorOfComponent(ContainerComponent component,
			ContainerComponent ancestorComponent)
	{
		boolean result = false;

		if (component != null && ancestorComponent != null
				&& (component != ancestorComponent))
		{
			ContainerComponent parentComponent = component.getParentComponent();

			if (ancestorComponent == parentComponent)
				result = true;
			else
				result = isAncestorOfComponent(parentComponent,
						ancestorComponent);
		}

		return result;
	}

	/**
	 * Used to determine if <code>descendantComponent</code> is a descendant
	 * of <code>component</code>
	 * 
	 * @param component
	 *            The <code>Component</code> whose ancestry is being checked.
	 * @param descendantComponent
	 *            The <code>Component</code> who is being checked to see if it
	 *            is a descendant of <code>component</code>.
	 * @return <code>true</code> if <code>descendantComponent</code> is an
	 *         descendant of <code>component</code>, otherwise returns
	 *         <code>false</code>.
	 */
	public static boolean isDescendantOfComponent(ContainerComponent component,
			ContainerComponent descendantComponent)
	{
		boolean result = false;

		if (component != null && descendantComponent != null)
		{
			Component[] childComponents = component.getComponents();

			for (int i = 0; !result && i < childComponents.length; i++)
			{
				Component childComponent = childComponents[i];

				if (descendantComponent == childComponent)
					result = true;
				else if (childComponent instanceof ContainerComponent)
					result = isDescendantOfComponent(
							(ContainerComponent) childComponent,
							descendantComponent);
			}
		}

		return result;
	}

	public static Point2D getOnScreenLocation(Component component)
	{
		return getOnScreenLocation(component, component.getX(), component
				.getY());
	}

	public static Point2D getOnScreenLocation(Component component, double x,
			double y)
	{
		Point2D location = null;

		if (component == null)
			return location;

		location = getLocationOnComponentPanel(component, x, y);

		/*
		 * We now have the location relative to the ComponentPanel, so now we
		 * need to factor in the location of the component panel as the last
		 * step.
		 */
		ComponentPanel componentPanel = component.getComponentPanel();

		if (componentPanel != null)
		{
			Point componentPanelLocation = componentPanel.getLocation();
			SwingUtilities.convertPointToScreen(componentPanelLocation,
					componentPanel);
			location.setLocation(location.getX()
					+ componentPanelLocation.getX(), location.getY()
					+ componentPanelLocation.getY());
		}

		return location;
	}

	public static Point2D getOnScreenLocation(Component component,
			Point2D location)
	{
		return getOnScreenLocation(component, location.getX(), location.getY());
	}

	public static Point2D getLocationOnComponentPanel(Component component)
	{
		return getLocationOnComponentPanel(component, component.getX(),
				component.getY());
	}

	public static Point2D getLocationOnComponentPanel(Component component,
			double x, double y)
	{
		Point2D location = null;

		if (component == null)
			return location;

		location = new Point2D.Double(x, y);

		for (ContainerComponent parent = component.getParentComponent(); parent != null; parent = parent
				.getParentComponent())
			location.setLocation(location.getX() + parent.getX(), location
					.getY()
					+ parent.getY());

		return location;
	}

	public static Point2D getLocationOnComponentPanel(Component component,
			Point2D location)
	{
		return getLocationOnComponentPanel(component, location.getX(), location
				.getY());
	}

	public static Rectangle2D getOnScreenBounds(Component component)
	{
		return getOnScreenBounds(component, component.getX(), component.getY(),
				component.getWidth(), component.getHeight());
	}

	public static Rectangle2D getOnScreenBounds(Component component, double x,
			double y, double width, double height)
	{
		if (component == null)
			return null;

		Point2D location = getOnScreenLocation(component, x, y);
		return new Rectangle2D.Double(location.getX(), location.getY(), width,
				height);
	}

	public static Rectangle2D getOnScreenBounds(Component component,
			Rectangle2D bounds)
	{
		return getOnScreenBounds(component, bounds.getX(), bounds.getY(),
				bounds.getWidth(), bounds.getHeight());
	}

	public static Rectangle2D getBoundsOnComponentPanel(Component component)
	{
		return getBoundsOnComponentPanel(component, component.getX(), component
				.getY(), component.getWidth(), component.getHeight());
	}

	public static Rectangle2D getBoundsOnComponentPanel(Component component,
			double x, double y, double width, double height)
	{
		if (component == null)
			return null;

		Point2D location = getLocationOnComponentPanel(component, x, y);
		return new Rectangle2D.Double(location.getX(), location.getY(), width,
				height);
	}

	public static Rectangle2D getBoundsOnComponentPanel(Component component,
			Rectangle2D bounds)
	{
		return getBoundsOnComponentPanel(component, bounds.getX(), bounds
				.getY(), bounds.getWidth(), bounds.getHeight());
	}
}