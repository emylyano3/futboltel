package com.deportel.editor.administracion.view.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.deportel.editor.administracion.controller.UsuarioGuiController;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.view.R;
import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * 
 * @author Emy
 * @since 30/08/2011
 */
public class UsuarioActionListener extends EditorActionListener
{
	public UsuarioActionListener(GuiManager manager)
	{
		super(manager);
	}

	private final UsuarioGuiController	controller = UsuarioGuiController.getInstance(this.manager);

	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.log.debug("Se presiono el componente [" + source.getName() + "] del modulo [" + AdministracionEditor.getContext() + "] del editor de administracion");
		if (R.BUTTONS.ADD_PERFIL.equalsIgnoreCase(source.getName()))
		{
			this.controller.addProfile();
		}
		else if (R.BUTTONS.DEL_PERFIL.equalsIgnoreCase(source.getName()))
		{
			this.controller.delProfile();
		}
		else if (R.BUTTONS.GENERATE_PASSWORD.equalsIgnoreCase(source.getName()))
		{
			this.controller.generateNewPassword();
		}
	}

}
