package com.deportel.futboltel.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.common.exception.UserShowableException;
import com.deportel.guiBuilder.model.GuiManager;

public class GuiManagerTest
{
	private Logger log = Logger.getLogger(GuiManagerTest.class);

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
	public void getComponents () throws UserShowableException, FileNotFoundException
	{
		this.log.debug("Inicia el test de loadGui");
		InputStream is = new FileInputStream(xmlFile);
		GuiManager gui = new GuiManager(is, null);
		JFrame window = new JFrame();
		window.add(gui.getButton("BotonSalir"));
		window.add(gui.getLabel("LabelSalir"));
		window.setVisible(true);
		window.pack();
		this.log.debug("Finaliza el test de loadGui");
	}

	public static JUnit4TestAdapter suite()
	{
		return new JUnit4TestAdapter(GuiManagerTest.class);
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(GuiManagerTest.class);
	}

	private static String xmlDef =
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
		"<gui xmlns=\"http://www.w3schools.com\"" +
		"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
		"	xsi:schemaLocation=\"http://svn3.xp-dev.com/svn/Futboltel/trunk/Implementacion/fulboltel/guibuilder/src/main/resources/gui.xsd\">" +
		"	<buttons>" +
		"		<button name=\"BotonSalir\" action_listener=\"\" icon=\"\" text=\"Cancelar\" tool_tip=\"Cancela el ingreso de datos\" enabled=\"true\"/>" +
		"	</buttons>" +
		"	<text_fields>" +
		"		<text_field name=\"User\" text=\"\" tool_tip=\"Ingrese su nombre de usuario\" enabled=\"true\"/>" +
		"		<password_field name=\"Pass\" text=\"\" tool_tip=\"Ingrese su clave de usuario\" enabled=\"true\"/>" +
		"	</text_fields>" +
		"	<labels>" +
		"		<label name=\"LabelSalir\" text=\"Usuario\" icon=\"\" tool_tip=\"\" enabled=\"true\"/>" +
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
