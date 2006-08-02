package com.kallasoft.avondale.model;

import javax.swing.Icon;

import com.kallasoft.avondale.component.ContainerComponent;

public interface ComponentLayer extends ContainerComponent
{
	public static final String Z_ORDER_PROPERTY_NAME = "zOrder";
	public static final String NAME_PROPERTY_NAME = "name";
	public static final String DESCRIPTION_PROPERTY_NAME = "description";
	public static final String ICON_PROPERTY_NAME = "icon";
	public static final String MODEL_PROPERTY_NAME = "model";
	
	public int getZOrder();
	
	public void setZOrder(int zOrder);
	
	public String getName();
	
	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public Icon getIcon();
	
	public void setIcon(Icon icon);
	
	public Model getModel();
	
	public void setModel(Model model);
}