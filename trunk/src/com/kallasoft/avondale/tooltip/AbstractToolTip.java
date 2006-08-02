package com.kallasoft.avondale.tooltip;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;

import com.kallasoft.avondale.Spacer;
import com.kallasoft.avondale.component.Component;

public abstract class AbstractToolTip implements ToolTip
{
	private boolean visible = true;
	private int xOffset = DEFAULT_X_OFFSET;
	private int yOffset = DEFAULT_Y_OFFSET;
	private Spacer padding = DEFAULT_PADDING;
	private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
	private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
	private Font font = DEFAULT_FONT;
	private Icon icon;
	private String text;
	private Component component;

	public AbstractToolTip(Component component, String text, Icon icon)
	{
		this.component = component;
		this.text = text;
		this.icon = icon;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public int getXOffset()
	{
		return xOffset;
	}

	public void setXOffset(int xOffset)
	{
		this.xOffset = xOffset;
	}

	public int getYOffset()
	{
		return yOffset;
	}

	public void setYOffset(int yOffset)
	{
		this.yOffset = yOffset;
	}

	public Spacer getPadding()
	{
		return padding;
	}

	public void setPadding(Spacer padding)
	{
		this.padding = padding;
	}

	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public Color getForegroundColor()
	{
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor)
	{
		this.foregroundColor = foregroundColor;
	}

	public Font getFont()
	{
		return font;
	}

	public void setFont(Font font)
	{
		this.font = font;
	}

	public Icon getIcon()
	{
		return icon;
	}

	public void setIcon(Icon icon)
	{
		this.icon = icon;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public Component getComponent()
	{
		return component;
	}
}