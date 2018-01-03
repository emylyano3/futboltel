package com.deportel.editor.common.core;

import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public abstract class EditorListSelectionListener implements ListSelectionListener
{

	protected static Logger		log	= Logger.getLogger(EditorListSelectionListener.class);

	protected final GuiManager	manager;

	public EditorListSelectionListener(GuiManager manager)
	{
		this.manager = manager;
	}

}
