package com.kallasoft.avondale.component;

import java.awt.Color;

import com.kallasoft.avondale.component.border.LineComponentBorder;
import com.kallasoft.avondale.component.layout.RootComponentLayout;
import com.kallasoft.avondale.component.model.ComponentModel;
import com.kallasoft.avondale.panel.ComponentPanel;

public class RootComponent extends DefaultContainerComponent
{
	public RootComponent()
	{
		this(null);
	}
	
	public RootComponent(ComponentModel componentModel)
	{
		super(componentModel, null);

		setOpaque(false);
		setContainerLayout(RootComponentLayout.getInstace());

		if (ComponentPanel.DEBUG)
			setComponentBorder(new LineComponentBorder(Color.RED));
	}

	@Override
	public void setParentComponent(ContainerComponent parentComponent)
	{
		if (parentComponent == null)
			return;

		throw new UnsupportedOperationException(
				"parentComponent property cannot be set on a RootComponent");
	}

	/* TODO: Put this back after component  panel is reimplemented */
//	/**
//	 * Overridden in order to invalidate the overviewPanel's buffer. If the
//	 * rootComponent has been invalidated then something has changed that
//	 * required a relaying out of the components, which means the overviewPanel
//	 * likely needs to regenerate it's buffer.
//	 */
//	public void invalidate()
//	{
//		/* If this component is already invalid don't do anything */
//		if (!isValid())
//			return;
//
//		ComponentPanel componentPanel = getComponentPanel();
//
//		if (componentPanel != null)
//		{
//			OverviewPanel overviewPanel = componentPanel.getOverviewPanel();
//
//			if (overviewPanel != null)
//				overviewPanel.invalidateBuffer();
//		}
//
//		super.invalidate();
//	}
}