package com.deportel.server.service.form.strategies;

import static com.deportel.server.service.form.Form.Fields.*;
import static com.deportel.server.web.Resources.Texts.*;
import static com.deportel.server.web.Resources.Constants.*;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.ClienteController;
import com.deportel.componentes.controller.SuscripcionController;
import com.deportel.componentes.controller.TemaController;
import com.deportel.componentes.modelo.Cliente;
import com.deportel.componentes.modelo.Suscripcion;
import com.deportel.componentes.modelo.Tema;
import com.deportel.server.service.form.exception.FormServiceException;

/**
 * @author EMY
 */
public class MobileSuscriptionFormStrategy extends MobileFormManagerStrategy
{
	protected static final Logger		log						= Logger.getLogger(MobileSuscriptionFormStrategy.class);
	private final ClienteController		clienteController		= ClienteController.getInstance(false);
	private final SuscripcionController	suscripcionController	= SuscripcionController.getInstance(false);
	private final TemaController		temaController			= TemaController.getInstance(false);
	private Cliente						cliente;
	private boolean						isNewUser				= false;

	private final String				FORM_TEMPLATE_FILE		= "/config/suscription/form.properties";

	private String						name;
	private String						cel;
	private String						email;

	public MobileSuscriptionFormStrategy()
	{
		super();
	}

	@Override
	public void doLoad (HttpServletRequest request) throws HibernateException
	{
		log.info("Realizando la carga del formulario de suscripcion enviado");
		/*
		 * Datos generales de conexion
		 */
		this.userId = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_USER_ID));
		this.application = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME));
		this.customization = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_APP_CUSTOMIZATION));
		/*
		 * Datos del formulario
		 */
		this.name = (String) request.getAttribute(this.formTemplate.getField(NAME));
		this.cel = (String) request.getAttribute(this.formTemplate.getField(CELULAR));
		this.email = (String) request.getAttribute(this.formTemplate.getField(EMAIL));
	}

	@Override
	protected void validateFormData () throws FormServiceException
	{
		if (!Utils.validateEmail(this.email))
		{
			throw new FormServiceException(VAL_ERROR_EMAIL);
		}
		if (!Utils.isNullOrEmpty(this.cel) && (!Utils.validateIntegerNumber(this.cel, 10) || this.cel.length() < 10))
		{
			throw new FormServiceException(VAL_ERROR_CEL_NUMBER);
		}
		if (Utils.isNullOrEmpty(this.name))
		{
			throw new FormServiceException(VAL_ERROR_NULL_NAME);
		}
	}

	@Override
	protected void doStuff () throws FormServiceException
	{
		log.info("Creando la suscripcion del usuario");
		try
		{
			findOrCreateClient();
			Tema tema = this.temaController.findByName(this.application);
			Suscripcion suscripcion = new Suscripcion(this.cliente, tema, this.customization, this.email, this.cel);
			Date fechaSusc = new Timestamp(System.currentTimeMillis());
			suscripcion.setFechaSuscripcion(fechaSusc);
			suscripcion.setEstado(Constants.HABILITADO);
			suscripcion = this.suscripcionController.create(suscripcion);
		}
		catch (HibernateException e)
		{
			throw new FormServiceException(FORM_ERROR_SUSCR_LOAD);
		}
		catch (Exception e)
		{
			throw new FormServiceException(FORM_ERROR_SUSCR_LOAD);
		}
	}

	/**
	 * @param userId
	 * @param name
	 * @return
	 */
	private Cliente findOrCreateClient ()
	{
		if ((this.cliente = lookForClient()) == null)
		{
			this.cliente = new Cliente(this.name);
			this.cliente = this.clienteController.create(this.cliente);
			this.isNewUser = true;
		}
		return this.cliente;
	}

	/**
	 * Busca el cliente por el id, si lo encuentra en la base devuelve una instancia de
	 * {@link Cliente} con los datos del mismo. En caso de no existir en la base un registro con el
	 * id enviado, devuelve <tt>null</tt>
	 *
	 * @param userId
	 *            El id del cliente (usuario)
	 * @return una instancia de {@link Cliente} o <tt>null</tt> si no se envuentran datos en la base
	 *         con el id recibido
	 */
	private Cliente lookForClient ()
	{
		log.debug("Buscando el cliente con id " + this.userId);
		try
		{
			Integer id = Integer.parseInt(this.userId);
			return this.clienteController.findById(id);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public String getResponse ()
	{
		StringBuilder sb = new StringBuilder();
		if (this.isNewUser)
		{
			sb.append(this.rm.getConst(CONN_PARAM_SERVER_CMD))
			.append(Constants.EQUALS)
			.append("CONF")
			.append(this.rm.getConst(SPLITTER_RESPONSE))
			.append(this.rm.getConst(CONN_PARAM_SERVER_CMD_PARAMS))
			.append(Constants.EQUALS)
			.append("{CONF[tar=4;not=true;val=")
			.append(this.cliente.getId())
			.append("]}");
		}
		log.info("Respuesta para la solicitud de formulario: " + sb);
		return sb.toString();
	}

	@Override
	protected String getFormTemplateFile ()
	{
		return this.FORM_TEMPLATE_FILE;
	}
}
