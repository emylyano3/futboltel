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

import com.deportel.administracion.controller.ModuloController;
import com.deportel.administracion.dao.hibernate.ModuloPerfilDaoHibernate;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.editor.administracion.exceptions.AdministracionException;
import com.deportel.editor.administracion.model.ProjectTexts;
import com.deportel.editor.administracion.model.ProjectValues;
import com.deportel.editor.administracion.view.R;
import com.deportel.guiBuilder.model.GuiManager;


/**
 * @author Emy
 */
public class ModuloGuiController extends AEGuiControllerStrategy
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger			log					= Logger.getLogger(ModuloGuiController.class);
	private final ModuloController		moduloController	= ModuloController.getInstance();

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************
	private static ModuloGuiController	instance;

	private ModuloGuiController(GuiManager manager)
	{
		this.manager = manager;
	}

	public static ModuloGuiController getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new ModuloGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	protected void updateInterface (String source)
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
	protected void setInputControlsEnabled (boolean value)
	{
		this.manager.getButton(R.BUTTONS.OK).setEnabled(value);
		this.manager.getButton(R.BUTTONS.CANCEL).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setEnabled(value);
		this.manager.getLabel(R.LABELS.DESCRIPCION).setEnabled(value);
		this.manager.getLabel(R.LABELS.NOMBRE).setEnabled(value);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setEnabled(value);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).updateUI();
	}

	@Override
	protected void cleanInputControls()
	{
		log.debug("Se limpian los detalles para el editor de usuario");
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
		if (this.moduloController.findByName(nombre) != null) throw new AdministracionException(ProjectTexts.ERROR_CREATE_MODULO_EXIST);
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
		log.info("Creo el Módulo");
		String nombre = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String descripcion = (this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION)).getText();
		try
		{
			Modulo moduloExistente = this.moduloController.findByName(nombre);
			if (moduloExistente != null)
			{
				throw new InvalidParameterException("El nombre del módulo ya existe");
			}
		}
		catch (EntityNotFoundException e)
		{
			log.debug("No se encontró el módulo, continúo con el alta");
		}

		Modulo modulo = new Modulo();
		modulo.setDNombre(nombre);
		modulo.setDDescripcion(descripcion);
		Modulo moduloNuevo = this.moduloController.crear(modulo);
		updateInterface(R.BUTTONS.OK);
		log.info("se creó el módulo: " + moduloNuevo.toString());
	}

	@Override
	public void validateDelete () throws AdministracionException
	{
		JTable table = getListTable();
		int rowSelectedIndex = table.getSelectedRow();
		if (rowSelectedIndex == -1)
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_MODULE_SELECTION);
		}
		String[] moduloSelec = Utils.getSelectedRow(table, table.getColumnCount());
		Integer idModulo = Integer.parseInt(moduloSelec[ProjectValues.COL_MODULO_ID]);
		List<ModuloPerfil> perfilesModulos = ModuloPerfilDaoHibernate.getInstance().findByIdModulo(idModulo);
		if (!Utils.isNullOrEmpty(perfilesModulos))
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_DATA_CONSTRAINT);
		}
	}

	@Override
	public void delete ()
	{
		String[] moduloSelec = Utils.getSelectedRow(this.manager.getTable(R.TABLES.MODULO), this.manager.getTable(R.TABLES.MODULO).getColumnCount());
		Integer id = Integer.parseInt(moduloSelec[ProjectValues.COL_MODULO_ID]);
		log.info("Elimino el modulo: " + id);
		Modulo modulo = this.moduloController.findById(id);
		this.moduloController.eliminar(modulo.getCId());
		log.info("Se eliminó el módulo");
		clearListGrid();
		fillGrid();
		updateInterface(R.BUTTONS.DELETE);
	}

	@Override
	public void modify()
	{
		String [] moduloSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.MODULO), this.manager.getTable(R.TABLES.MODULO).getColumnCount());
		Integer	id = Integer.parseInt(moduloSelected[ProjectValues.COL_MODULO_ID]);

		log.debug("Tomo los nuevos valores");
		String nombre = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String descripcion = (this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION)).getText();
		boolean estado = (this.manager.getCheckBox(R.CHECK_BOXES.ESTADO)).isSelected();

		log.info("Modifico el módulo con id: " + id);
		Modulo modulo = this.moduloController.findById(id);
		modulo.setDNombre(nombre);
		modulo.setDDescripcion(descripcion);
		modulo.setMEstado(estado?Constants.HABILITADO:Constants.DESHABILITADO);
		this.moduloController.modificar(modulo);

		updateInterface(R.BUTTONS.OK);
	}

	@Override
	protected void setInputValues ()
	{
		String [] moduloSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.MODULO), this.manager.getTable(R.TABLES.MODULO).getColumnCount());
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText(moduloSelected[1]);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setText(moduloSelected[2]);
	}

	/**
	 * Se utiliza para completar los datos del combo de Modulo
	 */
	public List<Modulo> getAllModulo ()
	{
		return this.moduloController.findAll();
	}

	@Override
	protected void fillGrid ()
	{
		List<Modulo> modulos = this.moduloController.findAll();
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULO).getModel();
		Vector<Object> row;
		Modulo modulo;
		for (Iterator<Modulo> it = modulos.iterator(); it.hasNext();)
		{
			modulo = it.next();
			row = new Vector<Object>();
			row.add(modulo.getCId());
			row.add(modulo.getDNombre());
			row.add(modulo.getDDescripcion());
			row.add(modulo.getMEstado());
			model.addRow(row);
		}
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
		this.manager.getLabel(R.LABELS.LISTADO_MODULOS).setVisible(true);
		this.manager.getTable(R.TABLES.MODULO).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.manager.getScrollPane(R.SCROLL_PANES.GRILLA_SCROLL).setViewportView(this.manager.getTable(R.TABLES.MODULO));
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setVisible(true);
		this.manager.getLabel(R.LABELS.NOMBRE).setVisible(true);
		this.manager.getLabel(R.LABELS.DESCRIPCION).setVisible(true);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setVisible(true);
	}

	@Override
	protected void fillCombos ()
	{

	}

	@Override
	public JTable getListTable ()
	{
		return this.manager.getTable(R.TABLES.MODULO);
	}

	@Override
	public void doPreAddActions ()
	{

	}
}
