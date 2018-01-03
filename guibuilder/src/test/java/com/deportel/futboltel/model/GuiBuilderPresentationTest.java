package com.deportel.futboltel.model;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class GuiBuilderPresentationTest
{
	private Logger log = Logger.getLogger(GuiBuilderPresentationTest.class);

	/**
	 * Se llama una sola vez para cargar todo lo necesario para efectuar los
	 * tests.
	 */
	@BeforeClass
	public static void initClass ()
	{

	}

	/**
	 * Se llama una sola vez, para liberar los recursos utilizados en los
	 * tests.
	 */
	@AfterClass
	public static void endClass ()
	{

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
	public void getManager ()
	{
		this.log.debug("Inicia el test de getManager");

		//		GuiBuilderPresentation.getManager(xmlFileName, clazz)

		this.log.debug("Finaliza el test de getManager");
	}

	public static JUnit4TestAdapter suite()
	{
		return new JUnit4TestAdapter(GuiBuilderPresentationTest.class);
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(GuiBuilderPresentationTest.class);
	}
}
