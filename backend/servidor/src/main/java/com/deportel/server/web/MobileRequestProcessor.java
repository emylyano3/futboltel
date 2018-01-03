package com.deportel.server.web;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.deportel.server.service.MobileDataServiceFactory;
import com.deportel.server.service.strategy.MobileDataServiceStrategy;

/**
 * Se encargada de procesar el request realizado desde la aplicacion
 * mobile cliente.
 * 
 * @author Emy
 * @since 15/09/2011
 */
public class MobileRequestProcessor
{
	/**
	 * Se procesa el request dependiendo del tipo. Se devuelve el
	 * resultado del porcesamiento del request en formato de bytes
	 * dentro de un {@link ByteArrayOutputStream}
	 * 
	 * @param request
	 *            El request enviado desde el cliente mobile
	 * @return {@link ByteArrayOutputStream} con el resultado del
	 *         porcesamiento del request
	 */
	public ByteArrayOutputStream processRequest (ServletRequest request)
	{
		String requestType = (String) request.getAttribute(ConnectionConstants.CONN_PARAM_REQ_TYPE);
		if (!isValidRequestType(requestType))
		{
			// llama a la clase que devuelve el error de tipo
			return null;
		}
		else
		{
			String requestSubType = (String) request.getAttribute(ConnectionConstants.CONN_PARAM_REQ_SUB_TYPE);
			if (!isValidRequestSubType(requestType, requestSubType))
			{
				// llama a la clase que devuelve el error de subtipo
				return null;
			}
			else
			{
				MobileDataServiceStrategy strategy = MobileDataServiceFactory.getInstance().getStrategy(requestType, requestSubType);
				return strategy.doService((HttpServletRequest)request);
			}
		}
	}

	/**
	 * Determina si el tipo de request es valido para una aplicacion
	 * mobile
	 * 
	 * @param requestType
	 *            El tipo de request
	 * @return <tt>true</tt> si el sub tipo de request es valido
	 *         <tt>false</tt> en caso contrario.
	 */
	private boolean isValidRequestType (String requestType)
	{
		return true;
	}

	/**
	 * Determina si el sub tipo de request es valido para una
	 * aplicacion mobile.
	 * 
	 * @param requestSubType
	 *            El sub tipo de request
	 * @return <tt>true</tt> si el sub tipo de request es valido
	 *         <tt>false</tt> en caso contrario.
	 */
	private boolean isValidRequestSubType (String requestType, String requestSubType)
	{
		return true;
	}
}
