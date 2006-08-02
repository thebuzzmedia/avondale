package com.kallasoft.avondale.component.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.logging.Logger;

import com.kallasoft.avondale.component.Component;

public class LineComponentBorder extends AbstractComponentBorder
{
	public static final Paint DEFAULT_LINE_PAINT = Color.BLACK;

	public static final Stroke SOLID_LINE_STROKE = new BasicStroke(
			(float) DEFAULT_BORDER_WIDTH);
	public static final Stroke DASHED_LINE_STROKE = new BasicStroke(
			(float) DEFAULT_BORDER_WIDTH, BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_MITER, 10, new float[] { 2, 2 }, 0);

	private static ComponentBorder instance;

	public static synchronized ComponentBorder getInstance()
	{
		if (instance == null)
			instance = new LineComponentBorder();

		return instance;
	}
	
	private Paint linePaint;
	private Stroke lineStroke;

	public LineComponentBorder()
	{
		this(DEFAULT_LINE_PAINT);
	}

	public LineComponentBorder(Paint linePaint)
	{
		this(linePaint, DEFAULT_BORDER_WIDTH);
	}

	public LineComponentBorder(Paint linePaint, double width)
	{
		this(linePaint, width, new BasicStroke((float) width));
	}

	public LineComponentBorder(Paint linePaint, double width, Stroke lineStroke)
	{
		setLinePaint(linePaint);
		setLineStroke(width, lineStroke);
	}

	public void paintBorder(Graphics2D g2d, Component component)
	{
		Paint oldPaint = g2d.getPaint();
		Paint linePaint = getLinePaint();

		if (linePaint == null)
			return;

		Stroke oldStroke = g2d.getStroke();
		Stroke lineStroke = getLineStroke();

		if (lineStroke == null || getBorderWidth(component) == 0)
			return;

		Shape shape = component.getComponentShape();

		if (shape == null)
			return;

		g2d.setPaint(linePaint);
		g2d.setStroke(lineStroke);
		g2d.draw(shape);
		g2d.setStroke(oldStroke);
		g2d.setPaint(oldPaint);
	}

	public Paint getLinePaint()
	{
		return linePaint;
	}

	public void setLinePaint(Paint linePaint)
	{
		this.linePaint = linePaint;
	}

	public Stroke getLineStroke()
	{
		return lineStroke;
	}

	public void setLineStroke(BasicStroke lineStroke)
	{
		setLineStroke(lineStroke.getLineWidth(), lineStroke);
	}

	public void setLineStroke(double borderWidth, Stroke lineStroke)
	{
		if (borderWidth < 0)
			throw new IllegalArgumentException(
					"borderWidth must be >= 0 and should be equal to the width of the line defined by the lineStroke otherwise rendering anomolies can occur (clipping calculations will be incorrect)");

		if (lineStroke instanceof BasicStroke)
		{
			float value = ((BasicStroke) lineStroke).getLineWidth();

			if (((float) borderWidth) != value)
				Logger.getLogger(getClass().getName()).warning("The value of borderWidth specified does not equal the returned value from ((BasicStroke)lineStroke).getLineWidth(), these values should be equal otherwise painting anomolies can occur (clipping calculations will be incorrect).");
		}

		this.borderWidth = borderWidth;
		this.lineStroke = lineStroke;
	}
}