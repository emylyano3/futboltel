package com.deportel.persistencia.utils;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.deportel.common.exception.InvalidConfigurationException;
import com.deportel.common.exception.PropertiesNotFoundException;
import com.deportel.common.utils.Utils;

/**
 * Clase singletton que asegura que se levante una sola vez el archivo hibernate.cfg.xml
 * @author Emy
 */
public abstract class GenericSessionFactoryUtil
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	/**
	 * Instancia de la fabrica de sesiones.
	 */
	protected SessionFactory		sessionFactory;

	private static Logger			log				= Logger.getLogger(GenericSessionFactoryUtil.class);

	protected static final String	CONNECTION_URL	= "hibernate.connection.url";
	protected static final String	CONNECTION_PASS	= "hibernate.connection.password";
	protected static final String	CONNECTION_USER	= "hibernate.connection.username";

	public synchronized SessionFactory getSessionFactory ()
	{
		if (this.sessionFactory == null)
		{
			try
			{
				AnnotationConfiguration config = new AnnotationConfiguration();
				config.configure(getDefaultConfig());
				overrideConfig(config);
				this.sessionFactory = config.buildSessionFactory();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return this.sessionFactory;
	}

	protected abstract String getDefaultConfig();

	protected abstract String getCustomConfig();

	private void overrideConfig (AnnotationConfiguration config)
	{
		try
		{
			Properties customConfig = Utils.loadProperties(getCustomConfig());
			validateCustomConfig(customConfig);
			config.setProperty(CONNECTION_USER, customConfig.getProperty(CONNECTION_USER));
			config.setProperty(CONNECTION_PASS, customConfig.getProperty(CONNECTION_PASS));
			config.setProperty(CONNECTION_URL, customConfig.getProperty(CONNECTION_URL));
		}
		catch (PropertiesNotFoundException e)
		{
			log.warn(e.getMessage());
		}
		catch (InvalidConfigurationException e)
		{
			log.warn(e.getMessage());
		}
		catch (Exception e)
		{
			log.warn("No se puedo cargar la configuracion especifica del esquema componentes. Utilizando la configuracion por defecto.");
		}
	}

	private void validateCustomConfig (Properties config) throws InvalidConfigurationException
	{
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_USER)))
		{
			throw new InvalidConfigurationException("No se encontro el nombre de usuario para la conexion a la base de datos.");
		}
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_PASS)))
		{
			throw new InvalidConfigurationException("No se encontro la contraseña de usuario para la conexion a la base de datos.");
		}
		if (Utils.isNullOrEmpty(config.getProperty(CONNECTION_URL)))
		{
			throw new InvalidConfigurationException("No se encontro la url para la conexion a la base de datos.");
		}
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Cierra la fabrica de sesiones
	 */
	public void close ()
	{
		if (this.sessionFactory != null)
		{
			this.sessionFactory.close();
		}
		this.sessionFactory = null;
	}

	/**
	 * Returns a session from the session context. If there is no
	 * session in the context it opens a session, stores it in the
	 * context and returns it. This factory is intended to be used
	 * with a hibernate.cfg.xml including the following property
	 * <property
	 * name="current_session_context_class">thread</property> This
	 * would return the current open session or if this does not
	 * exist, will create a new session
	 * 
	 * @return the session
	 */
	public Session getCurrentSession ()
	{
		return this.sessionFactory.getCurrentSession();
	}

	/**
	 * Opens a session and will not bind it to a session context
	 * 
	 * @return the session
	 */
	public Session openSession ()
	{
		return this.sessionFactory.openSession();
	}
}
