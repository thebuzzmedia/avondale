package com.kallasoft.avondale.tooltip;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.Serializable;

import javax.swing.Icon;

import com.kallasoft.avondale.DefaultSpacer;
import com.kallasoft.avondale.Spacer;
import com.kallasoft.avondale.component.Component;

/* TODO: Refactor this to a top level tooltip dir because it effects components
 * and the componentpanel. */
/* TODO: Need to implement this like Swing does, with a light weight JPanel
 * as the first attempt at displaying it in the window and if that isn't possible
 * using the JWindow. The reason for that is using the JWindow causes flicker
 * in the main frame. Maybe this is caused by focus change and this can be
 * avoided by making the root not focusable? */
public interface ToolTip extends Serializable
{
	public static final int DEFAULT_X_OFFSET = 0;
	public static final int DEFAULT_Y_OFFSET = 24;
	
	public static final Spacer DEFAULT_PADDING = new DefaultSpacer();
	public static final Color DEFAULT_BACKGROUND_COLOR = SystemColor.info;
	public static final Color DEFAULT_FOREGROUND_COLOR = SystemColor.infoText;
	public static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 11);

	public boolean isVisible();

	public void setVisible(boolean visible);
	
	public int getXOffset();

	public void setXOffset(int xOffset);

	public int getYOffset();

	public void setYOffset(int yOffset);

	public Spacer getPadding();

	public void setPadding(Spacer padding);

	public Color getBackgroundColor();

	public void setBackgroundColor(Color backgroundColor);

	public Color getForegroundColor();

	public void setForegroundColor(Color foregroundColor);

	public Font getFont();

	public void setFont(Font font);

	public Icon getIcon();

	public void setIcon(Icon icon);

	public String getText();

	public void setText(String text);
	
	public Component getComponent();
}