package com.deportel.componentes.utils;

import java.util.Properties;

import com.deportel.common.exception.InvalidConfigurationException;
import com.deportel.common.utils.Utils;
import com.deportel.persistencia.utils.GenericSessionFactoryUtil;

/**
 * Clase singletton que asegura que se levante una sola vez el archivo hibernate.cfg.xml
 * @author Emy
 */
public class ComponentesSessionFactoryUtil extends GenericSessionFactoryUtil
{
	private static final String		DEFAULT_CONFIG	= "hibernate-Componentes.cfg.xml";
	private static final String		CUSTOM_CONFIG	= "../conf/componentes-connection.properties";

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private ComponentesSessionFactoryUtil()
	{
	}

	private static ComponentesSessionFactoryUtil instance;

	public synchronized static ComponentesSessionFactoryUtil getInstance ()
	{
		if (instance == null)
		{
			instance = new ComponentesSessionFactoryUtil();
		}
		return instance;
	}

	protected void validateCustomConfig (Properties config) throws InvalidConfigurationException
	{

		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_USER)))
		{
			throw new InvalidConfigurationException("No se encontro el nombre de usuario para la conexion a la base de datos de componentes.");
		}
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_PASS)))
		{
			throw new InvalidConfigurationException("No se encontro la contraseña de usuario para la conexion a la base de datos de componentes.");
		}
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_URL)))
		{
			throw new InvalidConfigurationException("No se encontro la url para la conexion a la base de datos de componentes.");
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
