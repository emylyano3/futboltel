package com.deportel.server.service.strategy;

import static com.deportel.server.web.Resources.Constants.*;
import static com.deportel.server.web.Resources.Texts.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.deportel.componentes.modelo.Componente;
import com.deportel.components.BjaeBuilder;
import com.deportel.server.service.exception.ServerServiceException;
import com.deportel.server.web.ResourceManager;

/**
 * Interfaz que define la prestacion de servicios a las aplicaciones
 * mobile.
 * 
 * @author Emy
 */
public abstract class MobileDataServiceStrategy
{
	protected static Logger			log			= Logger.getLogger(MobileDataServiceStrategy.class);

	protected List<Componente>		components	= new ArrayList<Componente>(0);

	protected BjaeBuilder			bjaeBuilder	= new BjaeBuilder();

	protected ResourceManager		rm			= ResourceManager.getInstance();

	protected ByteArrayOutputStream	response;

	public MobileDataServiceStrategy ()
	{
		this.response = new ByteArrayOutputStream();
	}

	/**
	 * Metodo que se deberá implementar por cada estrategia de acuerdo
	 * a la solicitud que este atendiendo.
	 * 
	 * @param parameters
	 *            Los parametros que llegan en el request realizado
	 *            por la aplicacion mobile
	 * @return El resultado del procesamiento del request representado
	 *         como un stream de bytes dentro de un objeto
	 *         {@link ByteArrayOutputStream}
	 */
	public final ByteArrayOutputStream doService (HttpServletRequest request)
	{
		log.info("Servicio para App: [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "]");
		try
		{
			log.info("Recopilando datos para App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "]");
			prepareDataForResponse(request);
			log.info("Armando respuesta para App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "]");
			buildResponse(request);
			return this.response;
		}
		catch (ServerServiceException e)
		{
			log.error("Error recopilando datos de App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "] causado por: " + e.getMessage());
			buildError(e.getMessage());
			return this.response;
		}
		catch (IOException e)
		{
			log.error("Error recopilando datos de App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "] causado por: " + e.getMessage());
			e.printStackTrace();
			buildError(this.rm.getText(ERROR_SERVER_INTERNAL));
			return this.response;
		}
		catch (HibernateException e)
		{
			log.error("Error recopilando datos de App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "] causado por: " + e.getMessage());
			e.printStackTrace();
			buildError(this.rm.getText(ERROR_SERVER_INTERNAL));
			return this.response;
		}
		catch (NumberFormatException e)
		{
			log.error("Error recopilando datos de App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "] causado por: " + e.getMessage());
			e.printStackTrace();
			buildError(this.rm.getText(ERROR_SERVER_INTERNAL));
			return this.response;
		}
		catch (Exception e)
		{
			log.error("Error recopilando datos de App [" + request.getAttribute(this.rm.getConst(CONN_PARAM_APP_NAME)) + "] causado por: " + e.getMessage());
			e.printStackTrace();
			buildError(this.rm.getText(ERROR_SERVER_INTERNAL));
			return this.response;
		}
	}

	/**
	 * 
	 * @param request
	 * @throws InstanceNotFoundException
	 */
	protected abstract void prepareDataForResponse (HttpServletRequest request) throws ServerServiceException, Exception;

	/**
	 * 
	 * @return
	 */
	protected abstract void buildError (String message);

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	protected abstract void buildResponse (HttpServletRequest request) throws Exception;

}
