package com.deportel.server.web.servlet;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.deportel.controller.CacheController;

/**
 * Servlet implementation class AdministrationServiceServlet
 */
public class AdministrationServiceServlet extends HttpServlet
{
	private static final long		serialVersionUID	= 1L;

	private static final Logger		log					= Logger.getLogger(AdministrationServiceServlet.class);

	private final CacheController	cacheController		= new CacheController();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdministrationServiceServlet()
	{
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * Procesa el request recibido.
	 * 
	 * @param request
	 *            Request recibido
	 * @param response
	 *            Response a enviar
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InvalidParameterException, IllegalArgumentException
	{
		log.info("Se recibio un request para AdministrationServiceServlet");
		try
		{
			this.cacheController.reloadCache();
			ServletOutputStream sos = response.getOutputStream();
			sos.write("La recarga de cache del server se realizo correctamente".getBytes());
			sos.close();
		}
		catch (Exception e)
		{
			ServletOutputStream sos = response.getOutputStream();
			sos.write(("Error en recarga de cache del server causado por: " + e.getMessage()).getBytes());
			sos.close();
		}
	}

}
