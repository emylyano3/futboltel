package com.deportel.futboltel.persistencia.componentes;

import java.util.List;

import javax.management.InstanceNotFoundException;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.componentes.controller.ServicioSoporteController;
import com.deportel.componentes.controller.TemaController;
import com.deportel.componentes.modelo.ServicioSoporte;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;

public class ServicioSoporteTest
{
	@BeforeClass
	public static void initClass ()
	{
		ComponentesSessionFactoryUtil.getInstance().getSessionFactory();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(ServicioSoporteTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(ServicioSoporteTest.class);
	}

	private final static Logger				log								= LoggerFactory.getLogger(ServicioSoporteTest.class);

	private final ServicioSoporteController	servicioSoporteController		= ServicioSoporteController.getInstance(true);

	private final TemaController			temaController					= TemaController.getInstance(true);

	@Test
	public void create () throws InstanceNotFoundException
	{
		log.debug("Inicia el test create");
		Tema tema;
		tema = this.temaController.findById(1);
		final ServicioSoporte servicioSoporte = new ServicioSoporte(tema, "Soporte Test", "Soporte Test");
		this.servicioSoporteController.create(servicioSoporte);
	}

	@Test
	public void findAll ()
	{
		final List<?> result = this.servicioSoporteController.findAll();
		Assert.assertNotNull("El findAll no deberia retornar null", result);
	}

	@Test
	public void findByName ()
	{
		final ServicioSoporte result = this.servicioSoporteController.findByName("Soporte Test");
		Assert.assertNotNull("El findByName no deberia retornar null", result);
	}

	@Test
	public void remove ()
	{
		final ServicioSoporte servicioSoporte = this.servicioSoporteController.findByName("Soporte Test");
		this.servicioSoporteController.remove(servicioSoporte.getCId());
	}
}
