package com.deportel.seguridad.main;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.deportel.common.Text;
import com.deportel.common.callback.CallBackListener;
import com.deportel.common.utils.Utils;
import com.deportel.seguridad.model.ProjectValues;
import com.deportel.seguridad.view.SeguridadWindow;

public class Seguridad
{
	private static Logger			log		= Logger.getLogger(Seguridad.class);

	private static Class<?>			clazz	= Seguridad.class;

	private static Properties		properties;

	private static String			language;

	private static String			name;

	private static Text				texts;

	private final SeguridadWindow	window;

	private Seguridad (CallBackListener listener)
	{
		this.window = SeguridadWindow.getInstance(listener);
	}

	public static Seguridad newInstance (CallBackListener listener)
	{
		return new Seguridad(listener);
	}

	public void start ()
	{
		this.window.createAndShowGui();
	}

	/**
	 * Carga las propiedades de la aplicacion
	 * de un archivo de properties
	 */
	private static void loadProperties ()
	{
		log.debug("Se cargan las properties del modulo Seguridad");
		properties = Utils.loadProperties(ProjectValues.PROPERTIES_FILE_PATH, clazz);
	}

	/**
	 * Se cargan los textos de la aplicacion
	 * desde el archivo de textos segun el lenguage
	 * configurado.
	 */
	private static void loadTexts ()
	{
		log.debug("Se cargan los textos del Seguridad");
		String textFilePath = getProperties().getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, clazz);
	}

	/**
	 * Se carga el lenguage de la aplicacion desde
	 * las properties.
	 */
	private static void loadLanguage ()
	{
		log.debug("Se carga el lenguage del Seguridad");
		language = getProperties().getProperty(ProjectValues.LANGUAGE);
	}

	/**
	 * Devuelve las properties de la aplicación
	 * que fueron cargadas desde un archivo de
	 * configuracion.
	 * @return {@link Properties} Con las properties
	 * de la aplicacion.
	 * @since 24/02/2011
	 */
	public static Properties getProperties ()
	{
		if (properties == null)
		{
			loadProperties();
		}
		return properties;
	}

	/**
	 * Devuelve los textos de la aplicacion.
	 * @return {@link Properties} Con los textos
	 * de la aplicacion.
	 * @since 24/02/2011
	 */
	public static Text getTexts ()
	{
		if (texts == null)
		{
			loadTexts();
		}
		return texts;
	}

	/**
	 * Devuelve el lenguaje que se preseteo para
	 * la aplicacion.
	 * @return El lenguaje de la aplicacion
	 * 
	 * @since 24/02/2011
	 */
	public static String getLanguage ()
	{
		if (language == null)
		{
			loadLanguage();
		}
		return language;
	}

	/**
	 * Devuelve el nombre de la aplicacion.
	 * @return el nombre de la aplicacion.
	 */
	public static String getName ()
	{
		if (name == null)
		{
			name = getProperties().getProperty(ProjectValues.APP_NAME);
		}
		return name;
	}
}
