package com.deportel.guiBuilder.model.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.deportel.common.Constants;
import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Utils;
import com.deportel.guiBuilder.model.GuiLoader;

public class InterfaceExporter
{
	private static Logger	log			= Logger.getLogger(InterfaceExporter.class);

	private static String	INTERFACE	= "R";
	public static final int	SPACE_SIZE	= 40;

	/**
	 * Genera la interfaz desde donde se manejaran los recursos dentro
	 * del paquete y proyecto especificado.
	 * 
	 * @param projectPath
	 *            El path del root del proyecto
	 * @param xmlFileName
	 *            El nombre del XML desde donde se toma se va a tomar
	 *            el diseño.
	 * @param packageName
	 *            El nombre del paquete bajo el que se creara la
	 *            interfaz.
	 * @author Emy
	 * @throws UserShowableException
	 * @since 25/01/2011
	 */
	public static void generateInterface (String projectPath, String xmlFileName, String packageName) throws UserShowableException
	{
		log.debug("Generando la interfaz...");
		Utils.validateParameters(projectPath, xmlFileName, packageName);
		log.debug("Obtengo el path de la carpeta resources");
		String resourcePath = Utils.findChildFolder(projectPath, Constants.SETTINGS_FOLDER);

		log.debug("Cargo el XML en la memoria");
		File xmlFile = new File (resourcePath + Constants.PATH_SEPARATOR + xmlFileName);
		GuiLoader loader = GuiLoader.getInstance();

		Map <String, Map <String, JComponent>> gui = loader.loadGui(xmlFile);

		log.debug("Busco la carpeta del paquete");
		String destFolder = Utils.findPackageFolder(projectPath, packageName);

		log.debug("Comienzo la escritura de la interfaz");
		writeInterface(gui, destFolder, packageName);
	}

	/**
	 * Escribe la interfaz en el archivo especificado.
	 * @param gui El diseño de la gui
	 * @param destFolder La carpeta donde se escribe la interfaz
	 * @return El output stream
	 * @throws UserShowableException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @since 25/01/2011
	 */
	private static void writeInterface (Map <String, Map<String, JComponent>> gui, String destFolder, String packageName) throws UserShowableException
	{
		log.debug("Generando la interfaz de la GUI en: " +  destFolder);
		FileOutputStream fos = null;
		File outFile = null;
		try
		{
			outFile = new File(destFolder + Constants.PATH_SEPARATOR + INTERFACE + Constants.DOT + Constants.JAVA_EXT);
			fos = new FileOutputStream(outFile);
			fos.write
			(
					(
							Constants.PACKAGE + Constants.SPACE +
							packageName + Constants.SEMICOLON +
							Constants.RETURN + Constants.RETURN
					).getBytes()
			);
			fos.write
			(
					(
							Constants.INTERFACE_DECLARATION +
							Constants.SPACE + INTERFACE +
							Constants.RETURN + Constants.BRACE_OPEN +
							Constants.RETURN
					).getBytes()
			);
			String declaration = "";
			Collection <String> groups = gui.keySet();
			Collection <JComponent> components;
			JComponent component;
			String componentAttributeName;
			String componentAttributeValue;
			String groupName;
			for (Iterator <String> it = groups.iterator(); it.hasNext();)
			{
				groupName = it.next();
				components = gui.get(groupName).values();
				fos.write
				(
						(
								Constants.TAB +
								Constants.INTERFACE_DECLARATION +
								Constants.SPACE + formatAttributeName(groupName) +
								Constants.RETURN + Constants.TAB +
								Constants.BRACE_OPEN + Constants.RETURN
						).getBytes()
				);
				for (Iterator <JComponent> it2 = components.iterator(); it2.hasNext();)
				{
					component = it2.next();
					componentAttributeName = formatAttributeName(component.getName());
					componentAttributeValue = formatAttributeValue(component.getName());
					declaration = Constants.DOUBLE_TAB + Constants.PSS + Constants.TAB + componentAttributeName + getSpace(componentAttributeName) + Constants.SPACE + Constants.EQUALS + Constants.SPACE + componentAttributeValue + Constants.SEMICOLON + Constants.RETURN;
					fos.write(declaration.getBytes());
				}
				fos.write
				(
						(
								Constants.TAB + Constants.BRACE_CLOSE +
								Constants.RETURN + Constants.RETURN
						).getBytes()
				);
			}
			fos.write((Constants.BRACE_CLOSE + Constants.RETURN).getBytes());
		}
		catch (FileNotFoundException e)
		{
			log.error("Error generando la interfaz");
			throw new UserShowableException("El archivo [" + outFile.getAbsolutePath() + "] no fue encontrado");
		}
		catch (IOException e)
		{
			log.error("Error escribiendo el archvivo");
			throw new UserShowableException("Ocurrio un error escribiendo la intefaz sobre el archivo [" + outFile.getAbsolutePath() + "]");
		}
		finally
		{
			try
			{
				log.debug("Finalizó correctamente la generación de la interfaz " + packageName + "." + INTERFACE + "." + Constants.JAVA_EXT);
				fos.close();
			}
			catch (IOException e)
			{
				log.error("Hubo un error escribiendo la interfaz");
				throw new UserShowableException("Ocurrio un error escribiendo la intefaz sobre el archivo [" + outFile.getAbsolutePath() + "]");
			}
			catch (NullPointerException e2)
			{
				log.error("Hubo un error escribiendo la interfaz");
				throw new UserShowableException("Ocurrio un error escribiendo la intefaz sobre el archivo [" + outFile.getAbsolutePath() + "]");
			}
		}
	}

	/**
	 * Formatea el string que recibe a la forma de nombre de constante
	 * segun la convencion java (todo mayuscula).
	 * @param unformated
	 * @return
	 * @since 25/01/2011
	 */
	private static String formatAttributeName (String unformated)
	{
		String formated = "";
		char [] charArray = new char [unformated.length()];
		unformated.getChars(0, unformated.length(), charArray, 0);
		int charType;
		for (int i = 0; i < charArray.length; i++)
		{
			char c = charArray[i];
			charType = Character.getType(c);
			if (charType == Character.UPPERCASE_LETTER && i != 0)
			{
				formated += "_" + c;
			}
			else
			{
				formated += c;
			}
		}
		formated = formated.toUpperCase();
		return formated;
	}

	/**
	 * Formatea el string que recibe agregandole las comillas para darle formato
	 * de valor de la constante.
	 * @param unformated
	 * @return
	 * @since 25/01/2011
	 */
	private static String formatAttributeValue (String unformated)
	{
		String formated = unformated;
		formated = "\"" + formated + "\"";
		return formated;
	}

	/**
	 * Calculates the necessary space to ident the interface values correctly.
	 * @param s The String to be written.
	 * @return String representing the space necessary.
	 * @since 25/01/2011
	 */
	private static String getSpace (String s)
	{
		String space = "";
		for (int i = 0; i < SPACE_SIZE - s.length(); i++)
		{
			space = space + Constants.SPACE;
		}
		return space;
	}

}
