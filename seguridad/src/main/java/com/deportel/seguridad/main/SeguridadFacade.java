package com.deportel.seguridad.main;

import com.deportel.administracion.controller.UsuarioController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.common.utils.EmailSender;
import com.deportel.common.utils.Utils;
import com.deportel.seguridad.exception.InvalidUserAccessException;
import com.deportel.seguridad.exception.InvalidUserCredentialsException;
import com.deportel.seguridad.model.ProjectTexts;
import com.deportel.seguridad.model.ProjectValues;

public class SeguridadFacade
{
	private final UsuarioController	usuarioController;

	private SeguridadFacade()
	{
		this.usuarioController = UsuarioController.getInstance();
	}

	private static SeguridadFacade instance;

	public static SeguridadFacade getInstance ()
	{
		if (instance == null)
		{
			instance = new SeguridadFacade();
		}
		return instance;
	}

	/**
	 * 
	 * @param alias
	 * @param password
	 * @return
	 * @throws InvalidUserCredentialsException
	 */
	public Usuario authenticateUser(String alias, String password) throws InvalidUserCredentialsException
	{
		Usuario user = this.usuarioController.findByAlias(alias);
		if (user == null || !user.validateCredentials(alias, password))
		{
			throw new InvalidUserCredentialsException(Seguridad.getTexts().get(ProjectTexts.ERROR_LOGIN_INVALID_CREDENTIALS));
		}
		return user;
	}

	public void challengeUser (Usuario user, Modulo module, TipoPermiso permissionType) throws InvalidUserAccessException
	{
		for (Perfil perfil : user.getPerfiles())
		{
			for (ModuloPerfil moduloPerfil : perfil.getModulosPerfil())
			{
				if (moduloPerfil.getModulo().equals(module))
				{
					if (moduloPerfil.getTipoPermiso().equals(permissionType))
					{
						return;
					}
				}
			}
		}
		throw new InvalidUserAccessException(Seguridad.getTexts().get(ProjectTexts.ERROR_LOGIN_USER_INSUFICIENT_PERMISSIONS));
	}

	public Usuario recoverPassword (Usuario user, boolean sendToUser)
	{
		String newPass = this.usuarioController.generateNewPassword();
		user.setDPassword(newPass);
		user = this.usuarioController.modificar(user);
		if (sendToUser)
		{
			sendPasswordToUser(user);
		}
		return user;
	}

	public void sendPasswordToUser (Usuario user)
	{
		String emailServerPropFilePath = Seguridad.getProperties().getProperty(ProjectValues.EMAIL_SERVER_CONFIG);
		String subject = Seguridad.getTexts().get(ProjectTexts.PASSWORD_EMAIL_SUBJECT);
		String body = Seguridad.getTexts().get(ProjectTexts.PASSWORD_EMAIL_BODY);
		String userPlaceHolder = Seguridad.getProperties().getProperty(ProjectValues.USER_PLACEHOLDER);
		String passPlaceHolder = Seguridad.getProperties().getProperty(ProjectValues.PASS_PLACEHOLDER);
		body = body.replace(userPlaceHolder, user.getDAlias());
		body = body.replace(passPlaceHolder, user.getDPassword());
		EmailSender es = new EmailSender(Utils.loadProperties(emailServerPropFilePath, this.getClass()));
		es.sendEmail(user.getDEmail(), subject, body);
	}

}
