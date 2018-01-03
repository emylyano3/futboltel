package com.deportel.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.deportel.common.utils.EmailSender;
import com.deportel.common.utils.Properties;

public class EmailerTest
{
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
	public void sendMail () throws FileNotFoundException, IOException
	{
		Properties mailServerConfig = new Properties();
		mailServerConfig.load(new FileInputStream("C:/workspaces/ws_futboltel/futboltel/common/src/test/resources/emailer-test.properties"));
		EmailSender emailer = new EmailSender(mailServerConfig);
		emailer.sendEmail
		(
				"emylyano3@hotmail.com",
				"Test Email Sender Futboltel",
				"Test Email Sender Futboltel"
		);
	}

	@Test(expected=FileNotFoundException.class)
	public void sendMailErrorProperties () throws FileNotFoundException, IOException
	{
		Properties mailServerConfig = new Properties();
		mailServerConfig.load(new FileInputStream("ruta-inexistente.txt"));
		EmailSender emailer = new EmailSender(mailServerConfig);
		emailer.sendEmail
		(
				"emylyano3@hotmail.com",
				"Test Email Sender Futboltel",
				"Test Email Sender Futboltel"
		);
	}

	public static JUnit4TestAdapter suite()
	{
		return new JUnit4TestAdapter(EmailerTest.class);
	}

	public static void main(String[] args)
	{
		JUnitCore.runClasses(EmailerTest.class);
	}
}
