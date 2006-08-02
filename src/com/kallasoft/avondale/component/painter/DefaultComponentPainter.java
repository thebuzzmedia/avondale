package com.kallasoft.avondale.component.painter;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

import com.kallasoft.avondale.component.Component;

public class DefaultComponentPainter implements ComponentPainter
{
	private static ComponentPainter instance;

	public static synchronized ComponentPainter getInstance()
	{
		if (instance == null)
			instance = new DefaultComponentPainter();

		return instance;
	}

	public void paintComponent(Graphics2D g2d, Component component)
	{
		Paint oldPaint = g2d.getPaint();
		Paint backgroundPaint = component.getBackgroundPaint();

		if (!component.isOpaque() || backgroundPaint == null)
			return;

		Shape shape = component.getComponentShape();

		if (shape == null)
			return;

		g2d.setPaint(backgroundPaint);
		g2d.fill(shape);
		g2d.setPaint(oldPaint);
	}
}