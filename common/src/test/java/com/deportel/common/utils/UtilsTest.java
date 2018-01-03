package com.deportel.common.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.common.Constants;
import com.deportel.common.exception.NotADirectoryException;
import com.deportel.common.exception.UserShowableException;

public class UtilsTest
{
	private final static Logger log = Logger.getLogger(UtilsTest.class);

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

	@Ignore
	@Test(expected = NotADirectoryException.class)
	public void findPackageFolderError () throws UserShowableException
	{
		final String result = FileUtils.findPackageFolder("C:/Workspaces/ProyectoFinalWorkspace/futboltel/common/src/test/java", "com.deportel.futboltel.exception");
		Assert.assertEquals(result, "C:\\Workspaces\\ProyectoFinalWorkspace\\futboltel\\common\\src\\test\\java\\com\\deportel\\futboltel");
	}

	@Test
	public void loadClassFromJar ()
	{
		try
		{
			final List <File> jars = FileUtils.findFiles("C:/Freelance/MavenWorkspace/reflection/src/main/resources/", Constants.JAR_EXTENSION);
			final Class <?> clazz = JarClassLoader.getInstance(jars.get(0).getAbsolutePath()).findClass("com.gui.utils.Utils");
			final Method [] methods = clazz.getMethods();
			for (final Method method : methods)
			{
				log.debug(method.getName());
			}
		}
		catch (final NotADirectoryException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void imprimeLaFechaEnMilisegundos ()
	{
		log.debug(System.currentTimeMillis());
	}

	/**
	 * Test de Utils.removeFileExtension
	 */
	@Test
	public void removeFileExtension ()
	{
		Assert.assertEquals("file", FileUtils.removeFileExtension("file.extension"));
	}

	@Test
	public void validateIntegerNumber ()
	{
		Assert.assertTrue("1 es un numero valido para longitud 1", Utils.validateIntegerNumber("1", 1));
		Assert.assertTrue("1234567890 es un numero valido para longitud 10", Utils.validateIntegerNumber("1234567890", 10));
		Assert.assertTrue("123 es un numero valido para longitud 3", Utils.validateIntegerNumber("123", 3));
		Assert.assertFalse("A no es un numero valido", Utils.validateIntegerNumber("A", 1));
		Assert.assertFalse("123 no es un numero valido para longitud 1", Utils.validateIntegerNumber("123", 1));
		Assert.assertTrue("123 es un numero valido para longitud 5", Utils.validateIntegerNumber("123", 5));
	}

	@Test
	public void validateAlphaNumeric ()
	{
		Assert.assertTrue(Utils.validateAlphaNumeric("123", 5));
		Assert.assertTrue(Utils.validateAlphaNumeric("Hola", 5));
		Assert.assertTrue(Utils.validateAlphaNumeric("Niño", 5));
		Assert.assertTrue(Utils.validateAlphaNumeric("AEIOUÁÉÍÓÚ áéíóú üÜ ñÑ", 30));
		Assert.assertFalse(Utils.validateAlphaNumeric("{", 5));
	}

	@Test
	public void encoder ()
	{
		final String original = "admin";
		final String encoded = Encoder.encode(original);
		final String decoded = Encoder.decode(encoded);
		Assert.assertEquals("El original debería ser igual al decoded", original, decoded);
		log.info("Original: " + original);
		log.info("Encoded: " + encoded);
		log.info("Decoded: " + decoded);
	}

	public void stringToInt ()
	{
		Assert.assertEquals(255, Converter.stringToInt("FF", 16).intValue());
	}

	public static JUnit4TestAdapter suite()
	{
		return new JUnit4TestAdapter(UtilsTest.class);
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(UtilsTest.class);
	}
}
