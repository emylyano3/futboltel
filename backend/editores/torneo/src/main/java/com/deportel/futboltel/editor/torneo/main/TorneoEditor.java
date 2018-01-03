package com.deportel.futboltel.editor.torneo.main;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.deportel.futboltel.common.Text;
import com.deportel.futboltel.common.utils.Utils;
import com.deportel.futboltel.editor.torneo.model.ProjectValues;
import com.deportel.futboltel.editor.torneo.view.TorneoEditorWindow;

/**
 * Es el EP (entry point) a la aplicacion contenedor.
 * Es el unico componente de todo el modulo de edicion
 * que posee EP (es decir un main) ya que todos los demás
 * módulos serán invocados por él.
 * 
 * @author Emy
 * @since 03/02/2011
 */
public class TorneoEditor
{
	private static Class<?>		clazz					= TorneoEditor.class;

	private static Logger		log						= Logger.getLogger(TorneoEditor.class);

	private static String		logPropertiesPath		= "/settings/log4j.properties";

	private static Properties	logProperties;

	private static Properties	properties;

	private static String		language;

	private static Text			texts;

	static
	{
		loadLogProperties();
		properties = Utils.loadProperties(ProjectValues.PROPERTIES_FILE_PATH, TorneoEditorWindow.class);
		String textFilePath = properties.getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, clazz);
	}

	private static void loadLogProperties ()
	{
		logProperties = Utils.loadProperties(logPropertiesPath, TorneoEditor.class);
		PropertyConfigurator.configure(logProperties);
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
		if (language == null || language.equals(""))
		{
			language = properties.getProperty(ProjectValues.LANGUAGE);
		}
		log.debug("Lenguage del " + getName() + ": " + language);
		return language;
	}

	/**
	 * Devuelve en nombre de la aplicacion.
	 * @return
	 */
	public static String getName ()
	{
		return properties.getProperty(ProjectValues.APP_NAME);
	}
}
