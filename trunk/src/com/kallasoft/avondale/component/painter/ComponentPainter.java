package com.kallasoft.avondale.component.painter;

import java.awt.Graphics2D;

import com.kallasoft.avondale.component.Component;

public interface ComponentPainter
{
	public void paintComponent(Graphics2D g2d, Component component);
}