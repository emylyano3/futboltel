package com.deportel.server.service.form.strategies;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.deportel.server.service.form.exception.FormServiceException;

/**
 * @author EMY
 */
public class MobileDummyFormStrategy extends MobileFormManagerStrategy
{
	protected static final Logger log = Logger.getLogger(MobileDummyFormStrategy.class);

	private final String					FORM_TEMPLATE_FILE		= "/config/subscription-form.properties";

	public MobileDummyFormStrategy()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.form.strategies.MobileFormManagerStrategy#loadForm(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void doLoad (HttpServletRequest request)
	{
		log.debug("Implementacion dummy de la carga de formulario");
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.form.strategies.MobileFormManagerStrategy#getResponse()
	 */
	@Override
	public String getResponse ()
	{
		return "";
	}

	@Override
	protected String getFormTemplateFile ()
	{
		return this.FORM_TEMPLATE_FILE;
	}

	@Override
	protected void doStuff () throws FormServiceException
	{

	}

	@Override
	protected void validateFormData () throws FormServiceException
	{

	}
}
