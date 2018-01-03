package com.deportel.editor.administracion.controller;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityNotFoundException;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.deportel.administracion.controller.TipoPermisoController;
import com.deportel.administracion.dao.hibernate.ModuloPerfilDaoHibernate;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.common.utils.Converter;
import com.deportel.common.utils.Utils;
import com.deportel.editor.administracion.exceptions.AdministracionException;
import com.deportel.editor.administracion.model.ProjectTexts;
import com.deportel.editor.administracion.model.ProjectValues;
import com.deportel.editor.administracion.view.R;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class TipoPermisoGuiController extends AEGuiControllerStrategy
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger				log						= Logger.getLogger(TipoPermisoGuiController.class);
	private final TipoPermisoController		tipoPermisoController	= TipoPermisoController.getInstance();

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static TipoPermisoGuiController	instance;

	private TipoPermisoGuiController(GuiManager manager)
	{
		this.manager = manager;
	}

	public static TipoPermisoGuiController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new TipoPermisoGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void updateInterface(String source)
	{
		if (R.BUTTONS.DELETE.equals(source))
		{
			setInputControlsEnabled(false);
		}
		else if (R.BUTTONS.OK.equals(source))
		{
			cleanInputControls();
			setInputControlsEnabled(false);
			clearListGrid();
			fillGrid();
		}
	}

	@Override
	protected void setInputControlsEnabled(boolean value)
	{
		this.manager.getLabel(R.LABELS.DESCRIPCION).setEnabled(value);
		this.manager.getLabel(R.LABELS.NOMBRE).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setEnabled(value);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setEnabled(value);
		this.manager.getButton(R.BUTTONS.OK).setEnabled(value);
		this.manager.getButton(R.BUTTONS.CANCEL).setEnabled(value);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).updateUI();
	}

	@Override
	public void cleanInputControls()
	{
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText("");
	}

	@Override
	public void validateAdd ()
	{
		String nombre = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText();
		String desc = this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).getText();
		if (Utils.isNullOrEmpty(nombre)) throw new AdministracionException(ProjectTexts.ERROR_NAME_NULL);
		if (!Utils.validateAlphaNumeric(nombre, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_NAME);
		if (!Utils.validateAlphaNumeric(desc, 200)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_DESC);
		if (this.tipoPermisoController.findByName(nombre) != null) throw new AdministracionException(ProjectTexts.ERROR_PERMISSION_TYPE_EXIST);
	}

	@Override
	public void validateModify ()
	{
		String nombre = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText();
		String desc = this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).getText();
		if (Utils.isNullOrEmpty(nombre)) throw new AdministracionException(ProjectTexts.ERROR_NAME_NULL);
		if (!Utils.validateAlphaNumeric(nombre, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_NAME);
		if (!Utils.validateAlphaNumeric(desc, 200)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_DESC);
	}

	@Override
	public void add ()
	{
		log.info("Creo el tipo de permiso");
		//tomo los valores nuevos
		String nombre = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String descripcion = (this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION)).getText();

		//valido que no exista
		try
		{
			TipoPermiso tipoPermisoExistente = this.tipoPermisoController.findByName(nombre);
			if (tipoPermisoExistente != null)
			{
				throw new InvalidParameterException(ProjectTexts.ERROR_PERMISSION_TYPE_NAME_EXIST);
			}
		}
		catch (EntityNotFoundException e)
		{
			log.debug("No se encontró el tipo de permiso, se continúa con el alta");
		}
		//Creo el tipo de permiso
		TipoPermiso tipoPermiso = new TipoPermiso(nombre, descripcion);
		TipoPermiso tipoPermisoNuevo = this.tipoPermisoController.crear(tipoPermiso);
		log.info("se creó el tipo de permiso: " + tipoPermisoNuevo.toString());
		updateInterface(R.BUTTONS.OK);
	}

	@Override
	public void validateDelete () throws AdministracionException
	{
		JTable table = getListTable();
		int rowSelectedIndex = table.getSelectedRow();
		if (rowSelectedIndex == -1)
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_PERMISSION_TYPE_SELECTION);
		}
		String[] tipoPermiso = Utils.getSelectedRow(table, table.getColumnCount());
		log.info("Validando la eliminacion del tipo de permiso con id: " + tipoPermiso[0]);
		int idTipoPermiso = Converter.stringToInt(tipoPermiso[0]).intValue();
		List<ModuloPerfil> modPerConTipoPermiso = ModuloPerfilDaoHibernate.getInstance().findByIdTipoPermiso(idTipoPermiso);
		if (!Utils.isNullOrEmpty(modPerConTipoPermiso))
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_DATA_CONSTRAINT);
		}
	}

	@Override
	public void delete ()
	{
		String[] tipoPermisoRow = Utils.getSelectedRow(this.manager.getTable(R.TABLES.TIPO_PERMISO), this.manager.getTable(R.TABLES.TIPO_PERMISO).getColumnCount());
		String nombreTipoPermiso = tipoPermisoRow[ProjectValues.COL_TIPO_PER_NOMBRE];
		log.info("Elimino el tipo de permiso: " + nombreTipoPermiso);
		TipoPermiso tipoPermiso = this.tipoPermisoController.findByName(nombreTipoPermiso);
		this.tipoPermisoController.eliminar(tipoPermiso.getCId());
		log.debug("Se eliminó el tipo de permiso");
		clearListGrid();
		fillGrid();
		updateInterface(R.BUTTONS.DELETE);
	}

	@Override
	public void modify ()
	{
		String [] tipoPermisoSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.TIPO_PERMISO), this.manager.getTable(R.TABLES.TIPO_PERMISO).getColumnCount());
		String nombreTipoPermiso = tipoPermisoSelected[ProjectValues.COL_TIPO_PER_NOMBRE];
		String nombre = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String descripcion = (this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION)).getText();
		log.info("Modifico el tipo de permiso: " + tipoPermisoSelected[ProjectValues.COL_TIPO_PER_NOMBRE]);
		TipoPermiso tipoPermiso = this.tipoPermisoController.findByName(nombreTipoPermiso);
		tipoPermiso.setDNombre(nombre);
		tipoPermiso.setDDescripcion(descripcion);
		this.tipoPermisoController.modificar(tipoPermiso);
		updateInterface(R.BUTTONS.OK);
	}

	@Override
	public void setInputValues()
	{
		String [] tipoPermisoSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.TIPO_PERMISO), this.manager.getTable(R.TABLES.TIPO_PERMISO).getColumnCount());
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText(tipoPermisoSelected[ProjectValues.COL_TIPO_PER_NOMBRE]);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setText(tipoPermisoSelected[ProjectValues.COL_TIPO_PER_DESCRIPCION]);
	}

	/**
	 * 
	 * @return {@link List} con todos los {@link TipoPermiso} existentes.
	 */
	public List<TipoPermiso> getAllTipoPermiso ()
	{
		return this.tipoPermisoController.findAll();
	}

	@Override
	public void fillGrid()
	{
		TipoPermisoController controller = this.tipoPermisoController;
		List<TipoPermiso> tipoPermisos = controller.findAll();
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.TIPO_PERMISO).getModel();
		Vector<Object> row;
		TipoPermiso tipoPermiso;
		for (Iterator<TipoPermiso> it = tipoPermisos.iterator(); it.hasNext();)
		{
			tipoPermiso = it.next();
			row = new Vector<Object>();
			row.add(tipoPermiso.getCId());
			row.add(tipoPermiso.getDNombre());
			row.add(tipoPermiso.getDDescripcion());
			model.addRow(row);
		}
	}

	@Override
	public void fillCombos ()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void setOwnControlsVisible()
	{
		this.manager.getButton(R.BUTTONS.NEW).setEnabled(true);
		this.manager.getPanel(R.PANELS.GENERAL).setVisible(true);
		this.manager.getPanel(R.PANELS.CONFIRMACION).setVisible(true);
		this.manager.getPanel(R.PANELS.LISTADO).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS).setVisible(true);
		this.manager.getTable(R.TABLES.TIPO_PERMISO).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.manager.getScrollPane(R.SCROLL_PANES.GRILLA_SCROLL).setViewportView(this.manager.getTable(R.TABLES.TIPO_PERMISO));
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setVisible(true);
		this.manager.getLabel(R.LABELS.LISTADO_TIPO_PERMISO).setVisible(true);
		this.manager.getLabel(R.LABELS.NOMBRE).setVisible(true);
		this.manager.getLabel(R.LABELS.DESCRIPCION).setVisible(true);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setVisible(false);
	}

	@Override
	public JTable getListTable ()
	{
		return this.manager.getTable(R.TABLES.TIPO_PERMISO);
	}

	@Override
	public void doPreAddActions ()
	{

	}
}
