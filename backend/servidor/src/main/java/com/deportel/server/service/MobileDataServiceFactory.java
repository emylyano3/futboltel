package com.deportel.server.service;

import static com.deportel.server.web.Resources.Constants.*;
import org.apache.log4j.Logger;

import com.deportel.server.service.strategy.MobileChildsRequestStrategy;
import com.deportel.server.service.strategy.MobileContentRequestStrategy;
import com.deportel.server.service.strategy.MobileDataServiceStrategy;
import com.deportel.server.service.strategy.MobileDummyDataServiceStrategy;
import com.deportel.server.service.strategy.MobileReceiveFormStrategy;
import com.deportel.server.service.strategy.MobileUpdateComponentsStrategy;
import com.deportel.server.web.ResourceManager;

/**
 * Devuelve la estrategia apropiada para brindar el servicio requerido
 * por la aplicacion mobile cliente.
 * 
 * @author Emy
 * @since 16/09/2011
 */
public class MobileDataServiceFactory
{
	// ****************************************************************************
	// Constructor Singleton
	// ****************************************************************************

	public static MobileDataServiceFactory getInstance ()
	{
		if (instance == null)
		{
			instance = new MobileDataServiceFactory();
		}
		return instance;
	}

	private static MobileDataServiceFactory instance;

	private MobileDataServiceFactory ()
	{

	}

	// ****************************************************************************
	// Atributos
	// ****************************************************************************

	private final static Logger		log	= Logger.getLogger(MobileDataServiceFactory.class);
	private final ResourceManager	rm	= ResourceManager.getInstance();

	// ****************************************************************************
	// Metodos publicos
	// ****************************************************************************

	public MobileDataServiceStrategy getStrategy (String requestType, String requestSubType)
	{
		log.debug("Buscando la estrategia apropieada para el request tipo: " + requestType + " - Sub Tipo: " + requestSubType);
		if (requestType.equals(this.rm.getConst(CHILDS_REQUEST)))
		{
			return new MobileChildsRequestStrategy();
		}
		if (requestType.equals(this.rm.getConst(CONTENT_REQUEST)))
		{
			return new MobileContentRequestStrategy();
		}
		else if (requestType.equals(this.rm.getConst(COMPONENTS_UPDATE)))
		{
			return new MobileUpdateComponentsStrategy();
		}
		else if (requestType.equals(this.rm.getConst(SEND_FORM)))
		{
			return new MobileReceiveFormStrategy();
		}
		else
		{
			return new MobileDummyDataServiceStrategy();
		}
	}

}
