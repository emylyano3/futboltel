package com.deportel.server.service.strategy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

/**
 * Implementacion de la estrategia que se encarga de recibir los datos
 * enviados por la aplicacion mobile.
 * 
 * @author Emy
 * @since 15/09/2011
 */
public class MobileDummyDataServiceStrategy extends  MobileDataServiceStrategy
{
	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.strategies.MobileDataServiceStrategy#buildResponse(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void buildResponse (HttpServletRequest request) throws IOException, NumberFormatException
	{
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.strategies.MobileDataServiceStrategy#onError()
	 */
	@Override
	protected void buildError (String message)
	{
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.servidor.data.service.strategies.MobileDataServiceStrategy#prepareDataForResponse(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void prepareDataForResponse (HttpServletRequest request) throws HibernateException
	{
	}
}
