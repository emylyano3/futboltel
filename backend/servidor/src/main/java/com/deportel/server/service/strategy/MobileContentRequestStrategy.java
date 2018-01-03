package com.deportel.server.service.strategy;

import static com.deportel.server.web.Resources.Constants.*;
import static com.deportel.server.web.Resources.Texts.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.ConsultaDinamicaController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.ParametroConsulta;
import com.deportel.componentes.modelo.TipoComponente;
import com.deportel.controller.GlobalConsultasController;
import com.deportel.persistencia.utils.QueryParam;
import com.deportel.server.service.content.ComponentResponseStrategyFactory;
import com.deportel.server.service.content.strategies.ComponentResponseStrategy;
import com.deportel.server.service.exception.ServerServiceException;

/**
 * Implementacion de la estrategia que se encarga de buscar un
 * contenido determinado y prearmar los componentes de interfaz para
 * la aplicacion mobile. Con contenido se refiere a
 * "Datos legibles y de interes para el usuario final".
 *
 * @author Emy
 * @since 19/11/2011
 */
public class MobileContentRequestStrategy extends MobileDataServiceStrategy
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final ComponenteController				componenteController				= ComponenteController.getInstance(false);

	private final ConsultaDinamicaController		consultaDinamicaController			= ConsultaDinamicaController.getInstance(false);

	private final ComponentResponseStrategyFactory	componentResponseStrategyFactory	= new ComponentResponseStrategyFactory();

	private final GlobalConsultasController			consultasController					= new GlobalConsultasController(false);

	private final List<QueryParam>					requestParams						= new ArrayList<QueryParam>();

	private ConsultaDinamica						consultaDinamica;

	private List<?>									queryResult;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	protected void buildError (String message)
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			sb.append(this.rm.getConst(CONN_PARAM_RESULT))
			.append(Constants.EQUALS)
			.append(this.rm.getConst(STATE_ERROR));
			this.response.write(sb.toString().getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void buildResponse (HttpServletRequest request) throws IOException
	{
		int scw = Integer.parseInt((String) request.getAttribute(this.rm.getConst(CONN_PARAM_SCW)));
		int sch = Integer.parseInt((String) request.getAttribute(this.rm.getConst(CONN_PARAM_SCH)));
		ByteArrayOutputStream bjae = this.bjaeBuilder.doBuild(this.components, scw, sch, true);
		StringBuilder sb = new StringBuilder();
		sb.append(this.rm.getConst(CONN_PARAM_RESULT))
		.append(Constants.EQUALS )
		.append(this.rm.getConst(STATE_OK))
		.append(this.rm.getConst(SPLITTER_RESPONSE))
		.append(this.rm.getConst(CONN_PARAM_SOURCE))
		.append(Constants.EQUALS)
		.append((String) request.getAttribute(this.rm.getConst(CONN_PARAM_SOURCE)))
		.append(this.rm.getConst(SPLITTER_RESPONSE))
		.append(this.rm.getConst(CONN_PARAM_BJAE))
		.append(Constants.EQUALS);
		this.response.write(sb.toString().getBytes());
		this.response.write(bjae.toByteArray());
		this.response.flush();
		this.response.close();
	}

	@Override
	protected void prepareDataForResponse (HttpServletRequest request) throws ServerServiceException, InstanceNotFoundException, NumberFormatException
	{
		findDynamicQuery(request);
		injectParamsIntoDynamicQuery(request);
		executeDynamicQuery(request);
		Componente componenteRespuesta = this.consultaDinamica.getComponenteRespuesta();
		TipoComponente tipoComponente = componenteRespuesta.getTipoComponente();
		String componentType = tipoComponente.getDNombre();
		ComponentResponseStrategy strategy = this.componentResponseStrategyFactory.getStrategy(componentType);
		this.components = strategy.buildComponents(request, this.queryResult, this.consultaDinamica);
	}

	/**
	 * Busca la consulta dinamica usando el id del componente que ejecuto la action que disparo la
	 * consuta al server
	 *
	 * @param request
	 *            El request de la app mobile
	 * @throws InstanceNotFoundException
	 *             Si no encuentra ninguna cosnulta dinamica asociada al componente que ejecuto la
	 *             action que disparo la consulta al server
	 */
	private void findDynamicQuery (HttpServletRequest request) throws ServerServiceException
	{
		try
		{
			try
			{
				String source = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_SOURCE));
				source = Integer.toHexString(Integer.parseInt(source));
				findQueryBySourceId(source);
				log.info("Se encontro la consulta de contenidos [" + this.consultaDinamica.getDNombre() + "] para el componente [" + source + "]");
			}
			catch (Exception e)
			{
				String queryName = (String) request.getAttribute(this.rm.getConst(CONN_PARAM_QUERY_NAME));
				try
				{
					log.info("Busco la consulta por el nombre de la misma [" + queryName + "]");
					findQueryByQueryName(queryName);
					log.info("Se encontro la consulta de contenidos [" + this.consultaDinamica.getDNombre() + "]");
				}
				catch (InstanceNotFoundException e1)
				{
					log.info("No se encontro la consulta buscando por el nombre de la misma [" + queryName + "]");
					log.info("Busco la consulta por nombre de componente [" + queryName + "]");
					findQueryBySourceName(queryName);
					log.info("Se encontro la consulta de contenidos [" + this.consultaDinamica.getDNombre() + "]");
				}
			}
		}
		catch (InstanceNotFoundException e)
		{
			log.error("Error buscando la consulta dinamica causado por: " + e.getMessage());
			throw new ServerServiceException(e.getMessage());
		}
		catch (NullPointerException e)
		{
			log.error("Error buscando la consulta dinamica causado por: " + e.getMessage());
			throw new ServerServiceException(this.rm.getText(ERROR_NO_QUERY_FOUND));
		}
	}

	private void findQueryBySourceId (String sourceCode) throws InstanceNotFoundException
	{
		Componente componenteSolicitante =	this.componenteController.findByCodigo(sourceCode);
		this.consultaDinamica = this.consultaDinamicaController.findByIdSolicitante(componenteSolicitante.getCId());
	}

	private void findQueryByQueryName (String queryName) throws InstanceNotFoundException
	{
		this.consultaDinamica = this.consultaDinamicaController.findByName(queryName);
		if (this.consultaDinamica == null)
		{
			throw new InstanceNotFoundException(this.rm.getText(ERROR_NO_QUERY_FOR_NAME));
		}
	}

	private void findQueryBySourceName (String sourceName) throws InstanceNotFoundException
	{
		throw new InstanceNotFoundException(this.rm.getText(ERROR_NO_QUERY_FOR_SOURCE));
		//TODO Implementar la buqueda del consulta dinamica utilizando el nombre del componente
	}

	/**
	 * Toma los parametros correspondientes a la query, enviados en el request de la app mobile y
	 * los inyecta en el HQL de consulta dinamica
	 *
	 * @param request
	 *            El request de la app mobile
	 * @throws NullPointerException
	 */
	private void injectParamsIntoDynamicQuery (HttpServletRequest request) throws NullPointerException, RuntimeException
	{
		// Levanto los parametros de la consulta segun la configuracion guardada en la DB
		Set<ParametroConsulta> configuratedInParams = getDinamicQueryInParams();
		String[] queryParamsArray = getParamsInRequest(request);
		if (queryParamsArray.length < configuratedInParams.size()) throw new RuntimeException(this.rm.getText(ERROR_MISS_PARAMS_DYNA_QUERY));
		QueryParam checkedParam;
		String[] yoke;

		for (String element : queryParamsArray)
		{
			yoke = element.split(this.rm.getConst(SPLITTER_QUERY_PARAM));
			for (ParametroConsulta configuratedParam : configuratedInParams)
			{
				if (configuratedParam.getNombre().equalsIgnoreCase(yoke[0]))
				{
					checkedParam = new QueryParam();
					checkedParam.setType(configuratedParam.getTipoDato());
					checkedParam.setName(configuratedParam.getNombre());
					checkedParam.setValue(yoke[1]);
					this.requestParams.add(checkedParam);
				}
			}
		}
		if (this.requestParams.size() < configuratedInParams.size()) throw new RuntimeException(this.rm.getText(ERROR_MISS_PARAMS_DYNA_QUERY));
	}

	/**
	 * Levanta los parametros para la query de obtencion de contenidos que se enviaron en el request
	 *
	 * @param request
	 *            El request de la app mobile
	 * @return String[] con los parametros enviados para la query
	 */
	private String[] getParamsInRequest (HttpServletRequest request)
	{
		// Levanto los parametros enviados desde la app mobile (debe coincidir con lo definido en la base, en caso contrario se lanazar una excepcion)
		String queryParamsString = (String)request.getAttribute(this.rm.getConst(CONN_PARAM_QUERY_PARAMS));
		if (Utils.isNullOrEmpty(queryParamsString))
		{
			return new String[0];
		}
		else
		{
			return queryParamsString.split(this.rm.getConst(SPLITTER_QUERY_PARAMS));
		}
	}

	/**
	 * Ejecuta la consulta dinamica.
	 *
	 * @param request
	 *            El request de la app mobile
	 */
	private void executeDynamicQuery (HttpServletRequest request)
	{
		this.queryResult = this.consultasController.executeCustomQuery(this.consultaDinamica.getConsulta(), this.requestParams);
	}

	/**
	 * Filta los parametros de la consulta dinamica dejando solo aquellos que sean de entrada
	 *
	 * @return los parametros de entrada de la consulta
	 */
	private Set<ParametroConsulta> getDinamicQueryInParams ()
	{
		Set<ParametroConsulta> inParams = new HashSet<ParametroConsulta>();
		for (ParametroConsulta parametter : this.consultaDinamica.getParametrosConsulta())
		{
			if (Constants.DESHABILITADO.equals(parametter.getMTipoSalida()))
			{
				inParams.add(parametter);
			}
		}
		return inParams;
	}
}
