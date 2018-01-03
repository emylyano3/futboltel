package com.deportel.server.service.strategy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.server.service.form.MobileFormManagerFactory;
import com.deportel.server.service.form.exception.FormServiceException;
import com.deportel.server.service.form.strategies.MobileFormManagerStrategy;
import com.deportel.server.web.ConnectionConstants;

/**
 * Implementacion de la estrategia que se encarga de recibir los datos
 * enviados por la aplicacion mobile.
 * 
 * @author Emy
 * @since 15/09/2011
 */
public class MobileReceiveFormStrategy extends  MobileDataServiceStrategy
{
	private MobileFormManagerStrategy		formStrategy;

	private final MobileFormManagerFactory	formManagerStrategyFactory	= MobileFormManagerFactory.getInstance();

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.strategies.MobileDataServiceStrategy#buildResponse(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void buildResponse (HttpServletRequest request) throws IOException, NumberFormatException
	{
		log.info("Armando la respuesta de la carga de formulario");
		this.response.write((ConnectionConstants.CONN_PARAM_RESULT + Constants.EQUALS + ConnectionConstants.STATE_OK).getBytes());

		String formManagerResponse = this.formStrategy.getResponse();
		if (!Utils.isNullOrEmpty(formManagerResponse))
		{
			this.response.write(ConnectionConstants.SPLITTER_RESPONSE.getBytes());
			this.response.write(formManagerResponse.getBytes());
		}
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.strategies.MobileDataServiceStrategy#onError()
	 */
	@Override
	protected void buildError (String message)
	{
		try
		{
			this.response.write((ConnectionConstants.CONN_PARAM_RESULT + Constants.EQUALS + ConnectionConstants.STATE_ERROR).getBytes());
			this.response.write(ConnectionConstants.SPLITTER_RESPONSE.getBytes());
			this.response.write((ConnectionConstants.CONN_PARAM_MESSAGE + Constants.EQUALS + message).getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.strategies.MobileDataServiceStrategy#prepareDataForResponse(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void prepareDataForResponse (HttpServletRequest request) throws FormServiceException, HibernateException
	{
		log.info("Buscando estrategia para la carga del formulario enviado");
		String formType = (String) request.getAttribute(ConnectionConstants.CONN_PARAM_REQ_SUB_TYPE);
		this.formStrategy = this.formManagerStrategyFactory.getStrategy(formType);
		this.formStrategy.loadForm(request);
	}
}
