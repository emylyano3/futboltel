package com.deportel.editor.common.core;

import java.awt.event.FocusListener;

import org.apache.log4j.Logger;

import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public abstract class EditorFocusListener implements FocusListener
{

	protected Logger			log	= Logger.getLogger(this.getClass());

	protected final GuiManager	manager;

	public EditorFocusListener(GuiManager manager)
	{
		this.manager = manager;
	}

}
