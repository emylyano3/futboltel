package com.deportel.editor.administracion.view.listener;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import com.deportel.editor.administracion.controller.PerfilGuiController;
import com.deportel.editor.administracion.controller.UsuarioGuiController;
import com.deportel.editor.administracion.view.R;
import com.deportel.editor.common.core.EditorListSelectionListener;
import com.deportel.guiBuilder.model.GuiManager;

public class AETableSelectionListener extends EditorListSelectionListener
{
	public AETableSelectionListener(GuiManager manager)
	{
		super(manager);
	}

	public AETableSelectionListener(GuiManager manager, JTable table)
	{
		super(manager);
		this.table = table;
	}

	private JTable table;

	@Override
	public void valueChanged (ListSelectionEvent e)
	{
		if (R.TABLES.PERFIL.equals(this.table.getName()))
		{
			PerfilGuiController.getInstance(this.manager).onListGridSlectionChange();
		}
		else if (R.TABLES.USUARIO.equals(this.table.getName()))
		{
			UsuarioGuiController.getInstance(this.manager).onListGridSlectionChange();
		}
	}

}
