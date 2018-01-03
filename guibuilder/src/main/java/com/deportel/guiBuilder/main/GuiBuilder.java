package com.deportel.guiBuilder.main;

import java.awt.EventQueue;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.deportel.common.Constants;
import com.deportel.common.Text;
import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.model.ProjectTexts;
import com.deportel.guiBuilder.model.ProjectValues;
import com.deportel.guiBuilder.view.GuiBuilderSplash;
import com.deportel.guiBuilder.view.GuiBuilderWindow;

/**
 * Clase principar que posee el entry point de la herramienta.
 * 
 * @author Emy
 * @since 10/01/2011
 */
public class GuiBuilder
{
	private static Class<?>			clazz					= GuiBuilder.class;

	private static Logger			log						= Logger.getLogger(GuiBuilder.class);

	private static String			logPropertiesPath		= "/settings/log4j.properties";

	private static Properties		logProperties;

	private static Text				texts;

	private static String			language;

	private static final String		PROPERTIES_FILE_PATH	= "/settings/gui-builder.properties";

	private static Properties		properties				= Utils.loadProperties(PROPERTIES_FILE_PATH, GuiBuilderWindow.class);

	private static GuiBuilderSplash	splash;

	private static GuiBuilderWindow	window;

	/**
	 * Entry point de la aplicacion Gui Builder
	 * @param args
	 * @throws UserShowableException
	 */
	public static void main (String [] args) throws UserShowableException
	{
		splash = GuiBuilderSplash.getInstance();
		splash.start();
		loadLogProperties();
		String textFilePath = properties.getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, clazz);
		log.debug("Inicia la aplicacion " + texts.get(ProjectTexts.APP_NAME));
		GuiUtils.setLookAndFeel(Constants.WINDOWS_LOOK_AND_FEEL);
		window = GuiBuilderWindow.getInstance();
		Utils.sleep(1000);
		splash.close();
		start();
	}

	/**
	 * Lanza la interfaz grafica del Gui Builder
	 */
	public static void start ()
	{
		EventQueue.invokeLater
		(
				new Runnable()
				{
					public void run ()
					{
						window.createAndShowGui();
					}
				}
		);
	}

	/**
	 * Carga la configuracion del log desde el
	 * archvio log4j.properties
	 */
	private static void loadLogProperties ()
	{
		logProperties = Utils.loadProperties(logPropertiesPath, GuiBuilder.class);
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
