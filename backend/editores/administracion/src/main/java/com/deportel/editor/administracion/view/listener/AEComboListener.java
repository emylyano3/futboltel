package com.deportel.editor.administracion.view.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class AEComboListener extends EditorActionListener
{
	/**
	 * @param manager
	 */
	public AEComboListener(GuiManager manager)
	{
		super(manager);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComboBox source = (JComboBox) e.getSource();
		this.log.debug("Se presiono el combo [" + source.getName() + "] del editor de contenido");
	}

}
