package com.deportel.server.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;


/**
 * Clase que implementa {@link Filter} y se encarga de
 * la autenticacion de usuarios.
 * 
 * @author Emy
 * @since 13/09/2011
 */
public class MobileAuthenticationFilter implements Filter
{

	private final static Logger log = Logger.getLogger(MobileAuthenticationFilter.class);

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy ()
	{

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		log.debug("Inicia el doFilter");
		log.debug("Finaliza el doFilter");
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init (FilterConfig arg0) throws ServletException
	{

	}

}
