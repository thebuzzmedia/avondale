package com.kallasoft.avondale.component.layout;

import com.kallasoft.avondale.component.ContainerComponent;

/**
 * Interface used to describe a layout algorithm that can be applied to a
 * <code>ContainerComponent</code> in order to lay out any child
 * <code>Component</code>s it contains.
 * 
 * @author Riyad Kalla
 * @version 1.0
 * @since 1.0
 */
public interface ContainerLayout
{
	/**
	 * Used to run the layout algorithm on the <code>ContainerComponent</code>
	 * that is passed in.
	 * 
	 * @param containerComponent
	 *            The <code>ContainerComponent</code> whose child
	 *            <code>Component</code>s will be laid out.
	 * @return <code>true</code> if any layout was changed or
	 *         <code>false</code> if the container did not need to be laid out
	 *         and nothing changed.
	 */
	public boolean layoutContainer(ContainerComponent containerComponent);
}