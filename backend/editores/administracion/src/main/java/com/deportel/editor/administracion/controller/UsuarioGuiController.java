package com.deportel.editor.administracion.controller;

import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.deportel.administracion.controller.PerfilController;
import com.deportel.administracion.controller.UsuarioController;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.common.Constants;
import com.deportel.common.utils.EmailSender;
import com.deportel.common.utils.Utils;
import com.deportel.editor.administracion.exceptions.AdministracionException;
import com.deportel.editor.administracion.main.AdministracionEditor;
import com.deportel.editor.administracion.model.ProjectTexts;
import com.deportel.editor.administracion.model.ProjectValues;
import com.deportel.editor.administracion.view.R;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class UsuarioGuiController extends AEGuiControllerStrategy
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger		log					= Logger.getLogger(UsuarioGuiController.class);

	private final UsuarioController	usuarioController	= UsuarioController.getInstance();

	private final PerfilController	perfilController	= PerfilController.getInstance();

	// *********************************************************************************************************************
	// Constructor singleton
	// *********************************************************************************************************************

	private UsuarioGuiController(GuiManager manager)
	{
		this.manager = manager;
	}

	private static UsuarioGuiController	instance;

	public static UsuarioGuiController getInstance (GuiManager manager)
	{
		if (instance == null)
		{
			instance = new UsuarioGuiController(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos publicos
	// *********************************************************************************************************************

	@Override
	protected void updateInterface (String source)
	{
		log.debug("Actualizando la interfaz del editor de usuario");
		if (R.BUTTONS.DELETE.equals(source))
		{
			setInputControlsEnabled(false);
		}
		else if (R.BUTTONS.OK.equals(source))
		{
			cleanInputControls();
			setInputControlsEnabled(false);
			clearGrid(R.TABLES.PERFILES_USUARIO);
			clearListGrid();
			fillGrid();
		}
	}

	@Override
	protected void fillGrid ()
	{
		log.debug("Se llena la grilla para el editor de usuario");
		UsuarioController controller = this.usuarioController;
		List<Usuario> usuarios = controller.findAll();
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.USUARIO).getModel();
		Vector<Object> row;
		for (Usuario usuario : usuarios)
		{
			row = new Vector<Object>();
			row.add(usuario.getCId());
			row.add(usuario.getDAlias());
			row.add(usuario.getDNombre());
			row.add(usuario.getDApellido());
			row.add(usuario.getMEstado());
			model.addRow(row);
		}
	}

	@Override
	protected void cleanInputControls ()
	{
		log.debug("Se limpian los detalles para el editor de usuario");
		this.manager.getTextField(R.TEXT_FIELDS.ALIAS).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.EMAIL).setText("");
		this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL).setText("");
		clearGrid(R.TABLES.PERFILES_USUARIO);
	}

	@Override
	protected void setInputControlsEnabled (boolean value)
	{
		this.manager.getButton(R.BUTTONS.OK).setEnabled(value);
		this.manager.getButton(R.BUTTONS.CANCEL).setEnabled(value);
		this.manager.getButton(R.BUTTONS.ADD_PERFIL).setEnabled(value);
		this.manager.getButton(R.BUTTONS.GENERATE_PASSWORD).setEnabled(value);
		this.manager.getButton(R.BUTTONS.DEL_PERFIL).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.ALIAS).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.EMAIL).setEnabled(value);
		this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL).setEnabled(value);
		this.manager.getLabel(R.LABELS.ALIAS).setEnabled(value);
		this.manager.getLabel(R.LABELS.NOMBRE).setEnabled(value);
		this.manager.getLabel(R.LABELS.APELLIDO).setEnabled(value);
		this.manager.getLabel(R.LABELS.EMAIL).setEnabled(value);
		this.manager.getLabel(R.LABELS.CONFIRM_EMAIL).setEnabled(value);
		this.manager.getTable(R.TABLES.PERFILES_USUARIO).setEnabled(value);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setEnabled(value);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).updateUI();
	}

	@Override
	public void add ()
	{
		log.info("Ejecutando la action add para el editor de usuario");
		String alias = (this.manager.getTextField(R.TEXT_FIELDS.ALIAS)).getText();
		String nombre = (this.manager.getTextField(R.TEXT_FIELDS.NOMBRE)).getText();
		String apellido = (this.manager.getTextField(R.TEXT_FIELDS.APELLIDO)).getText();
		String password = (this.manager.getTextField(R.TEXT_FIELDS.PASSWORD)).getText();
		String email = (this.manager.getTextField(R.TEXT_FIELDS.EMAIL)).getText();
		boolean enabled = this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).isSelected();
		String estado = enabled ? Constants.HABILITADO : Constants.DESHABILITADO;

		Usuario user = new Usuario();
		user.setDAlias(alias);
		user.setDNombre(nombre);
		user.setDApellido(apellido);
		user.setDEmail(email);
		user.setDPassword(password);
		//		user.setDPassword(Encoder.encode(password));
		user.setMEstado(estado);

		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		Perfil perfil;
		user.getPerfiles().clear();
		for (Vector<?> vector : data)
		{
			perfil = (Perfil) vector.get(0);
			user.getPerfiles().add(perfil);
		}
		user = this.usuarioController.crear(user);
		log.info("Se creó el usuario: " + user);
		sendPasswordToUser(user);
		log.info("Se envió la contraseña al usuario");
		updateInterface(R.BUTTONS.OK);
	}

	private void sendPasswordToUser(Usuario user)
	{
		String emailServerPropFilePath = AdministracionEditor.getProperties().getProperty(ProjectValues.EMAIL_SERVER_CONFIG);
		String subject = AdministracionEditor.getTexts().get(ProjectTexts.PASSWORD_EMAIL_SUBJECT);
		String body = AdministracionEditor.getTexts().get(ProjectTexts.PASSWORD_EMAIL_BODY);
		String userPlaceHolder = AdministracionEditor.getProperties().getProperty(ProjectValues.USER_PLACEHOLDER);
		String passPlaceHolder = AdministracionEditor.getProperties().getProperty(ProjectValues.PASS_PLACEHOLDER);
		body = body.replace(userPlaceHolder, user.getDAlias());
		body = body.replace(passPlaceHolder, user.getDPassword());
		EmailSender es = new EmailSender(Utils.loadProperties(emailServerPropFilePath, this.getClass()));
		es.sendEmail(user.getDEmail(), subject, body);
	}

	@Override
	public void validateDelete () throws AdministracionException
	{
		int rowSelectedIndex = this.manager.getTable(R.TABLES.USUARIO).getSelectedRow();
		if (rowSelectedIndex == -1)
		{
			throw new AdministracionException(ProjectTexts.ERROR_REMOVE_USER_SELECTION);
		}
	}

	@Override
	public void delete ()
	{
		JTable table = this.manager.getTable(R.TABLES.USUARIO);
		String[] usuarioRow = Utils.getSelectedRow(table, table.getColumnCount());
		String aliasUsuario = usuarioRow[ProjectValues.COL_USUARIO_ALIAS];
		log.info("Se va a eliminar el usuario: " + aliasUsuario);
		Usuario user = this.usuarioController.findByAlias(aliasUsuario);
		this.usuarioController.eliminar(user.getCId());
		log.debug("Se eliminó el usuario");
		updateInterface(R.BUTTONS.OK);
	}

	@Override
	public void modify ()
	{
		log.info("Ejecutando la action modify para el editor de usuario");
		Usuario usuario = getSelectedUser();
		boolean notifyUser = updateUserWithInputs(usuario);
		usuario = this.usuarioController.modificar(usuario);
		log.info("se modificó el usuario: " + usuario);
		if (notifyUser)
		{
			sendPasswordToUser(usuario);
		}
		updateInterface(R.BUTTONS.OK);
	}

	/**
	 * Actualiza los atributos del usuario que esta siendo editado con los valores de input ingresados por el
	 * usuario. Si detecta un cambio en la contraseña, devuelve true para indicar que se debe notificar al
	 * usuario el cambio.
	 * 
	 * @param usuario
	 *            El usuario que esta siendo modificado actualmente.
	 */
	private boolean updateUserWithInputs (Usuario usuario)
	{
		boolean enabled = this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).isSelected();
		String estado = enabled ? Constants.HABILITADO : Constants.DESHABILITADO;
		//		String inputPass = Encoder.encode(this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).getText());
		String inputPass = this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).getText();
		boolean notifyUser = !usuario.getDPassword().equals(inputPass);
		usuario.setDPassword(inputPass);
		usuario.setDAlias(this.manager.getTextField(R.TEXT_FIELDS.ALIAS).getText());
		usuario.setDNombre(this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText());
		usuario.setDApellido(this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).getText());
		usuario.setDEmail(this.manager.getTextField(R.TEXT_FIELDS.EMAIL).getText());
		usuario.setMEstado(estado);
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		usuario.getPerfiles().clear();
		Perfil perfil;
		for (Vector<?> vector : data)
		{
			perfil = (Perfil) vector.get(0);
			usuario.getPerfiles().add(perfil);
		}
		return notifyUser;
	}

	/**
	 * Devuelve el usuario que esta seleccionado en la grilla de seleccion.
	 * 
	 * @return el usuario que esta seleccionado en la grilla de seleccion.
	 */
	private Usuario getSelectedUser ()
	{
		String[] usuarioSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.USUARIO), this.manager.getTable(R.TABLES.USUARIO).getColumnCount());
		String alias = usuarioSelected[1];
		Usuario usuario = this.usuarioController.findByAlias(alias);
		return usuario;
	}

	@Override
	protected void fillCombos ()
	{
		log.debug("Se llenan los combos para el editor de usuario");
		this.manager.getBindedCombo(R.BINDED_COMBOS.PERFIL).bindWithList
		(
				this.perfilController.findAll(), Perfil.FIELD_NAME
		);
	}

	@Override
	protected void setInputValues ()
	{
		log.debug("Se setean los valores para el editor de usuario");
		String[] usuarioSelected = Utils.getSelectedRow(this.manager.getTable(R.TABLES.USUARIO), this.manager.getTable(R.TABLES.USUARIO).getColumnCount());
		String id = usuarioSelected[0];
		Usuario user = this.usuarioController.findById(Integer.parseInt(id));
		this.manager.getTextField(R.TEXT_FIELDS.ALIAS).setText(user.getDAlias());
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setText(user.getDNombre());
		this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).setText(user.getDApellido());
		this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).setText(user.getDPassword());
		this.manager.getTextField(R.TEXT_FIELDS.EMAIL).setText(user.getDEmail());
		this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL).setText(user.getDEmail());
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		Vector<Object> row;
		for (Perfil perfil : user.getPerfiles())
		{
			row = new Vector<Object>();
			row.add(perfil);
			model.addRow(row);
		}
	}

	@Override
	public void onListGridSlectionChange ()
	{
		String[] userSelected = Utils.getSelectedRow
		(
				this.manager.getTable(R.TABLES.USUARIO),
				this.manager.getTable(R.TABLES.USUARIO).getColumnCount()
		);
		if(userSelected != null)
		{
			Usuario user = this.usuarioController.findByAlias(userSelected[ProjectValues.COL_USUARIO_ALIAS]);
			DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.USER_CROSS_DATA).getModel();
			clearGrid(R.TABLES.USER_CROSS_DATA);
			Vector<Object> row;
			for (Perfil perfil : user.getPerfiles())
			{
				row = new Vector<Object>();
				row.add(perfil.getDNombre());
				model.addRow(row);
			}
			this.manager.getTable(R.TABLES.USER_CROSS_DATA).updateUI();
		}
	}

	/**
	 * 
	 */
	public void addProfile ()
	{
		log.info("Se agrego un nuevo perfil al usuario");
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		Vector<Object> row = new Vector<Object>();
		model.addRow(row);
	}

	/**
	 * 
	 */
	public void delProfile ()
	{
		log.info("Se elimino un perfil del usuario");
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		int rowIndex = this.manager.getTable(R.TABLES.PERFILES_USUARIO).getSelectedRow();
		if (rowIndex != -1)
		{
			model.removeRow(rowIndex);
		}
	}

	/**
	 * 
	 */
	public void generateNewPassword ()
	{
		this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).setText(this.usuarioController.generateNewPassword());
	}

	@Override
	protected void setOwnControlsVisible()
	{
		this.manager.getPanel(R.PANELS.GENERAL).setVisible(true);
		this.manager.getButton(R.BUTTONS.NEW).setEnabled(true);
		this.manager.getButton(R.BUTTONS.ADD_PERFIL).setVisible(true);
		this.manager.getButton(R.BUTTONS.DEL_PERFIL).setVisible(true);
		this.manager.getButton(R.BUTTONS.GENERATE_PASSWORD).setVisible(true);
		this.manager.getPanel(R.PANELS.CONFIRMACION).setVisible(true);
		this.manager.getPanel(R.PANELS.LISTADO).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_GENERAL).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_CAMPOS).setVisible(true);
		this.manager.getPanel(R.PANELS.INGRESO_DATOS_TABLA).setVisible(true);
		this.manager.getPanel(R.PANELS.CROSS_DATA).setVisible(true);
		this.manager.getTable(R.TABLES.USUARIO).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.manager.getScrollPane(R.SCROLL_PANES.GRILLA_SCROLL).setViewportView(this.manager.getTable(R.TABLES.USUARIO));
		this.manager.getScrollPane(R.SCROLL_PANES.INPUT_GRID_SCROLL).setViewportView(this.manager.getTable(R.TABLES.PERFILES_USUARIO));
		this.manager.getScrollPane(R.SCROLL_PANES.CROSS_DATA).setViewportView(this.manager.getTable(R.TABLES.USER_CROSS_DATA));
		this.manager.getTextField(R.TEXT_FIELDS.ALIAS).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.EMAIL).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL).setVisible(true);
		this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).setVisible(true);
		this.manager.getLabel(R.LABELS.LISTADO_USUARIOS).setVisible(true);
		this.manager.getLabel(R.LABELS.ALIAS).setVisible(true);
		this.manager.getLabel(R.LABELS.NOMBRE).setVisible(true);
		this.manager.getLabel(R.LABELS.APELLIDO).setVisible(true);
		this.manager.getLabel(R.LABELS.CONFIRM_EMAIL).setVisible(true);
		this.manager.getLabel(R.LABELS.EMAIL).setVisible(true);
		this.manager.getLabel(R.LABELS.PERFILES).setVisible(true);
		this.manager.getTable(R.TABLES.USER_CROSS_DATA).setEnabled(true);
		this.manager.getCheckBox(R.CHECK_BOXES.ESTADO).setVisible(true);
	}

	@Override
	public JTable getListTable ()
	{
		return this.manager.getTable(R.TABLES.USUARIO);
	}

	@Override
	public void validateAdd ()
	{
		String alias = this.manager.getTextField(R.TEXT_FIELDS.ALIAS).getText();
		String pass = this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).getText();
		String name = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText();
		String email = this.manager.getTextField(R.TEXT_FIELDS.EMAIL).getText();
		String confirmedEmail = this.manager.getTextField(R.TEXT_FIELDS.CONFIRM_EMAIL).getText();
		String surname = this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).getText();
		if (Utils.isNullOrEmpty(alias)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_USER_ALIAS_NULL);
		if (Utils.isNullOrEmpty(pass)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_PASSWORD_REQUIRED);
		if (Utils.isNullOrEmpty(email)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_EMAIL_REQUIRED);
		if (!email.equals(confirmedEmail)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_EMAIL_NOT_MATCH);
		if (!Utils.validateAlphaNumeric(alias, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_ALIAS);
		if (!Utils.validateEmail(email)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_EMAIL);
		if (!Utils.validateAlphaNumeric(pass, 12)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_PASS);
		if (!Utils.validateAlphaNumeric(name, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_NAME);
		if (!Utils.validateAlphaNumeric(surname, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_SURNAME);
		if (this.usuarioController.findByAlias(alias) != null) throw new AdministracionException(ProjectTexts.ERROR_CREATE_USER_ALIAS_EXIST);
		if (this.usuarioController.findByPassword(pass) != null) throw new AdministracionException(ProjectTexts.ERROR_CREATE_USER_PASSWORD_EXIST);
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		for (Vector<?> vector : data)
		{
			if (vector.get(0) == null) throw new AdministracionException(ProjectTexts.ERROR_CREATE_PROFILE_NULL);
		}
	}

	@Override
	public void validateModify ()
	{
		String alias = this.manager.getTextField(R.TEXT_FIELDS.ALIAS).getText();
		String pass = this.manager.getTextField(R.TEXT_FIELDS.PASSWORD).getText();
		String name = this.manager.getTextField(R.TEXT_FIELDS.NOMBRE).getText();
		String email = this.manager.getTextField(R.TEXT_FIELDS.EMAIL).getText();
		String surname = this.manager.getTextField(R.TEXT_FIELDS.APELLIDO).getText();
		if (Utils.isNullOrEmpty(alias)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_USER_ALIAS_NULL);
		if (Utils.isNullOrEmpty(pass)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_PASSWORD_REQUIRED);
		if (Utils.isNullOrEmpty(email)) throw new AdministracionException(ProjectTexts.ERROR_CREATE_EMAIL_REQUIRED);
		if (!Utils.validateEmail(email)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_EMAIL);
		if (!Utils.validateAlphaNumeric(alias, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_ALIAS);
		if (!Utils.validateAlphaNumeric(name, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_NAME);
		if (!Utils.validateAlphaNumeric(surname, 45)) throw new AdministracionException(ProjectTexts.ERROR_INVALID_SURNAME);

		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.PERFILES_USUARIO).getModel();
		@SuppressWarnings("unchecked")
		Vector<Vector<?>> data = model.getDataVector();
		for (Vector<?> vector : data)
		{
			if (vector.get(0) == null) throw new AdministracionException(ProjectTexts.ERROR_CREATE_PROFILE_NULL);
		}
	}

	@Override
	public void doPreAddActions ()
	{
		generateNewPassword();
	}
}
