package com.deportel.server.service.strategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

import com.deportel.common.Constants;
import com.deportel.common.utils.Converter;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.server.web.ConnectionConstants;

/**
 * Implementacion de la estrategia que se encarga de conseguir los
 * datos de aquellos componentes enviados por por la aplicacion
 * mobile.
 *
 * @author Emy
 * @since 15/09/2011
 */
public class MobileUpdateComponentsStrategy extends MobileDataServiceStrategy
{
	@Override
	protected void prepareDataForResponse (HttpServletRequest request) throws HibernateException
	{
		String items = (String) request.getAttribute(ConnectionConstants.CONN_PARAM_ITEMS);
		List<String> itemsList = Arrays.asList(items.split(ConnectionConstants.SPLITTER_ITEMS));
		findComponents(itemsList);
	}

	@Override
	protected void buildError (String message)
	{
		try
		{
			this.response.write((ConnectionConstants.CONN_PARAM_RESULT + Constants.EQUALS + ConnectionConstants.STATE_ERROR).getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void buildResponse (HttpServletRequest request) throws IOException, NumberFormatException
	{
		int scw = Converter.stringToInt((String) request.getAttribute(ConnectionConstants.CONN_PARAM_SCW));
		int sch = Converter.stringToInt((String) request.getAttribute(ConnectionConstants.CONN_PARAM_SCH));
		ByteArrayOutputStream bjae = this.bjaeBuilder.doBuild(this.components, scw, sch, false);
		this.response = new ByteArrayOutputStream();
		this.response.write
		(
				(ConnectionConstants.CONN_PARAM_RESULT + Constants.EQUALS + ConnectionConstants.STATE_OK).getBytes()
		);
		this.response.write(ConnectionConstants.SPLITTER_RESPONSE.getBytes());
		writeParam(request, ConnectionConstants.CONN_PARAM_SOURCE);
		this.response.write(ConnectionConstants.SPLITTER_RESPONSE.getBytes());
		this.response.write((ConnectionConstants.CONN_PARAM_BJAE + Constants.EQUALS).getBytes());
		this.response.write(bjae.toByteArray());
	}

	/**
	 * Toma del request el parametro con el nombre paramName y lo escribe en el response
	 *
	 * @param request
	 *            {@link HttpServletRequest} con el request del cliente
	 * @param response
	 *            {@link ByteArrayOutputStream} con la respuesta que se le va a enviar al cliente
	 * @param paramName
	 *            El nombre del parametro que se va a escribir
	 */
	private void writeParam (HttpServletRequest request, String paramName)
	{
		try
		{
			StringBuilder param = new StringBuilder();
			param.append(paramName).append(Constants.EQUALS).
			append(request.getAttribute(paramName));
			this.response.write(param.toString().getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Encuentra los componentes que se quieren actualizar.
	 *
	 * @param componentsIdList
	 *            Lista de id de componentes a buscar
	 */
	private void findComponents (List<String> componentsIdList)
	{
		log.debug("Buscando los componentes solicitados");
		String codigo;
		Componente component;
		for (String string : componentsIdList)
		{
			int aux = Converter.stringToInt(string);
			codigo = Integer.toHexString(aux);
			component = ComponenteController.getInstance(false).findByCodigo(codigo);
			if (component != null)
			{
				this.components.add(component);
			}
		}
	}
}
