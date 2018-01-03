package com.deportel.editor.contenido.view.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.editor.contenido.controller.CEGuiController;
import com.deportel.editor.contenido.controller.QSController;
import com.deportel.editor.contenido.controller.QTController;
import com.deportel.editor.contenido.view.R;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class CEActionListener extends EditorActionListener
{
	/**
	 * @param manager
	 */
	public CEActionListener(GuiManager manager)
	{
		super(manager);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		if (!CEGuiController.getInstance(this.manager).isInitialized())
		{
			return;
		}
		JComponent source = (JComponent) e.getSource();
		this.log.debug("Se presiono el componente [" + source.getName() + "] del editor de contenido");
		if (R.BINDED_COMBOS.TEMA_DE_APLICACION.equalsIgnoreCase(source.getName()))
		{
			CEGuiController.getInstance(this.manager).updateInterface(source.getName());
		}
		else if (R.BINDED_COMBOS.TIPO_COMPONENTE_SOLICITANTE.equalsIgnoreCase(source.getName()))
		{
			CEGuiController.getInstance(this.manager).updateInterface(source.getName());
		}
		else if (R.BINDED_COMBOS.COMPONENTE_SOLICITANTE.equalsIgnoreCase(source.getName()))
		{
			CEGuiController.getInstance(this.manager).updateInterface(source.getName());
		}
		else if (R.BINDED_COMBOS.COMPONENTE_RESP.equalsIgnoreCase(source.getName()))
		{
			CEGuiController.getInstance(this.manager).updateInterface(source.getName());
		}
		else if (R.BUTTONS.SAVE.equals(source.getName()))
		{
			CEGuiController.getInstance(this.manager).saveQueryConfiguration(source.getName());
		}
		else if (R.BUTTONS.SAVE_AS_NEW.equals(source.getName()))
		{
			CEGuiController.getInstance(this.manager).saveQueryConfiguration(source.getName());
		}
		else if (R.BUTTONS.NEW.equals(source.getName()))
		{
			CEGuiController.getInstance(this.manager).enableQueryCreation();
		}
		else if (R.BUTTONS.EDIT.equals(source.getName()))
		{
			CEGuiController.getInstance(this.manager).openQuerySelectionWindow(R.BUTTONS.EDIT);
		}
		else if (R.BUTTONS.DELETE.equals(source.getName()))
		{
			CEGuiController.getInstance(this.manager).openQuerySelectionWindow(R.BUTTONS.DELETE);
		}
		else if (R.BUTTONS.TEST_QUERY.equals(source.getName()))
		{
			CEGuiController.getInstance(this.manager).openQueryTesterWindow();
		}
		else if (R.BUTTONS.QT_EJECUTAR.equalsIgnoreCase(source.getName()))
		{
			this.manager.getProgressBar(R.PROGRESS_BARS.EXECUTING_QUERY).setVisible(true);
			QTController.getInstance(this.manager, CEGuiController.getInstance(this.manager)).testQueryInWindow();
		}
		else if (R.BUTTONS.QS_DELETE.equalsIgnoreCase(source.getName()))
		{
			QSController.getInstance(this.manager).executeAction(R.BUTTONS.QS_DELETE);
		}
		else if (R.BUTTONS.QS_EDIT.equalsIgnoreCase(source.getName()))
		{
			QSController.getInstance(this.manager).executeAction(R.BUTTONS.QS_EDIT);
		}
		else if (R.BUTTONS.QS_CANCEL.equalsIgnoreCase(source.getName()))
		{
			QSController.getInstance(this.manager).executeAction(R.BUTTONS.QS_CANCEL);
		}
		else if (R.CHECK_BOXES.LIMIT_ROWS.equalsIgnoreCase(source.getName()))
		{
			CEGuiController.getInstance(this.manager).updateInterface(source.getName());
		}
		else if (R.BINDED_COMBOS.TIPO_COMPONENTE_RESP.equalsIgnoreCase(source.getName()))
		{
			CEGuiController.getInstance(this.manager).updateInterface(source.getName());
		}
	}

}
