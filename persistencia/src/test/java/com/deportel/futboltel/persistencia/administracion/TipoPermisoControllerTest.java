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

import com.deportel.administracion.controller.TipoPermisoController;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;

public class TipoPermisoControllerTest
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
		JUnitCore.runClasses(TipoPermisoControllerTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(TipoPermisoControllerTest.class);
	}

	private final Logger				log					= LoggerFactory.getLogger(TipoPermisoControllerTest.class);

	private final TipoPermisoController	tipoPermisoController	= TipoPermisoController.getInstance();

	@Test @Ignore
	public void initialCreate ()
	{
		this.log.debug("Inicia el test initialCreate");
		TipoPermiso tipoPermiso = new TipoPermiso();
		tipoPermiso.setDNombre("ejecucion");
		tipoPermiso.setDDescripcion("Permiso de ejecución sobre un módulo del sistema");
		this.tipoPermisoController.crear(tipoPermiso);
	}

	@Test
	public void create ()
	{
		this.log.debug("Inicia el test create");
		TipoPermiso tipoPermiso = new TipoPermiso();
		tipoPermiso.setDNombre("test_permission_type");
		tipoPermiso.setDDescripcion("TipoPermiso creado con finalidad de pruebas provisorio");

		this.tipoPermisoController.crear(tipoPermiso);
	}

	@Test
	public void findByName ()
	{
		this.log.debug("Inicia el test findByName");
		TipoPermiso tipoPermiso = this.tipoPermisoController.findByName("test_permission_type");
		this.log.debug("" + tipoPermiso);
	}

	@Test
	public void findAll ()
	{
		this.log.debug("Inicia el test findAll");
		List<TipoPermiso> modulos = this.tipoPermisoController.findAll();
		for (TipoPermiso tipoPermiso : modulos)
		{
			this.log.debug("" + tipoPermiso + "\n");
		}
	}

	@Test
	public void modificar ()
	{
		this.log.debug("Inicia el test modificar");
		TipoPermiso tipoPermiso = this.tipoPermisoController.findByName("test_permission_type");
		tipoPermiso.setDNombre(tipoPermiso.getDNombre() + "_modifyied");
		this.tipoPermisoController.modificar(tipoPermiso);
	}

	@Test
	public void eliminar ()
	{
		this.log.debug("Inicia el test eliminar");
		TipoPermiso tipoPermiso = this.tipoPermisoController.findByName("test_permission_type_modifyied");
		this.tipoPermisoController.eliminar(tipoPermiso.getCId());
	}

}
