package com.deportel.futboltel.persistencia.administracion;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.administracion.controller.ModuloController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;
import com.deportel.common.Constants;

public class ModuloControllerTest
{
	@BeforeClass
	public static void initClass ()
	{
		AdministracionSessionFactoryUtil.getInstance().getSessionFactory();
	}

	@Before
	public void init ()
	{
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(ModuloControllerTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(ModuloControllerTest.class);
	}

	private final Logger			log					= LoggerFactory.getLogger(ModuloControllerTest.class);

	private final ModuloController	moduloController	= ModuloController.getInstance();

	@Test @Ignore
	public void initialCreate ()
	{
		this.log.debug("Inicia el test create");
		Modulo modulo = new Modulo();
		modulo.setDNombre("editor_admin_futboltel");
		modulo.setDDescripcion("Módulo de administración de la aplicación Futboltel");
		modulo.setMEstado(Constants.HABILITADO);
		this.moduloController.crear(modulo);
	}

	@Test
	public void create ()
	{
		this.log.debug("Inicia el test create");
		Modulo modulo = new Modulo();
		modulo.setDNombre("test_module");
		modulo.setDDescripcion("Modulo creado con finalidad de pruebas provisorio");
		modulo.setMEstado(Constants.HABILITADO);

		this.moduloController.crear(modulo);
	}

	@Test
	public void findByName ()
	{
		this.log.debug("Inicia el test findByName");
		Modulo modulo = this.moduloController.findByName("test_module");
		this.log.debug("" + modulo);
	}

	@Test
	public void findAll ()
	{
		this.log.debug("Inicia el test findAll");
		List<Modulo> modulos = this.moduloController.findAll();
		for (Modulo modulo : modulos)
		{
			this.log.debug("" + modulo + "\n");
		}
	}

	@Test
	public void modificar ()
	{
		this.log.debug("Inicia el test modificar");
		Modulo modulo = this.moduloController.findByName("test_module");
		modulo.setDNombre(modulo.getDNombre() + "_modifyied");
		this.moduloController.modificar(modulo);
	}

	@Test
	public void eliminar ()
	{
		this.log.debug("Inicia el test eliminar");
		Modulo modulo = this.moduloController.findByName("test_module_modifyied");
		this.moduloController.eliminar(modulo.getCId());
	}

}
