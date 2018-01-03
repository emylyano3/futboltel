package com.deportel.server.web.filter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.deportel.common.utils.Encoder;


/**
 * Clase que implementa {@link Filter} y se encarga de la
 * decodificacion de los parámetros que llegan como parte de la query
 * string.
 * 
 * @author Emy
 * @since 13/09/2011
 */
public class MobileContextDecoderFilter implements Filter
{
	private static final Logger log = Logger.getLogger(MobileContextDecoderFilter.class);

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init (FilterConfig arg0) throws ServletException
	{

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, InvalidParameterException
	{
		log.debug("Inicia el doFilter");
		Map<String, String[]> params = request.getParameterMap();
		checkParameters(params);
		Set<String> keys = params.keySet();
		List<String[]> parameters = decodeParameters(keys.iterator().next());
		injectAttributes (request, parameters);
		log.debug("Finaliza el doFilter");
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy ()
	{

	}

	/**
	 * Valida los parámetros recibidos en el request. Se espera una
	 * cantidad de {link #EXCPECTED_PARAMS_COUNT} , que es el
	 * resultado de los parametros encriptados desde el cliente
	 * mobile.
	 * 
	 * @param params
	 *            Los parametros enviados desde el cliente.
	 * @throws InvalidParameterException
	 *             En caso de que exista más de
	 *             {@link #EXCPECTED_PARAMS_COUNT} parametro.
	 */
	private void checkParameters(Map<String, String[]> params) throws InvalidParameterException
	{
		if (params.values().size() < EXCPECTED_PARAMS_COUNT)
		{
			throw new InvalidParameterException();
		}
	}

	/**
	 * 
	 * @param encodedParameter
	 * @return
	 */
	private List<String[]> decodeParameters (String encodedParameter)
	{
		List<String[]> parameters = new ArrayList<String[]>();

		String decoded = Encoder.decode(encodedParameter);
		String[] yokes = decoded.split("&");
		String[] yoke;
		for (String yoke2 : yokes)
		{
			yoke = yoke2.split("=");
			parameters.add(yoke);
		}
		return parameters;
	}

	/**
	 * 
	 * @param request
	 * @param parameters
	 */
	private void injectAttributes (ServletRequest request, List<String[]> parameters)
	{
		String[] aux;
		for (Iterator<String[]> it = parameters.iterator(); it.hasNext();)
		{
			aux = it.next();
			if (aux.length == 2)
			{
				request.setAttribute(aux[PARAM_NAME],aux[PARAMS_VALUE]);
			}
		}
	}

	/** Es la cantidad de parametros que se esperan recibir en el request */
	public static final int	EXCPECTED_PARAMS_COUNT	= 1;

	public static final int	PARAM_NAME				= 0;
	public static final int	PARAMS_VALUE			= 1;
}
