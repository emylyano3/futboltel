package com.deportel.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.common.utils.XMLParser;

public class XMLParserTest
{
	private Logger			log		= Logger.getLogger(XMLParserTest.class);

	private static File		xmlFile;

	private static String	xmlPath	= "xmlAux";

	/**
	 * Se llama una sola vez para cargar todo lo necesario para efectuar los
	 * tests.
	 */
	@BeforeClass
	public static void initClass ()
	{
		try
		{
			xmlFile = new File(xmlPath);
			FileWriter writer = new FileWriter(xmlFile);
			writer.write(xmlDef);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Se llama una sola vez, para liberar los recursos utilizados en los
	 * tests.
	 */
	@AfterClass
	public static void endClass ()
	{
		xmlFile.delete();
	}

	/**
	 * Se llama antes de invocar a cada uno de los tests.
	 */
	@Before
	public void init ()
	{
	}

	/**
	 * Se llama luego de invocar a cada uno de los tests.
	 */
	@After
	public void end ()
	{

	}

	@Test
	public void getAttributeValues ()
	{
		this.log.debug("Inicia el test de getAttributeValues");
		String tagName = "BUTTON";
		String attName = "name";
		Vector <String> parsed = XMLParser.getAttributeValues(xmlFile, tagName, attName);
		for (Iterator <String> it = parsed.iterator(); it.hasNext();)
		{
			this.log.debug(it.next());
		}
		this.log.debug("Finaliza el test de getAttributeValues");
	}

	public static JUnit4TestAdapter suite()
	{
		return new JUnit4TestAdapter(XMLParserTest.class);
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(XMLParserTest.class);
	}

	private static String xmlDef =
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
		"<gui xmlns=\"http://www.w3schools.com\"" +
		"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
		"	xsi:schemaLocation=\"http://svn3.xp-dev.com/svn/Futboltel/trunk/Implementacion/fulboltel/guibuilder/src/main/resources/gui.xsd\">" +
		"	<buttons>" +
		"		<button name=\"Cancelar\" action_listener=\"\" icon=\"\" text=\"Cancelar\" tool_tip=\"Cancela el ingreso de datos\" enabled=\"true\"/>" +
		"	</buttons>" +
		"	<text_fields>" +
		"		<text_field name=\"User\" text=\"\" tool_tip=\"Ingrese su nombre de usuario\" enabled=\"true\"/>" +
		"		<password_field name=\"Pass\" text=\"\" tool_tip=\"Ingrese su clave de usuario\" enabled=\"true\"/>" +
		"	</text_fields>" +
		"	<labels>" +
		"		<label name=\"User\" text=\"Usuario\" icon=\"\" tool_tip=\"\" enabled=\"true\"/>" +
		"	</labels>" +
		"	<panels>" +
		"		<panel name=\"General\" text=\"\" tool_tip=\"\" layout=\"org.jdesktop.layout.GroupLayout\" enabled=\"true\"/>" +
		"	</panels>" +
		"	<menu_bars>" +
		"		<menu_bar name=\"Principal\" tool_tip=\"Barra de menu de la ventana principal\" enabled=\"true\">" +
		"			<menu name=\"File\" text=\"Archivo\" tool_tip=\"\" enabled=\"true\" >" +
		"				<menu_item name=\"NewFile\" text=\"New\" tool_tip=\"\" enabled=\"true\" action_listener=\"\" />" +
		"			</menu>" +
		"		</menu_bar>" +
		"	</menu_bars>" +
		"</gui>";
}
