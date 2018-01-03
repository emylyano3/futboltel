package com.deportel.editor.administracion.main;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.deportel.administracion.controller.ModuloController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.Text;
import com.deportel.common.utils.Utils;
import com.deportel.editor.administracion.model.ProjectValues;
import com.deportel.editor.administracion.view.AEWindow;

public class AdministracionEditor
{
	private static Class<?>		clazz	= AdministracionEditor.class;

	private static Logger		log		= Logger.getLogger(AdministracionEditor.class);

	private static Properties	logProperties;

	private static Properties	properties;

	private static String		language;

	private static String		name;

	private static Text			texts;

	private static String		context;

	private static Modulo		module;

	static
	{
		loadLogProperties();
		properties = Utils.loadProperties(ProjectValues.PROPERTIES_FILE_PATH, AEWindow.class);
		String textFilePath = properties.getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, clazz);
	}

	private static void loadLogProperties()
	{
		logProperties = Utils.loadProperties(ProjectValues.LOG_PROPERTIES_PATH, AdministracionEditor.class);
		PropertyConfigurator.configure(logProperties);
	}

	/**
	 * Devuelve las properties de la aplicación que fueron cargadas desde un
	 * archivo de configuracion.
	 * 
	 * @return {@link Properties} Con las properties de la aplicacion.
	 * @since 24/02/2011
	 */
	public static Properties getProperties()
	{
		return properties;
	}

	/**
	 * Devuelve los textos de la aplicacion.
	 * 
	 * @return {@link Properties} Con los textos de la aplicacion.
	 * @since 24/02/2011
	 */
	public static Text getTexts()
	{
		return texts;
	}

	/**
	 * Devuelve el lenguaje que se preseteo para la aplicacion.
	 * 
	 * @return El lenguaje de la aplicacion
	 * @since 24/02/2011
	 */
	public static String getLanguage()
	{
		if (language == null || language.equals(""))
		{
			language = properties.getProperty(ProjectValues.LANGUAGE);
		}
		log.debug("El lenguaje seteado para el Editor: " + name + " es: " + language);
		return language;
	}

	/**
	 * Devuelve en nombre de la aplicacion.
	 * 
	 * @return
	 */
	public static String getName()
	{
		if (name == null)
		{
			name = properties.getProperty(ProjectValues.APP_NAME);
		}
		return name;
	}

	/**
	 * @param context the context to set
	 */
	public static void setContext (String context)
	{
		AdministracionEditor.context = context;
	}

	/**
	 * @return the context
	 */
	public static String getContext ()
	{
		return context;
	}

	public static Modulo getModule (String name)
	{
		if (module == null)
		{
			module = ModuloController.getInstance().findByName(name);
		}
		return module;
	}
}
