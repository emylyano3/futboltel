package com.deportel.editor.administracion.controller;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.editor.administracion.exceptions.AdministracionException;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.model.ProjectTexts;
import com.deportel.editor.administracion.view.R;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public abstract class AEGuiControllerStrategy implements AEBasicAction, CallBackListener
{
	protected GuiManager		manager;

	private static final Logger	log	= Logger.getLogger(AEGuiControllerStrategy.class);

	/**
	 * Actualiza la interfaz del editor.
	 *
	 * @param source
	 */
	protected abstract void updateInterface (String source);

	/**
	 * Es llamado cuando el editor es puesto en escena
	 */
	public final void changeView ()
	{
		resetUI();
		setOwnControlsVisible();
		cleanInputControls();
		setInputControlsEnabled(false);
		clearListGrid();
		fillGrid();
		fillCombos();
	}

	/**
	 *
	 * @param value
	 */
	protected abstract void setOwnControlsVisible ();

	/**
	 * Limpia todos los controles utilizados por el usuario para ingresar datos
	 *
	 * @param value
	 */
	protected abstract void setInputControlsEnabled (boolean value);

	/**
	 * Limpia todos los controles utilizados por el usuario para ingresar datos
	 */
	protected abstract void cleanInputControls ();

	/**
	 * Llena la grilla con los datos del listado
	 */
	protected abstract void fillGrid ();

	/**
	 * Llena los combos del editor
	 */
	protected abstract void fillCombos ();

	/**
	 *
	 */
	protected abstract void setInputValues ();

	/**
	 *
	 * @param action
	 */
	public final void disableToolBar ()
	{
		this.manager.getButton(R.BUTTONS.NEW).setEnabled(false);
		this.manager.getButton(R.BUTTONS.DELETE).setEnabled(false);
		this.manager.getButton(R.BUTTONS.EDIT).setEnabled(false);
	}

	public final void enableToolBar ()
	{
		this.manager.getButton(R.BUTTONS.NEW).setEnabled(true);
		boolean enable = getListTable().getSelectedRow() != -1;
		this.manager.getButton(R.BUTTONS.DELETE).setEnabled(enable);
		this.manager.getButton(R.BUTTONS.EDIT).setEnabled(enable);
	}

	public void listGridGainedFocus()
	{
		log.info("La tabla de listado gano el foco, se activan las herramientas");
		enableToolBar();
	}

	/**
	 *
	 * @param action
	 */
	public void resetUI ()
	{
		this.manager.getButton(R.BUTTONS.NEW).setEnabled(false);
		this.manager.getButton(R.BUTTONS.DELETE).setEnabled(false);
		this.manager.getButton(R.BUTTONS.EDIT).setEnabled(false);
		this.manager.getButton(R.BUTTONS.ADD_MODULE).setVisible(false);
		this.manager.getButton(R.BUTTONS.DEL_MODULE).setVisible(false);
		this.manager.getButton(R.BUTTONS.ADD_PERFIL).setVisible(false);
		this.manager.getButton(R.BUTTONS.DEL_PERFIL).setVisible(false);
		this.manager.getButton(R.BUTTONS.GENERATE_PASSWORD).setVisible(false);

		this.manager.getTable(R.TABLES.PERFIL_CROSS_DATA).setEnabled(false);
		this.manager.getTable(R.TABLES.USER_CROSS_DATA).setEnabled(false);

		this.manager.getLabel(R.LABELS.APELLIDO).setVisible(false);
		this.manager.getLabel(R.LABELS.CONFIRM_EMAIL).setVisible(false);
		this.manager.getLabel(R.LABELS.LISTADO_MODULOS).setVisible(false);
		this.manager.getLabel(R.LABELS.LISTADO_PERFILES).setVisible(false);
		this.manager.getLabel(R.LABELS.LISTADO_TIPO_PERMISO).setVisible(false);
		this.manager.getLabel(R.LABELS.LISTADO_USUARIOS).setVisible(false);
		this.manager.getLabel(R.LABELS.PERFILES).setVisible(false);
		this.manager.getLabel(R.LABELS.DESCRIPCION).setVisible(false);
		this.manager.getLabel(R.LABELS.MODULOS).setVisible(false);
		this.manager.getLabel(R.LABELS.EMAIL).setVisible(false);
		this.manager.getLabel(R.LABELS.ALIAS).setVisible(false);

		this.manager.getPanel(R.PANELS.GENERAL).setVisible(false);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA).setVisible(false);
		this.manager.getPanel(R.PANELS.CROSS_DATA).setVisible(false);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA).setVisible(false);
		this.manager.getPanel(R.PANELS.CROSS_DATA).setVisible(false);

		this.manager.getTextField(R.TEXT_FIELDS.ALIAS).setVisible(false);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setVisible(false);
		this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).setVisible(false);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setVisible(false);
		this.manager.getTextField(R.TEXT_FIELDS.EMAIL).setVisible(false);
		this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL).setVisible(false);
		this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).setVisible(false);
	}

	/**
	 * Limpia la grilla especificada
	 *
	 * @param gridName
	 *            El nombre de la grilla
	 */
	protected void clearListGrid ()
	{
		log.debug("Se limpia la grilla de listado");
		clearGrid(getListTable().getName());
	}

	/**
	 * Limpia la grilla especificada
	 *
	 * @param gridName
	 *            El nombre de la grilla
	 */
	protected void clearGrid (String gridName)
	{
		log.debug("Se limpia la grilla: " + gridName);
		DefaultTableModel model = (DefaultTableModel)this.manager.getTable(gridName).getModel();
		int modelCount = model.getRowCount();
		for (int i = 0; i < modelCount; i++)
		{
			try
			{
				model.removeRow(0);
			}
			catch (Exception e)
			{
				log.error("Error limpiando la grilla: " + gridName);
			}
		}
	}

	/**
	 * Invocado cuando en la grilla que muestra el listado de datos, se modifica la seleccion.
	 */
	public void onListGridSlectionChange ()
	{
		//TODO Do nothing
	}

	@Override
	public final void prepareAdd ()
	{
		log.info("Preparando la pantalla de alta");
		setListTableSelectionEnabled(false);
		disableToolBar();
		cleanInputControls();
		setInputControlsEnabled(true);
		doPreAddActions();
	}

	@Override
	public final void prepareModify ()
	{
		log.info("Preparando la pantalla de modificacion");
		setListTableSelectionEnabled(false);
		disableToolBar();
		cleanInputControls();
		setInputControlsEnabled(true);
		setInputValues();
	}

	@Override
	public final void prepareDelete ()
	{
		log.info("Confirmar eliminacion");
		setListTableSelectionEnabled(false);
		disableToolBar();
		try
		{
			validateDelete();
			cleanInputControls();
			Popup.getInstance
			(
					AdministracionEditor.getTexts().get(ProjectTexts.WARN_REMOVE_DATA),
					Popup.POPUP_CONFIRM,
					null,
					this
			)
			.showGui();
		}
		catch (AdministracionException e)
		{
			Popup.getInstance
			(
					e.getMessage(),
					Popup.POPUP_ERROR,
					null,
					this
			)
			.showGui();
			setListTableSelectionEnabled(true);
		}
	}

	/**
	 *
	 * @param enabled
	 */
	private void setListTableSelectionEnabled (boolean enabled)
	{
		JTable table = getListTable();
		table.setFocusable(enabled);
		table.setEnabled(enabled);
	}

	public abstract JTable getListTable ();

	public abstract void doPreAddActions();

	public void safeAdd ()
	{
		try
		{
			validateAdd();
			add();
			setListTableSelectionEnabled(true);
			enableToolBar();
		}
		catch (AdministracionException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
		catch (DataException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_CREATE_DATA_TOO_LONG), Popup.POPUP_ERROR).showGui();
		}
		catch (ConstraintViolationException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_DATA_CONSTRAINT), Popup.POPUP_ERROR).showGui();
		}
		catch (HibernateException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_CREATE_DATA), Popup.POPUP_ERROR).showGui();
		}
		catch (RuntimeException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_CREATE_DATA), Popup.POPUP_ERROR).showGui();
		}
		catch (Exception e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_CREATE_DATA), Popup.POPUP_ERROR).showGui();
		}
	}

	public void safeDelete ()
	{
		try
		{
			delete();
			setListTableSelectionEnabled(true);
			enableToolBar();
		}
		catch (AdministracionException e)
		{
			log.error(e.getMessage(), e);
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			log.error("Error en la eliminacion", e);
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_GRID_SELECTION), Popup.POPUP_ERROR).showGui();
		}
		catch (NumberFormatException e)
		{
			log.error("Error en la eliminacion", e);
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_REMOVE_DATA), Popup.POPUP_ERROR).showGui();
		}
		catch (HibernateException e)
		{
			log.error("Error en la eliminacion", e);
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_REMOVE_DATA_CONSTRAINT), Popup.POPUP_ERROR).showGui();
		}
		catch (RuntimeException e)
		{
			log.error("Error en la eliminacion", e);
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_REMOVE_DATA), Popup.POPUP_ERROR).showGui();
		}
		catch (Exception e)
		{
			log.error("Error en la eliminacion", e);
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_REMOVE_DATA), Popup.POPUP_ERROR).showGui();
		}
	}

	public void safeModify ()
	{
		try
		{
			validateModify();
			modify();
			setListTableSelectionEnabled(true);
			enableToolBar();
		}
		catch (AdministracionException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
		catch (DataException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_EDIT_DATA_TOO_LONG), Popup.POPUP_ERROR).showGui();
		}
		catch (ConstraintViolationException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_DATA_CONSTRAINT), Popup.POPUP_ERROR).showGui();
		}
		catch (HibernateException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_EDIT_DATA), Popup.POPUP_ERROR).showGui();
		}
		catch (RuntimeException e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_EDIT_DATA), Popup.POPUP_ERROR).showGui();
		}
		catch (Exception e)
		{
			Popup.getInstance(AdministracionEditor.getTexts().get(ProjectTexts.ERROR_EDIT_DATA), Popup.POPUP_ERROR).showGui();
		}
	}

	@Override
	public void receiveCallBack (int command)
	{
		switch (command)
		{
			case Popup.ACTION_CONFIRM:
				safeDelete();
				break;
			case Popup.ACTION_NOT_CONFIRM:
				setListTableSelectionEnabled(true);
				break;

			default:
				break;
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

	@Override
	public Object getData ()
	{
		return null;
	}

	@Override
	public void cancelAction ()
	{
		cleanInputControls();
		setListTableSelectionEnabled(true);
		setInputControlsEnabled(false);
		enableToolBar();
	}
}
