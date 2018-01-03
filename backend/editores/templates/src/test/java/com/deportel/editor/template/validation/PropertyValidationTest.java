package com.deportel.editor.template.validation;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.editor.template.exception.InvalidPropertyException;

public class PropertyValidationTest
{
	private static final Logger log = Logger.getLogger(PropertyValidationTest.class);

	private static TipoPropiedadController tipoPropiedadController = TipoPropiedadController.getInstance(true);

	private static TipoPropiedad actionProp;
	private static TipoPropiedad actionParamsProp;
	private static TipoPropiedad textProp;

	/**
	 * Se llama una sola vez para cargar todo lo necesario para efectuar los
	 * tests.
	 */
	@BeforeClass
	public static void initClass ()
	{
		log.info("Inicializacion de la clase de test PropertyValidationTest");
		actionProp = tipoPropiedadController.findByXmlTag("action");
		actionParamsProp = tipoPropiedadController.findByXmlTag("aparam");;
		textProp = tipoPropiedadController.findByXmlTag("text");;
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
	public void actionPropertyValidation_EXISTING_PROPS ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionProp);
		validator.validate("CONF");
		validator.validate("CONF,OPEN_SCRE");
		validator.validate("OPEN_SCRE,OPEN_SCRE");
		validator.validate(null);
		validator.validate("");
	}

	@Test(expected=InvalidPropertyException.class)
	public void actionPropertyValidation_NON_EXISTING_PROP ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionProp);
		validator.validate("CON");
	}

	@Test
	public void actionParamsPropertyValidationTest_AVAIL_ACTIONS ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionParamsProp);
		validator.validate("{UPDA_PROP[tar=1;pro=11;val=100000A]}");
	}

	@Test(expected=InvalidPropertyException.class)
	public void actionParamsPropertyValidationTest_UNAVAIL_PARAMS ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionParamsProp);
		validator.validate("{UPDA_PROP[aux=1;xxx=11;aaa=100000A]}");
	}

	@Test(expected=InvalidPropertyException.class)
	public void actionParamsPropertyValidationTest_BAD_FORMAT ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionParamsProp);
		validator.validate("{UPDA_PROP(tar=1;pro=11;val=100000A)}");
	}

	@Test(expected=InvalidPropertyException.class)
	public void actionParamsPropertyValidationTest_BAD_FORMAT_2 ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionParamsProp);
		validator.validate("ñ*");
	}

	@Test(expected=InvalidPropertyException.class)
	public void actionParamsPropertyValidationTest_BAD_FORMAT_3 ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(actionParamsProp);
		validator.validate("[]");
	}

	@Test
	public void genericTextPropertyValidationTest_PERMITTED_CHARS ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(textProp);
		validator.validate("ABCDEFGHIJKLMNÑOPQRSTUVWXYZ");
		validator.validate("abcdefghijklmnñopqrstuvwxyz");
		validator.validate("0123456789");
		validator.validate(",;.:-_{[}]^+*~|!#$%&/()='?\\¿¡<>\"");
		validator.validate("{UPDA_PROP[tar=1;pro=11;val=100000A]}");
		validator.validate("[]");
	}

	@Test(expected=InvalidPropertyException.class)
	public void genericTextPropertyValidationTest_FORBIDEN_CHARS ()
	{
		PropertyValidationStrategy validator = PropertyValidationFactory.getStrategy(textProp);
		validator.validate("°");
		validator.validate("`");
		validator.validate("ü");
	}

	public static JUnit4TestAdapter suite()
	{
		return new JUnit4TestAdapter(PropertyValidationTest.class);
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(PropertyValidationTest.class);
	}
}
