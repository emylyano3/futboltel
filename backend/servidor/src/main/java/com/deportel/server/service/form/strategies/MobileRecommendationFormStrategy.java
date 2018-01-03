package com.deportel.server.service.form.strategies;

import static com.deportel.server.service.form.Form.Fields.*;
import static com.deportel.server.web.Resources.Texts.*;
import static com.deportel.server.web.Resources.Constants.*;
import static com.deportel.common.utils.EmailSender.Configuration.*;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.deportel.common.utils.EmailSender;
import com.deportel.common.utils.Utils;
import com.deportel.server.service.form.exception.FormServiceException;

/**
 * @author EMY
 */
public class MobileRecommendationFormStrategy extends MobileFormManagerStrategy
{
	protected static final Logger	log					= Logger.getLogger(MobileRecommendationFormStrategy.class);

	private final String			FORM_TEMPLATE_FILE	= "/config/recommendation/form.properties";
	private final String			RECOM_MAIL_TEMPLATE = "/config/recommendation/mail-template.properties";
	private final String			MAIL_SERVER_CONFIG	= "/config/core/email-server.properties";

	private String					name;
	private String					email;

	private final Properties		mailServerConfig	= Utils.loadProperties(this.MAIL_SERVER_CONFIG, MobileRecommendationFormStrategy.class);

	public MobileRecommendationFormStrategy()
	{
		super();
	}

	@Override
	public void doLoad (HttpServletRequest request) throws HibernateException, FormServiceException
	{
		log.info("Realizando la carga del formulario de suscripcion enviado");
		this.userId = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_USER_ID));
		this.application = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME));
		this.customization = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_APP_CUSTOMIZATION));
		this.name = (String) request.getAttribute(this.formTemplate.getField(NAME));
		this.email = (String) request.getAttribute(this.formTemplate.getField(EMAIL));
	}

	@Override
	protected void validateFormData () throws FormServiceException
	{
		if (!Utils.validateEmail(this.email))
		{
			throw new FormServiceException(VAL_ERROR_EMAIL);
		}
		if (Utils.isNullOrEmpty(this.name))
		{
			throw new FormServiceException(VAL_ERROR_NULL_NAME);
		}
	}

	@Override
	protected void doStuff () throws FormServiceException
	{
		try
		{
			log.info("Realizando la recomendacion");
			EmailSender sender = new EmailSender(this.mailServerConfig);
			Properties mail = Utils.loadProperties(this.RECOM_MAIL_TEMPLATE, getClass());
			sender.sendEmail(this.email, mail.getProperty(MAIL_SUBJECT), mail.getProperty(MAIL_BODY));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new FormServiceException(FORM_ERROR_RECOM_SEND);
		}
	}

	@Override
	public String getResponse ()
	{
		StringBuilder sb = new StringBuilder();
		log.info("Respuesta para la solicitud de formulario: " + sb);
		return sb.toString();
	}

	@Override
	protected String getFormTemplateFile ()
	{
		return this.FORM_TEMPLATE_FILE;
	}
}
