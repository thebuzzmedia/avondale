package com.kallasoft.avondale.component;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.kallasoft.avondale.component.event.ContainerComponentEvent;
import com.kallasoft.avondale.component.event.ContainerComponentListener;
import com.kallasoft.avondale.component.event.DefaultContainerComponentEvent;
import com.kallasoft.avondale.component.layout.ContainerLayout;
import com.kallasoft.avondale.component.model.ComponentModel;

public abstract class AbstractContainerComponent extends
		DefaultConnectedComponent implements ContainerComponent
{
	private boolean containerLayoutEnabled = true;
	private boolean containerComponentNotificationEnabled = true;
	private ContainerLayout containerLayout;
	private List<Component> componentList;
	private transient List<ContainerComponentListener> containerComponentListenerList;

	public AbstractContainerComponent(ComponentModel componentModel,
			ContainerComponent parentComponent)
	{
		super(componentModel);

		componentList = new ArrayList<Component>(0);
		containerComponentListenerList = new ArrayList<ContainerComponentListener>(
				0);

		setParentComponent(parentComponent);
	}

	public boolean isContainerLayoutEnabled()
	{
		return containerLayoutEnabled;
	}

	public void setContainerLayoutEnabled(boolean containerLayoutEnabled)
	{
		boolean oldContainerLayoutEnabled = isContainerLayoutEnabled();

		if (oldContainerLayoutEnabled == containerLayoutEnabled)
			return;

		this.containerLayoutEnabled = containerLayoutEnabled;
		firePropertyChangeEvent(this, CONTAINER_LAYOUT_ENALBED_PROPERTY_NAME,
				oldContainerLayoutEnabled, isContainerLayoutEnabled());
	}

	public ContainerLayout getContainerLayout()
	{
		return containerLayout;
	}

	public void setContainerLayout(ContainerLayout containerLayout)
	{
		ContainerLayout oldLayout = getContainerLayout();

		if (oldLayout == containerLayout)
			return;

		this.containerLayout = containerLayout;
		firePropertyChangeEvent(this, CONTAINER_LAYOUT_PROPERTY_NAME,
				oldLayout, getContainerLayout());

		/* Invalidate the component */
		invalidate();
	}

	public boolean containsComponent(Component component)
	{
		return componentList.contains(component);
	}

	public void addComponent(Component component)
	{
		addComponent(getComponentCount(), component);
	}

	public void addComponent(int index, Component component)
	{
		if (component == null)
			throw new NullPointerException("component cannot be null");

		if (containsComponent(component))
			return;

		if (component instanceof ContainerComponent)
		{
			ContainerComponent containerComponent = (ContainerComponent) component;
			ContainerComponent oldParentComponent = containerComponent
					.getParentComponent();

			/* Remove the component from it's previous parent */
			if (oldParentComponent != null)
				oldParentComponent.removeComponent(component);
		}

		/* Set the new parent component to this component */
		component.setParentComponent(this);

		/*
		 * Clear the new component's reference to any ComponentPanel if it has
		 * one, the rootComponent should be the only component in the tree who
		 * has one.
		 */
		component.setComponentPanel(null);

		componentList.add(index, component);
		fireContainerComponentEvent(new DefaultContainerComponentEvent(this,
				ContainerComponentEvent.EventType.COMPONENT_ADDED, component));

		/* Revalidate and repaint the component */
		revalidate();
		repaint();
	}

	public int getComponentCount()
	{
		return componentList.size();
	}

	public int getIndexOfComponent(Component component)
	{
		return componentList.indexOf(component);
	}

	public Component getComponent(int index)
	{
		return componentList.get(index);
	}

	public Component[] getComponentsAtLocation(double x, double y)
	{
		List<Component> list = new ArrayList<Component>(0);

		for (int i = 0, size = getComponentCount(); i < size; i++)
		{
			Component component = getComponent(i);

			if (component.contains(x, y))
				list.add(component);
		}

		return list.toArray(new Component[list.size()]);
	}

	public Component getComponentAtLocationRecursively(double x, double y)
	{
		Component component = null;

		for (int i = 0, size = getComponentCount(); component == null
				&& i < size; i++)
		{
			Component currentComponent = getComponent(i);

			/*
			 * First check to see if currentComponent contains the coordinates
			 */
			if (currentComponent.contains(x, y))
			{
				/*
				 * Now check if the currentComponent is an instance of
				 * ContainerComponent, in which case one of it's children could
				 * contain the coordinates
				 */
				if (currentComponent instanceof ContainerComponent)
				{
					/*
					 * Adjust the coordinates to the coordinate space of the
					 * child components.
					 */
					x -= getX();
					y -= getY();

					/*
					 * We need to recursively check the currentComponent to see
					 * if any of it's children contains this coordinate.
					 */
					component = ((ContainerComponent) currentComponent)
							.getComponentAtLocationRecursively(x, y);
				}

				/*
				 * None of the child components contained the coordinates, so
				 * the only component that contains these coordinates is the
				 * currentComponent.
				 */
				if (component == null)
					component = currentComponent;
			}
		}

		return component;
	}

	public Component[] getComponentsInBounds(Rectangle2D bounds)
	{
		List<Component> list = new ArrayList<Component>(0);

		if (bounds != null && bounds.getWidth() > 0 && bounds.getHeight() > 0)
		{
			for (int i = 0, size = getComponentCount(); i < size; i++)
			{
				Component component = getComponent(i);

				if (bounds.contains(component.getX(), component.getY(),
						component.getWidth(), component.getHeight()))
					list.add(component);
			}
		}

		return list.toArray(new Component[list.size()]);
	}

	public Component[] getComponents()
	{
		return componentList.toArray(new Component[getComponentCount()]);
	}

	public void removeComponent(int index)
	{
		Component component = componentList.remove(index);

		if (component == null)
			return;

		/* Clear the parent property */
		component.setParentComponent(null);
		fireContainerComponentEvent(new DefaultContainerComponentEvent(this,
				ContainerComponentEvent.EventType.COMPONENT_REMOVED, component));

		/* Revalidate and repaint the component */
		revalidate();
		repaint();
	}

	public void removeComponent(Component component)
	{
		if (component == null)
			return;

		int index = getIndexOfComponent(component);

		if (index > -1)
			removeComponent(index);
	}

	public boolean isContainerComponentNotificationEnabled()
	{
		return containerComponentNotificationEnabled;
	}

	public void setContainerComponentNotificationEnabled(
			boolean containerComponentNotificationEnabled)
	{
		boolean oldContainerComponentNotificationEnabled = isContainerComponentNotificationEnabled();

		if (oldContainerComponentNotificationEnabled == containerComponentNotificationEnabled)
			return;

		this.containerComponentNotificationEnabled = containerComponentNotificationEnabled;
		firePropertyChangeEvent(this,
				CONTAINER_COMPONENT_NOTIFICATION_ENABLED_PROPERTY_NAME,
				oldContainerComponentNotificationEnabled,
				isContainerComponentNotificationEnabled());
	}

	public boolean containsContainerComponentListener(
			ContainerComponentListener containerComponentListener)
	{
		return containerComponentListenerList
				.contains(containerComponentListener);
	}

	public void addContainerComponentListener(
			ContainerComponentListener containerComponentListener)
	{
		addContainerComponentListener(getContainerComponentListenerCount(),
				containerComponentListener);
	}

	public void addContainerComponentListener(int index,
			ContainerComponentListener containerComponentListener)
	{
		if (containerComponentListener == null)
			return;

		if (!containsContainerComponentListener(containerComponentListener))
			containerComponentListenerList.add(index,
					containerComponentListener);
	}

	public int getContainerComponentListenerCount()
	{
		return containerComponentListenerList.size();
	}

	public int getIndexOfContainerComponentListener(
			ContainerComponentListener containerComponentListener)
	{
		return containerComponentListenerList
				.indexOf(containerComponentListener);
	}

	public ContainerComponentListener getContainerComponentListener(int index)
	{
		return containerComponentListenerList.get(index);
	}

	public ContainerComponentListener[] getContainerComponentListeners()
	{
		return containerComponentListenerList
				.toArray(new ContainerComponentListener[getContainerComponentListenerCount()]);
	}

	public void removeContainerComponentListener(int index)
	{
		containerComponentListenerList.remove(index);
	}

	public void removeContainerComponentListener(
			ContainerComponentListener containerComponentListener)
	{
		if (containerComponentListener == null)
			return;

		int index = getIndexOfContainerComponentListener(containerComponentListener);

		if (index > -1)
			removeContainerComponentListener(index);
	}

	public void removeContainerComponentListeners()
	{
		containerComponentListenerList.clear();
	}

	protected void fireContainerComponentEvent(ContainerComponentEvent evt)
	{
		if (evt == null || !isContainerComponentNotificationEnabled())
			return;

		for (int i = 0, size = getContainerComponentListenerCount(); i < size; i++)
		{
			switch (evt.getEventType())
			{
				case COMPONENT_ADDED:
					getContainerComponentListener(i).componentsAdded(evt);
					break;

				case COMPONENT_REMOVED:
					getContainerComponentListener(i).componentsRemoved(evt);
					break;
			}
		}
	}
}