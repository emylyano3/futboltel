package com.deportel.seguridad.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Dialog.ModalityType;

import javax.swing.JComponent;
import javax.swing.JDialog;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

import com.deportel.administracion.controller.UsuarioController;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.seguridad.exception.InvalidUserCredentialsException;
import com.deportel.seguridad.main.Seguridad;
import com.deportel.seguridad.main.SeguridadFacade;
import com.deportel.seguridad.model.ProjectTexts;
import com.deportel.seguridad.model.ProjectValues;

public class PasswordRecoveryWindow
{
	private final JDialog					dialog	= new JDialog();
	private final GuiManager				manager;
	private Usuario							user;
	private static final Logger				log		= Logger.getLogger(PasswordRecoveryWindow.class);

	private static PasswordRecoveryWindow	instance;

	public synchronized static PasswordRecoveryWindow getInstance (GuiManager manager)
	{
		if (instance == null)
		{
			instance = new PasswordRecoveryWindow(manager);
		}
		return instance;
	}

	private PasswordRecoveryWindow (GuiManager manager)
	{
		this.manager = manager;
		arrangeComponents();
		doSettings();
	}

	private void doSettings ()
	{
		GuiUtils.addTitledBorder(this.manager.getPanel(R.PANELS.PR_GENERAL), Seguridad.getTexts().get(ProjectTexts.PASS_RECOVER_PANEL));
		int panelWidth = Integer.parseInt(Seguridad.getProperties().getProperty(ProjectValues.PR_PANEL_WIDTH));
		int panelHeight = Integer.parseInt(Seguridad.getProperties().getProperty(ProjectValues.PR_PANEL_HEIGHT));
		Dimension dimension = new Dimension(panelWidth, panelHeight);
		this.manager.getPanel(R.PANELS.PR_GENERAL).setPreferredSize(dimension);
		int textFieldWidth = Integer.parseInt(Seguridad.getProperties().getProperty(ProjectValues.TF_WIDTH));
		int textFieldHeight = Integer.parseInt(Seguridad.getProperties().getProperty(ProjectValues.TF_HEIGHT));
		dimension = new Dimension(textFieldWidth, textFieldHeight);
		this.manager.getTextField(R.TEXT_FIELDS.PR_ALIAS).setPreferredSize(dimension);
	}

	private void arrangeComponents ()
	{
		GroupLayout gl = new GroupLayout(this.dialog.getContentPane());
		this.dialog.getContentPane().setLayout(gl);
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup(gl.createParallelGroup().add(this.manager.getPanel(R.PANELS.PR_GENERAL)));
		gl.setHorizontalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.PR_GENERAL)));

		gl = new GroupLayout(this.manager.getPanel(R.PANELS.PR_GENERAL));
		this.manager.getPanel(R.PANELS.PR_GENERAL).setLayout(gl);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.PR_INPUT))
				.add(this.manager.getPanel(R.PANELS.PR_CONFIRM))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup(GroupLayout.CENTER)
				.add(this.manager.getPanel(R.PANELS.PR_INPUT))
				.add(this.manager.getPanel(R.PANELS.PR_CONFIRM))
		);
		arrangeInputPanel();
		arrangeConfirmPanel();
	}

	private void arrangeInputPanel ()
	{
		GroupLayout gl = new GroupLayout(this.manager.getPanel(R.PANELS.PR_INPUT));
		this.manager.getPanel(R.PANELS.PR_INPUT).setLayout(gl);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getLabel(R.LABELS.PR_ALIAS))
				.add(this.manager.getTextField(R.TEXT_FIELDS.PR_ALIAS))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getLabel(R.LABELS.PR_ALIAS))
				.add(this.manager.getTextField(R.TEXT_FIELDS.PR_ALIAS))
		);
		gl.linkSize(new JComponent[] {this.manager.getLabel(R.LABELS.PR_ALIAS), this.manager.getTextField(R.TEXT_FIELDS.PR_ALIAS)}, GroupLayout.HORIZONTAL);
		gl.linkSize(new JComponent[] {this.manager.getLabel(R.LABELS.PR_ALIAS), this.manager.getTextField(R.TEXT_FIELDS.PR_ALIAS)}, GroupLayout.VERTICAL);
	}

	private void arrangeConfirmPanel ()
	{
		GroupLayout gl = new GroupLayout(this.manager.getPanel(R.PANELS.PR_CONFIRM));
		this.manager.getPanel(R.PANELS.PR_CONFIRM).setLayout(gl);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createParallelGroup(GroupLayout.CENTER)
				.add(this.manager.getButton(R.BUTTONS.PR_CONFIRM))
		);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getButton(R.BUTTONS.PR_CONFIRM))
		);
	}

	/**
	 * 
	 * @throws InvalidUserCredentialsException
	 */
	private void validateAlias () throws InvalidUserCredentialsException
	{
		this.user = UsuarioController.getInstance().findByAlias(this.manager.getTextField(R.TEXT_FIELDS.PR_ALIAS).getText());
		if (this.user == null)
		{
			throw new InvalidUserCredentialsException(Seguridad.getTexts().get(ProjectTexts.ERROR_LOGIN_INVALID_CREDENTIALS));
		}
	}

	private void sendPassword ()
	{
		log.debug("Se envia la contraseña al usuario");
		this.user = SeguridadFacade.getInstance().recoverPassword(this.user, true);
		Popup.getInstance
		(
				Seguridad.getTexts().get(ProjectTexts.MSG_PASSWORD_RECOVERED_SENT),
				Popup.POPUP_INFO,
				this.dialog
		)
		.showGui();
	}

	public void recoverPassword ()
	{
		try
		{
			validateAlias();
			sendPassword();
			this.dialog.dispose();
		}
		catch (Exception e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR, this.dialog).showGui();
		}
	}

	public void showDialog (Container parent)
	{
		this.dialog.setLocationRelativeTo(parent);
		this.dialog.setModalityType(ModalityType.MODELESS);
		this.dialog.setAlwaysOnTop(true);
		this.dialog.setResizable(false);
		this.dialog.setVisible(true);
		this.dialog.toFront();
		this.dialog.pack();
	}
}
