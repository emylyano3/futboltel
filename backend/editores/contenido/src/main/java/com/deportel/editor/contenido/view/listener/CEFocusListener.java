package com.deportel.editor.contenido.view.listener;

import java.awt.event.FocusEvent;

import javax.swing.JComponent;

import com.deportel.editor.common.core.EditorFocusListener;
import com.deportel.editor.contenido.controller.CEGuiController;
import com.deportel.editor.contenido.view.R;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class CEFocusListener extends EditorFocusListener
{
	/**
	 * @param manager
	 */
	public CEFocusListener(GuiManager manager)
	{
		super(manager);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained (FocusEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.log.debug("El componente [" + source.getName() +"] ganó el foco");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost (FocusEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.log.debug("El componente [" + source.getName() +"] perdió el foco");
		String sourceName = source.getName();
		if (R.TEXT_AREAS.SQL_CONSULTA.equals(sourceName))
		{
			CEGuiController.getInstance(this.manager).updateInterface(sourceName);
		}
		else if (R.TEXT_FIELDS.ROW_LIMIT.equals(sourceName))
		{
			CEGuiController.getInstance(this.manager).updateInterface(sourceName);
		}
	}

}
