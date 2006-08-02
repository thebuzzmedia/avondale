package com.kallasoft.avondale.component;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.kallasoft.avondale.component.event.ContainerComponentListener;
import com.kallasoft.avondale.component.layout.ContainerLayout;

public interface ContainerComponent extends ConnectedComponent
{
	public static final String CONTAINER_LAYOUT_ENALBED_PROPERTY_NAME = "containerLayoutEnabled";
	public static final String CONTAINER_LAYOUT_PROPERTY_NAME = "containerLayout";
	public static final String CONTAINER_COMPONENT_NOTIFICATION_ENABLED_PROPERTY_NAME = "containerComponentNotificationEnabled";

	public boolean isContainerLayoutEnabled();

	public void setContainerLayoutEnabled(boolean containerLayoutEnabled);

	public void layoutContainer();

	public ContainerLayout getContainerLayout();

	public void setContainerLayout(ContainerLayout containerLayout);

	public void paintChildren(Graphics2D g2d);

	public boolean containsComponent(Component component);

	public void addComponent(Component component);

	public void addComponent(int index, Component component);

	public int getComponentCount();

	public int getIndexOfComponent(Component component);

	public Component getComponent(int index);

	public Component[] getComponentsAtLocation(double x, double y);

	public Component getComponentAtLocationRecursively(double x, double y);

	public Component[] getComponentsInBounds(Rectangle2D bounds);

	public Component[] getComponents();

	public void removeComponent(int index);

	public void removeComponent(Component component);

	public boolean isContainerComponentNotificationEnabled();

	public void setContainerComponentNotificationEnabled(
			boolean containerComponentNotificationEnabled);

	public boolean containsContainerComponentListener(
			ContainerComponentListener containerComponentListener);

	public void addContainerComponentListener(
			ContainerComponentListener containerComponentListener);

	public void addContainerComponentListener(
			int index, ContainerComponentListener containerComponentListener);

	public int getContainerComponentListenerCount();

	public int getIndexOfContainerComponentListener(
			ContainerComponentListener containerComponentListener);

	public ContainerComponentListener getContainerComponentListener(int index);

	public ContainerComponentListener[] getContainerComponentListeners();

	public void removeContainerComponentListener(int index);

	public void removeContainerComponentListener(
			ContainerComponentListener containerComponentListener);

	public void removeContainerComponentListeners();
}