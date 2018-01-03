package com.deportel.editor.administracion.view.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.deportel.editor.administracion.controller.PerfilGuiController;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.view.R;
import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * 
 * @author Emy
 * @since 30/08/2011
 */
public class PerfilActionListener extends EditorActionListener
{
	public PerfilActionListener(GuiManager manager)
	{
		super(manager);
	}

	private final PerfilGuiController	controller = PerfilGuiController.getInstance(this.manager);

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.log.debug("Se presiono el componente [" + source.getName() + "] del modulo [" + AdministracionEditor.getContext() + "] del editor de administracion");
		if (R.BUTTONS.ADD_MODULE.equalsIgnoreCase(source.getName()))
		{
			this.controller.addModule();
		}
		else if (R.BUTTONS.DEL_MODULE.equalsIgnoreCase(source.getName()))
		{
			this.controller.delModule();
		}
	}

}
