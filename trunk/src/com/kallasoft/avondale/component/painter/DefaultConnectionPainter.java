package com.kallasoft.avondale.component.painter;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import com.kallasoft.avondale.component.Component;
import com.kallasoft.avondale.component.connection.Connection;

public class DefaultConnectionPainter implements ComponentPainter
{
	private static ComponentPainter instance;

	public static synchronized ComponentPainter getInstance()
	{
		if (instance == null)
			instance = new DefaultConnectionPainter();

		return instance;
	}

	public void paintComponent(Graphics2D g2d, Component component)
	{
		Connection connection = (Connection) component;
		Shape shape = connection.getComponentShape();

		if (shape == null)
			return;

		Stroke connectionStroke = connection.getConnectionStroke();

		if (connectionStroke == null)
			return;

		Paint backgroundPaint = connection.getBackgroundPaint();

		if (backgroundPaint != null)
			g2d.setPaint(backgroundPaint);

		g2d.setStroke(connectionStroke);
		g2d.draw(shape);
	}
}