package com.deportel.guiBuilder.gui.component;

import java.awt.EventQueue;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.deportel.common.Text;
import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.model.ProjectValues;

/**
 * Clase singleton que representa una aplicacion java con interfaz grafica
 * 
 * @author Emy
 * @since 13/07/2011
 */
public abstract class GuiApplication <E, W extends Window, S extends Splash>
{
	private Class<?>			clazz				= this.getClass();

	private Logger				log					= Logger.getLogger(this.clazz);

	private static Properties	properties;

	private static String		language;

	private static Text			texts;

	private W					window;

	/**
	 * Punto de entrada de la aplicacion Contenedor
	 * @param args
	 */
	public static void main (String [] args)
	{
		//		instance.splash = Splash.getInstance();
		//		instance.splash.start();
		//		instance.loadProperties();
		//		instance.loadLanguage();
		//		instance.loadTexts();
		//		instance.init();
		//		instance.splash.close();
	}

	protected abstract void init ();

	//	private void initInterfaces ()
	//	{
	//		//		this.window = this.window.getInstance(properties.getProperty(ProjectValues.APP_NAME));
	//		//		this.window.init();
	//	}

	/**
	 * Encola el lanzamiento de la aplicacion en la
	 * cola de eventos del sistema. Llegado su turno
	 * lanza la interfaz grafica de la aplicacion.
	 */
	public void start ()
	{
		this.log.debug("Inicia la aplicacion Contenedor");
		EventQueue.invokeLater
		(
				new Runnable()
				{
					public void run ()
					{
						GuiApplication.this.window.createAndShowGui();
					}
				}
		);
	}

	//	/**
	//	 * Carga las propiedades de logueo de la aplicacion
	//	 * de un archivo de properties
	//	 */
	//	private void loadLogProperties ()
	//	{
	//		this.log.debug("Se cargan las properties de logging del Contenedor");
	//		logProperties = Utils.loadProperties(logPropertiesPath, this.clazz);
	//		PropertyConfigurator.configure(logProperties);
	//	}

	/**
	 * Carga las propiedades de la aplicacion
	 * de un archivo de properties
	 */
	protected abstract void loadProperties ();

	/**
	 * Se cargan los textos de la aplicacion
	 * desde el archivo de textos segun el lenguage
	 * configurado.
	 */
	private void loadTexts ()
	{
		this.log.debug("Se cargan los textos del Contenedor");
		String textFilePath = getProperties().getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, this.clazz);
	}

	/**
	 * Se carga el lenguage de la aplicacion desde
	 * las properties.
	 */
	private void loadLanguage ()
	{
		this.log.debug("Se carga el lenguage del Contenedor");
		language = getProperties().getProperty(ProjectValues.LANGUAGE);
	}

	/**
	 * Devuelve las properties de la aplicación
	 * que fueron cargadas desde un archivo de
	 * configuracion.
	 * @return {@link Properties} Con las properties
	 * de la aplicacion.
	 *  Emy
	 * @since 24/02/2011
	 */
	public Properties getProperties ()
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
	 *  Emy
	 * @since 24/02/2011
	 */
	public Text getTexts ()
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
	 *  Emy
	 * @since 24/02/2011
	 */
	public String getLanguage ()
	{
		if (language == null)
		{
			loadLanguage();
		}
		return language;
	}

}
