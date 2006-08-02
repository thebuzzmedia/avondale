package com.kallasoft.avondale.component.painter;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.kallasoft.avondale.component.Component;

public class SwingComponentPainter extends DefaultComponentPainter
{
	private static ComponentPainter instance;

	public static synchronized ComponentPainter getInstance()
	{
		if (instance == null)
			instance = new SwingComponentPainter();

		return instance;
	}

	@Override
	public void paintComponent(Graphics2D g2d, Component component)
	{
		/* Perform default component painting */
		super.paintComponent(g2d, component);

		/* Now paint the Swing component */
		JComponent jComponent = ((SwingComponent) component).getJComponent();

		if (jComponent == null)
			return;

		/* TODO: We shouldn't need this after the new coordinates are in place */
		/*
		 * Swing paints all components from a 0,0 coordinate location, so
		 * translate the graphic context to the 0,0 of this component
		 */
		g2d.translate(jComponent.getX(), jComponent.getY());

		/* Paint the JComponent */
		jComponent.paint(g2d);
	}
}