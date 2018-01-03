package com.deportel.editor.common.core;

import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public abstract class EditorActionListener implements ActionListener
{

	protected Logger			log	= Logger.getLogger(this.getClass());

	protected final GuiManager	manager;

	public EditorActionListener(GuiManager manager)
	{
		this.manager = manager;
	}

}
