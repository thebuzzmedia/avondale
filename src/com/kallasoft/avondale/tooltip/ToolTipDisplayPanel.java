package com.kallasoft.avondale.tooltip;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.kallasoft.avondale.Spacer;

public class ToolTipDisplayPanel extends JPanel
{
	private static final long serialVersionUID = 8096555969300047562L;

	private JLabel label;
	private ToolTip toolTip;

	public ToolTipDisplayPanel()
	{
		add((label = new JLabel()));
	}

	public ToolTip getToolTip()
	{
		return toolTip;
	}

	public void setToolTip(ToolTip toolTip)
	{
		Spacer padding = toolTip.getPadding();
		Color backgroundColor = toolTip.getBackgroundColor();
		Color foregroundColor = toolTip.getForegroundColor();
		Font font = toolTip.getFont();
		Icon icon = toolTip.getIcon();
		String text = toolTip.getText();

		/* Adjust the layout settings */
		FlowLayout layout = (FlowLayout) getLayout();
		layout
				.setHgap((int) (padding.getLeftSpace() + padding
						.getRightSpace()) / 2);
		layout
				.setVgap((int) (padding.getTopSpace() + padding
						.getBottomSpace()) / 2);

		/* Adjust the background of this panel to match the tooltip */
		setBackground(backgroundColor);
		setBorder(BorderFactory.createLineBorder(foregroundColor));

		/* Adjust the properties of the label to reflect the tooltip's */
		label.setForeground(foregroundColor);
		label.setFont(font);
		label.setIcon(icon);
		label.setText(text);
		
		/* Lay the panel back out */
		revalidate();
	}
}