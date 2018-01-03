package com.deportel.editor.template.main;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.deportel.administracion.controller.ModuloController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.Text;
import com.deportel.common.utils.Utils;
import com.deportel.editor.template.model.ProjectValues;
import com.deportel.editor.template.view.TEWindow;

/**
 * Es el EP (entry point) a la aplicacion contenedor.
 * Es el unico componente de todo el modulo de edicion
 * que posee EP (es decir un main) ya que todos los demás
 * módulos serán invocados por él.
 * 
 * @author Emy
 * @since 03/02/2011
 */
public class TemplateEditor
{
	private static Class<?>		clazz					= TemplateEditor.class;

	private static Logger		log						= Logger.getLogger(TemplateEditor.class);

	private static Properties	logProperties;

	private static Properties	properties;

	private static String		language;

	private static String		name;

	private static Text			texts;

	private static Modulo		module;

	static
	{
		loadLogProperties();
		properties = Utils.loadProperties(ProjectValues.PROPERTIES_FILE_PATH, TEWindow.class);
		String textFilePath = properties.getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, clazz);
	}

	private static void loadLogProperties ()
	{
		logProperties = Utils.loadProperties(ProjectValues.LOG_PROPERTIES_PATH, TemplateEditor.class);
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
		log.debug("El lenguaje seteado para el Editor: " + name + " es: " + language);
		return language;
	}

	/**
	 * Devuelve en nombre de la aplicacion.
	 * @return
	 */
	public static String getName ()
	{
		if (name == null)
		{
			name = properties.getProperty(ProjectValues.APP_NAME);
		}
		return name;
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
