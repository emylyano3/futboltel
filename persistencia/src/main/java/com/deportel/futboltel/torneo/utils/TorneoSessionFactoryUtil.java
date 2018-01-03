package com.deportel.futboltel.torneo.utils;

import java.util.Properties;

import com.deportel.common.exception.InvalidConfigurationException;
import com.deportel.common.utils.Utils;
import com.deportel.persistencia.utils.GenericSessionFactoryUtil;

/**
 * Clase singletton que asegura que se levante una sola vez el archivo hibernate.cfg.xml
 * @author Emy
 */
public class TorneoSessionFactoryUtil extends GenericSessionFactoryUtil
{
	private static final String		DEFAULT_CONFIG	= "hibernate-Torneo.cfg.xml";
	private static final String		CUSTOM_CONFIG	= "../conf/torneo-connection.properties";

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private TorneoSessionFactoryUtil()
	{
	}

	private static TorneoSessionFactoryUtil instance;

	public synchronized static TorneoSessionFactoryUtil getInstance ()
	{
		if (instance == null)
		{
			instance = new TorneoSessionFactoryUtil();
		}
		return instance;
	}

	protected void validateCustomConfig (Properties config) throws InvalidConfigurationException
	{

		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_USER)))
		{
			throw new InvalidConfigurationException("No se encontro el nombre de usuario para la conexion a la base de datos de torneo.");
		}
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_PASS)))
		{
			throw new InvalidConfigurationException("No se encontro la contraseña de usuario para la conexion a la base de datos de torneo.");
		}
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_URL)))
		{
			throw new InvalidConfigurationException("No se encontro la url para la conexion a la base de datos de torneo.");
		}
	}

	@Override
	protected String getDefaultConfig ()
	{
		return DEFAULT_CONFIG;
	}

	@Override
	protected String getCustomConfig ()
	{
		return CUSTOM_CONFIG;
	}

}
