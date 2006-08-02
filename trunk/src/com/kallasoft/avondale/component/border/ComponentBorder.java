package com.kallasoft.avondale.component.border;

import java.awt.Graphics2D;

import com.kallasoft.avondale.component.Component;

public interface ComponentBorder
{
	public static final double DEFAULT_BORDER_WIDTH = 1;

	public double getBorderWidth(Component component);

	public void paintBorder(Graphics2D g2d, Component component);
}