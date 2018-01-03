package com.deportel.editor.administracion.controller;

import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.deportel.administracion.controller.PerfilController;
import com.deportel.administracion.dao.hibernate.PerfilUsuarioDaoHibernate;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.PerfilUsuario;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.common.Constants;
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
public class PerfilGuiController extends AEGuiControllerStrategy
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger			log					= Logger.getLogger(PerfilGuiController.class);
	private final PerfilController		perfilController	= PerfilController.getInstance();

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static PerfilGuiController	instance;

	private PerfilGuiController(GuiManager manager)
	{
		this.manager = manager;
	}

	public static PerfilGuiController getInstance (GuiManager manager)
	{
		if (instance == null)
		{
			instance = new PerfilGuiController(manager);
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
			clearGrid(R.TABLES.MODULOS_PERFIL);
			clearListGrid();
			fillGrid();
		}
	}

	@Override
	protected void setInputControlsEnabled (boolean value)
	{
		log.debug("enableInputControls");
		this.manager.getButton(R.BUTTONS.OK).setEnabled(value);
		this.manager.getButton(R.BUTTONS.CANCEL).setEnabled(value);
		this.manager.getButton(R.BUTTONS.ADD_MODULE).setEnabled(value);
		this.manager.getButton(R.BUTTONS.DEL_MODULE).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setEnabled(value);
		this.manager.getLabel(R.LABELS.DESCRIPCION).setEnabled(value);
		this.manager.getLabel(R.LABELS.NOMBRE).setEnabled(value);
		this.manager.getTable(R.TABLES.MODULOS_PERFIL).setEnabled(value);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setEnabled(value);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).updateUI();
	}

	@Override
	protected void cleanInputControls ()
	{
		log.debug("cleanInputControls");
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText("");
		clearGrid(R.TABLES.MODULOS_PERFIL);
	}

	@Override
	public void validateAdd ()
	{
		String nombre = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText();
		String desc = this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).getText();
		if (Utils.isNullOrEmpty(nombre)) throw new AdministracionException(ProjectTexts.ERROR_NAME_NULL);
		if (!Utils.validateAlphaNumeric(nombre, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_NAME);
		if (!Utils.validateAlphaNumeric(desc, 200)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_DESC);
		if (this.perfilController.findByName(nombre) != null) throw new AdministracionException(ProjectTexts.ERROR_CREATE_PROFILE_EXIST);

		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULOS_PERFIL).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		for (Vector<?> rowData : data)
		{
			if (rowData.get(0) == null) throw new AdministracionException(ProjectTexts.ERROR_MODULE_NULL);
			if (rowData.get(1) == null) throw new AdministracionException(ProjectTexts.ERROR_PERMISSION_TYPE_NULL);
		}
	}

	@Override
	public void validateModify ()
	{
		String nombre = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText();
		String desc = this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).getText();
		if (Utils.isNullOrEmpty(nombre)) throw new AdministracionException(ProjectTexts.ERROR_NAME_NULL);
		if (!Utils.validateAlphaNumeric(nombre, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_NAME);
		if (!Utils.validateAlphaNumeric(desc, 200)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_DESC);

		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULOS_PERFIL).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		for (Vector<?> rowData : data)
		{
			if (rowData.get(0) == null) throw new AdministracionException(ProjectTexts.ERROR_MODULE_NULL);
			if (rowData.get(1) == null) throw new AdministracionException(ProjectTexts.ERROR_PERMISSION_TYPE_NULL);
		}
	}

	@Override
	public void add ()
	{
		log.info("Creando un nuevo perfil");
		String nombre = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String descripcion = (this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION)).getText();
		boolean enabled = this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).isSelected();
		String estado = enabled ? Constants.HABILITADO : Constants.DESHABILITADO;

		Perfil perfil = new Perfil();
		perfil.setDNombre(nombre);
		perfil.setDDescripcion(descripcion);
		perfil.setMEstado(estado);

		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULOS_PERFIL).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		Modulo modulo;
		TipoPermiso tipoPermiso;
		ModuloPerfil moduloPerfil;
		perfil.getModulosPerfil().clear();
		for (Vector<?> vector : data)
		{
			modulo = (Modulo) vector.get(0);
			tipoPermiso = (TipoPermiso) vector.get(1);
			moduloPerfil = new ModuloPerfil();
			moduloPerfil.setModulo(modulo);
			moduloPerfil.setTipoPermiso(tipoPermiso);
			perfil.getModulosPerfil().add(moduloPerfil);
		}
		Perfil perfilNuevo = this.perfilController.crear(perfil);
		log.info("se creó el perfil: " + perfilNuevo);
		updateInterface(R.BUTTONS.OK);
	}

	@Override
	public void validateDelete () throws AdministracionException
	{
		JTable table = getListTable();
		int rowSelectedIndex = table.getSelectedRow();
		if (rowSelectedIndex == -1)
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_PROFILE_SELECTION);
		}
		String[] perfil = Utils.getSelectedRow(table, table.getColumnCount());
		log.info("Validando la eliminacion del perfil con id: " + perfil[0]);
		int idPerfil = Converter.stringToInt(perfil[0]).intValue();
		List<PerfilUsuario> usuariosPerfiles = PerfilUsuarioDaoHibernate.getInstance().findByIdPerfil(idPerfil);
		if (!Utils.isNullOrEmpty(usuariosPerfiles))
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_DATA_CONSTRAINT);
		}
	}

	@Override
	public void delete ()
	{
		String[] perfil = Utils.getSelectedRow(this.manager.getTable(R.TABLES.PERFIL), this.manager.getTable(R.TABLES.PERFIL).getColumnCount());
		log.info("Elimino el perfil: " + perfil[0]);
		int idPerfil = Converter.stringToInt(perfil[0]).intValue();
		this.perfilController.eliminar(idPerfil);
		log.info("Se eliminó el perfil");
		updateInterface(R.BUTTONS.OK);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void modify ()
	{
		log.info("Se modifica el perfil");
		String[] perfilSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.PERFIL), this.manager.getTable(R.TABLES.PERFIL).getColumnCount());
		String nombrePerfil = perfilSelected[1];
		String inputName = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String inputDesc = (this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION)).getText();

		Perfil perfil = this.perfilController.findByName(nombrePerfil);
		perfil.setDNombre(inputName);
		perfil.setDDescripcion(inputDesc);

		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULOS_PERFIL).getModel();
		Vector<Vector<?>> data = model.getDataVector();
		Modulo modulo;
		TipoPermiso tipoPermiso;
		ModuloPerfil moduloPerfil;
		perfil.getModulosPerfil().clear();
		for (Vector<?> vector : data)
		{
			modulo = (Modulo) vector.get(0);
			tipoPermiso = (TipoPermiso) vector.get(1);
			moduloPerfil = new ModuloPerfil();
			moduloPerfil.setModulo(modulo);
			moduloPerfil.setTipoPermiso(tipoPermiso);
			perfil.getModulosPerfil().add(moduloPerfil);
		}
		perfil = this.perfilController.modificar(perfil);
		log.info("se modificó el perfil: " + perfil);
		updateInterface(R.BUTTONS.OK);
	}

	@Override
	protected void setInputValues ()
	{
		String[] perfilSelected = Utils.getSelectedRow
		(
				this.manager.getTable(R.TABLES.PERFIL),
				this.manager.getTable(R.TABLES.PERFIL).getColumnCount()
		);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText(perfilSelected[1]);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setText(perfilSelected[2]);
		Perfil perfil = this.perfilController.findByName(perfilSelected[1]);
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULOS_PERFIL).getModel();
		Vector<Object> row;
		for (ModuloPerfil moduloPerfil : perfil.getModulosPerfil())
		{
			row = new Vector<Object>();
			row.add(moduloPerfil.getModulo());
			row.add(moduloPerfil.getTipoPermiso());
			model.addRow(row);
		}
	}

	@Override
	protected void fillCombos ()
	{
		log.debug("fillCombos");
		this.manager.getBindedCombo(R.BINDED_COMBOS.TIPO_PERMISO).bindWithList
		(
				TipoPermisoGuiController.getInstance(this.manager).getAllTipoPermiso(), TipoPermiso.FIELD_NAME
		);
		this.manager.getBindedCombo(R.BINDED_COMBOS.MODULO).bindWithList
		(
				ModuloGuiController.getInstance(this.manager).getAllModulo(), Modulo.FIELD_NAME
		);
	}

	@Override
	protected void fillGrid ()
	{
		log.debug("fillGrid");
		List<Perfil> perfiles = this.perfilController.findAll();
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFIL).getModel();
		Vector<Object> row;
		for (Perfil perfil : perfiles)
		{
			row = new Vector<Object>();
			row.add(perfil.getCId());
			row.add(perfil.getDNombre());
			row.add(perfil.getDDescripcion());
			row.add(perfil.getMEstado());
			model.addRow(row);
		}
	}

	@Override
	protected void setOwnControlsVisible()
	{
		this.manager.getPanel(R.PANELS.GENERAL).setVisible(true);
		this.manager.getButton(R.BUTTONS.NEW).setEnabled(true);
		this.manager.getButton(R.BUTTONS.ADD_MODULE).setVisible(true);
		this.manager.getButton(R.BUTTONS.DEL_MODULE).setVisible(true);
		this.manager.getPanel(R.PANELS.CONFIRMACION).setVisible(true);
		this.manager.getPanel(R.PANELS.LISTADO).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA).setVisible(true);
		this.manager.getPanel(R.PANELS.CROSS_DATA).setVisible(true);
		this.manager.getTable(R.TABLES.PERFIL).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.manager.getTable(R.TABLES.MODULOS_PERFIL).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.manager.getTable(R.TABLES.PERFIL_CROSS_DATA).setEnabled(true);
		this.manager.getScrollPane(R.SCROLL_PANES.GRILLA_SCROLL).setViewportView(this.manager.getTable(R.TABLES.PERFIL));
		this.manager.getScrollPane(R.SCROLL_PANES.INPUT_GRID_SCROLL).setViewportView(this.manager.getTable(R.TABLES.MODULOS_PERFIL));
		this.manager.getScrollPane(R.SCROLL_PANES.CROSS_DATA).setViewportView(this.manager.getTable(R.TABLES.PERFIL_CROSS_DATA));
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.DESCRIPCION).setVisible(true);
		this.manager.getLabel(R.LABELS.LISTADO_PERFILES).setVisible(true);
		this.manager.getLabel(R.LABELS.NOMBRE).setVisible(true);
		this.manager.getLabel(R.LABELS.DESCRIPCION).setVisible(true);
		this.manager.getLabel(R.LABELS.MODULOS).setVisible(true);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setVisible(true);
	}

	@Override
	public void onListGridSlectionChange ()
	{
		String[] perfilSelected = Utils.getSelectedRow
		(
				this.manager.getTable(R.TABLES.PERFIL),
				this.manager.getTable(R.TABLES.PERFIL).getColumnCount()
		);
		if (perfilSelected != null)
		{
			Perfil perfil = this.perfilController.findByName(perfilSelected[ProjectValues.COL_PERFIL_NOMBRE]);
			DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFIL_CROSS_DATA).getModel();
			clearGrid(R.TABLES.PERFIL_CROSS_DATA);
			Vector<Object> row;
			for (ModuloPerfil moduloPerfil : perfil.getModulosPerfil())
			{
				row = new Vector<Object>();
				row.add(moduloPerfil.getModulo().getDNombre());
				row.add(moduloPerfil.getTipoPermiso().getDNombre());
				model.addRow(row);
			}
			this.manager.getTable(R.TABLES.PERFIL_CROSS_DATA).updateUI();
		}
	}

	public void addModule ()
	{
		log.info("Se agrego un nuevo modulo para el perfil");
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.MODULOS_PERFIL).getModel();
		Vector<Object> row = new Vector<Object>();
		model.addRow(row);
	}

	public void delModule ()
	{
		log.info("Se elimino un modulo para el perfil");
		JTable table = this.manager.getTable(R.TABLES.MODULOS_PERFIL);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowIndex = this.manager.getTable(R.TABLES.MODULOS_PERFIL).getSelectedRow();
		if (rowIndex != -1)
		{
			model.removeRow(rowIndex);
		}
	}

	@Override
	public JTable getListTable ()
	{
		return this.manager.getTable(R.TABLES.PERFIL);
	}

	@Override
	public void doPreAddActions ()
	{

	}
}
