package com.deportel.editor.contenido.controller;

import java.util.List;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.componentes.controller.ConsultaDinamicaController;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.editor.contenido.exception.ContenidoEditorException;
import com.deportel.editor.contenido.model.ProjectTexts;
import com.deportel.editor.contenido.view.QSWindow;
import com.deportel.editor.contenido.view.R;
import com.deportel.guiBuilder.gui.component.Dialog;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class QSController implements CallBackListener
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final GuiManager					manager;

	private Dialog								window;

	private String								mode;

	private final ConsultaDinamicaController	consultaDinamicaController	= ConsultaDinamicaController.getInstance(true);

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static QSController	instance;

	private QSController(GuiManager manager)
	{
		this.manager = manager;
	}

	public static QSController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new QSController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public void executeAction (String source)
	{
		if (R.BUTTONS.QS_CANCEL.equalsIgnoreCase(source))
		{
			this.window.dispose();
		}
		else if (R.BUTTONS.QS_DELETE.equalsIgnoreCase(source))
		{
			deleteQueryConfirmation();
		}
		else if (R.BUTTONS.QS_EDIT.equalsIgnoreCase(source))
		{
			this.window.dispose();
			initializeEditon();
		}
	}

	public void openWindow (String source) throws ContenidoEditorException
	{
		try
		{
			this.mode = source;
			this.window = QSWindow.getInstance(this.manager, this.mode);
			this.window.createAndShowGui();
			updateGrid();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ContenidoEditorException(ProjectTexts.ERROR_GENERAL);
		}
	}

	// *********************************************************************************************************************
	// Metodos Privados
	// *********************************************************************************************************************

	private void updateGrid ()
	{
		clearGrid();
		fillGrid();
	}

	private void fillGrid ()
	{
		List<ConsultaDinamica> dynaQueries = this.consultaDinamicaController.findAll();
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.QS_GRID).getModel();
		Vector<Object> row;
		for (ConsultaDinamica consultaDinamica : dynaQueries)
		{
			row = new Vector<Object>();
			row.add(consultaDinamica.getCId());
			row.add(consultaDinamica.getDNombre());
			row.add(consultaDinamica.getDDescripcion());
			model.addRow(row);
		}
	}

	private void clearGrid()
	{
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.QS_GRID).getModel();
		int modelCount=model.getRowCount();
		for (int i = 0; i < modelCount; i++)
		{
			model.removeRow(0);
		}
	}

	/**
	 *
	 */
	private void deleteQueryConfirmation ()
	{
		Popup.getInstance
		(
				"¿Está seguro de eliminar la consulta?",
				Popup.POPUP_CONFIRM,
				this.window,
				this
		)
		.showGui();
	}

	private void deleteQuery () throws ContenidoEditorException
	{
		JTable table = this.manager.getTable(R.TABLES.QS_GRID);
		int[] selection = table.getSelectedRows();
		Integer value;
		try
		{
			for (int element : selection)
			{
				value = (Integer) table.getModel().getValueAt(element, 0);
				this.consultaDinamicaController.remove(value);
			}
			updateGrid();
		}
		catch (InstanceNotFoundException e)
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_REMOVE_QUERY_NOT_FOUND);
		}
		catch (NumberFormatException e)
		{
			throw new ContenidoEditorException(ProjectTexts.ERROR_REMOVE_GENERAL);
		}
	}

	/**
	 *
	 */
	private void initializeEditon ()
	{
		JTable table = this.manager.getTable(R.TABLES.QS_GRID);
		int selection = table.getSelectedRow();
		if (selection != -1)
		{
			int queryId = (Integer) table.getValueAt(selection, 0);
			CEGuiController.getInstance(this.manager).enableQueryEdition(queryId);
		}
	}

	@Override
	public Object getData ()
	{
		return null;
	}

	@Override
	public void receiveCallBack (int action)
	{
		if (action == Popup.ACTION_CONFIRM)
		{
			try
			{
				deleteQuery();
			}
			catch (ContenidoEditorException e)
			{
				Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
			}
		}
	}

	@Override
	public void receiveCallBack (String command, CallBackLauncher laucher)
	{

	}

	@Override
	public void receiveCallBack (String command, Object data)
	{

	}
}
