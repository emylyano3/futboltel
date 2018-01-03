package com.deportel.futboltel.editor.contenedor.main;

import java.awt.EventQueue;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.deportel.administracion.modelo.Usuario;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;
import com.deportel.common.Text;
import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Properties;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;
import com.deportel.editor.common.core.Editor;
import com.deportel.futboltel.editor.contenedor.model.ProjectTexts;
import com.deportel.futboltel.editor.contenedor.model.ProjectValues;
import com.deportel.futboltel.editor.contenedor.view.ContenedorSplash;
import com.deportel.futboltel.editor.contenedor.view.ContenedorWindow;
import com.deportel.futboltel.editor.loader.EditorDescription;
import com.deportel.futboltel.editor.loader.EditorJarLoader;
import com.deportel.futboltel.editor.loader.EditorLoader;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.Popup;

/**
 * Es el EP (entry point) a la aplicacion contenedor. Es el unico componente de todo el modulo de edicion que
 * posee EP (es decir un main) ya que todos los demás módulos serán invocados por él.
 *
 * @author Emy
 * @since 03/02/2011
 */
public class Contenedor
{
	private static Class<?>				clazz				= Contenedor.class;

	private static Logger				log					= Logger.getLogger(clazz);

	private static String				logPropertiesPath	= "/settings/log4j.properties";

	private static Properties			logProperties;

	private static Properties			properties;

	private static String				language;

	private static Text					texts;

	private static ContenedorWindow		window;

	private static ContenedorSplash		splash;

	private static ContenedorSeguridad	security;

	private static Usuario				user;

	/**
	 * Punto de entrada de la aplicacion Contenedor
	 *
	 * @param args
	 */
	public static void main (String[] args)
	{
		splash = ContenedorSplash.getInstance();
		splash.start();
		loadProperties();
		loadLanguage();
		loadTexts();
		init();
		splash.close();
		security.authenticateUser();
	}

	private static void init ()
	{
		splash.updateText(texts.get(ProjectTexts.LOADING_CONF));
		loadLogProperties();
		splash.updateProgress(5D);

		splash.updateText(texts.get(ProjectTexts.LOADING_DB_ADMIN));
		AdministracionSessionFactoryUtil.getInstance().getSessionFactory();
		splash.updateProgress(22D);

		splash.updateText(texts.get(ProjectTexts.LOADING_DB_TOURNAMENT));
		TorneoSessionFactoryUtil.getInstance().getSessionFactory();
		splash.updateProgress(22D);

		splash.updateText(texts.get(ProjectTexts.LOADING_DB_COMPONENTS));
		ComponentesSessionFactoryUtil.getInstance().getSessionFactory();
		splash.updateProgress(21D);

		splash.updateText(texts.get(ProjectTexts.LOADING_LF));
		setLookAndFeel();
		splash.updateProgress(5D);

		splash.updateText(texts.get(ProjectTexts.LOADING_SECURITY));
		security = new ContenedorSeguridad();
		splash.updateProgress(5D);

		splash.updateText(texts.get(ProjectTexts.LOADING_INTERFACES));
		initInterfaces();
		splash.updateProgress(20D);

		splash.updateText(texts.get(ProjectTexts.INITIALIZING_APP));
		Utils.sleep(300);
	}

	private static void initInterfaces ()
	{
		window = ContenedorWindow.getInstance(properties.getProperty(ProjectValues.APP_NAME));
		window.init();
	}

	private static void addEditors (Usuario user)
	{
//		String useDynaModules = properties.getProperty(ProjectValues.DYNA_MODULES);
//		if (Utils.getBoolean(useDynaModules))
		{
			addDynaEditors();
		}
//		else
//		{
//			addStaticEditors();
//		}
	}

//	private static void addStaticEditors ()
//	{
//		Editor editor;
//		editor = AEWindow.getInstance(window);
//		editor.setModuleName("modulo_administracion_accesos");
//		addStaticEditor(editor, "Administración");
//		window.addEditor(editor, "Administración");
//		editor = CEWindow.getInstance(window);
//		editor.setModuleName("modulo_administracion_contenido");
//		addStaticEditor(editor, "Contenido");
//		window.addEditor(editor, "Contenido");
//		editor = TEWindow.getInstance(window);
//		editor.setModuleName("modulo_edicion_plantillas");
//		addStaticEditor(editor, "Templates");
//		window.addEditor(editor, "Templates");
//	}
//
//	private static void addStaticEditor (Editor editor, String name)
//	{
//		window.addNewEditorMenuItem(name, name, "", editor.getMenuIcon());
//	}

	private static void addDynaEditors ()
	{
		EditorJarLoader jarLoader = new EditorJarLoader(properties);
		EditorLoader loader = new EditorLoader(jarLoader);
		try
		{
			List<EditorDescription> editorsDesc = loader.getEditors(window);
			editorsDesc = Utils.invert(editorsDesc);
			for (EditorDescription editorDesc : editorsDesc)
			{
				String name = editorDesc.getDescriptor().getProperty("editor.name");
				String moduleName = editorDesc.getDescriptor().getProperty("editor.module.name");
				Editor editor = editorDesc.getEditor();
				editor.setModuleName(moduleName);
				window.addNewEditorMenuItem
				(
						editorDesc.getDescriptor().getProperty("editor.name"),
						editorDesc.getDescriptor().getProperty("editor.menu.text"),
						editorDesc.getDescriptor().getProperty("editor.menu.tooltip"),
						editor.getMenuIcon()
				);
				window.addEditor(editor, name);
			}
		}
		catch (Exception e)
		{
			log.error("Error cargando editores causado por: " + e.getMessage());
		}
	}

	/**
	 *
	 */
	private static void setLookAndFeel ()
	{
		try
		{
			GuiUtils.setLookAndFeel(properties.getProperty(ProjectValues.APP_LOOK_AND_FEEL));
		}
		catch (UserShowableException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
	}

	/**
	 * Encola el lanzamiento de la aplicacion en la cola de eventos del sistema. Llegado su turno lanza la
	 * interfaz grafica de la aplicacion.
	 */
	public static void start (Usuario user)
	{
		log.info("Inicia la aplicacion Contenedor para el usuario: " + user.getDAlias());
		addEditors(user);
		Contenedor.user = user;
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run ()
			{
				window.createAndShowGui();
			}
		});
	}

	/**
	 * Carga las propiedades de logueo de la aplicacion de un archivo de properties
	 */
	private static void loadLogProperties ()
	{
		log.debug("Se cargan las properties de logging del Contenedor");
		logProperties = Utils.loadProperties(logPropertiesPath, clazz);
		PropertyConfigurator.configure(logProperties);
	}

	/**
	 * Carga las propiedades de la aplicacion de un archivo de properties
	 */
	private static void loadProperties ()
	{
		log.debug("Se cargan las properties del Contenedor");
		properties = Utils.loadProperties(ProjectValues.PROPERTIES_FILE_PATH, clazz);
	}

	/**
	 * Se cargan los textos de la aplicacion desde el archivo de textos segun el lenguage configurado.
	 */
	private static void loadTexts ()
	{
		log.debug("Se cargan los textos del Contenedor");
		String textFilePath = getProperties().getProperty(ProjectValues.TEXTS_FILE_PATH);
		texts = Utils.loadTexts(getLanguage(), textFilePath, clazz);
	}

	/**
	 * Se carga el lenguage de la aplicacion desde las properties.
	 */
	private static void loadLanguage ()
	{
		log.debug("Se carga el lenguage del Contenedor");
		language = getProperties().getProperty(ProjectValues.LANGUAGE);
	}

	/**
	 * Devuelve las properties de la aplicación que fueron cargadas desde un archivo de configuracion.
	 *
	 * @return {@link Properties} Con las properties de la aplicacion. Emy
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
	 *
	 * @return {@link Properties} Con los textos de la aplicacion. Emy
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
	 * Devuelve el lenguaje que se preseteo para la aplicacion.
	 *
	 * @return El lenguaje de la aplicacion
	 *
	 *         Emy
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
	 * Devuelve la entidad con los datos del usuario que se logueo al uniciar la aplicacion
	 *
	 * @return la entidad con los datos del usuario que se logueo al uniciar la aplicacion
	 */
	public static Usuario getLoggedUser ()
	{
		return user;
	}

}
