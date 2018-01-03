package com.deportel.guiBuilder.presentation;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.guiBuilder.model.exporter.InterfaceExporter;

/**
 * Es la capa de presentacion de la funcionalidad del gui builder
 * para los sistemas externos.
 * @author Emy
 * @since 24/01/2011
 */
public abstract class GuiBuilderPresentation
{

	private static Logger log = Logger.getLogger(GuiBuilderPresentation.class);

	public static void generateInterface (String projectPath, String xmlFileName, String packageName) throws UserShowableException
	{
		InterfaceExporter.generateInterface(projectPath, xmlFileName, packageName);
	}

	public static GuiManager getManager (String xmlFilePath, Class <?> clazz)
	{
		Utils.validateParameters(xmlFilePath, clazz);
		log.debug("Inicio del getManager");
		try
		{
			InputStream is = clazz.getResourceAsStream(xmlFilePath);
			GuiManager manager = new GuiManager (is, clazz);
			return manager;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
