package com.deportel.futboltel.persistencia.administracion;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.administracion.controller.ModuloController;
import com.deportel.administracion.controller.PerfilController;
import com.deportel.administracion.controller.TipoPermisoController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;
import com.deportel.common.Constants;

public class PerfilControllerTest
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
		JUnitCore.runClasses(PerfilControllerTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(PerfilControllerTest.class);
	}

	private final Logger			log					= LoggerFactory.getLogger(PerfilControllerTest.class);

	private final PerfilController	perfilController	= PerfilController.getInstance();

	@Test
	public void create ()
	{
		this.log.debug("Inicia el test create");
		Perfil perfil = new Perfil();
		perfil.setDNombre("test_profile_ephemeral");
		perfil.setDDescripcion("Perfil creado con finalidad de pruebas provisorio");
		perfil.setMEstado(Constants.HABILITADO);

		TipoPermiso tipoPermiso = TipoPermisoController.getInstance().findAll().get(0);
		Modulo modulo = ModuloController.getInstance().findAll().get(0);

		ModuloPerfil moduloPerfil = new ModuloPerfil();
		moduloPerfil.setTipoPermiso(tipoPermiso);
		moduloPerfil.setModulo(modulo);

		perfil.getModulosPerfil().add(moduloPerfil);

		perfil = this.perfilController.crear(perfil);
		this.log.debug("" + perfil);
	}

	@Test
	public void findByName ()
	{
		this.log.debug("Inicia el test findByName");
		Perfil perfil = this.perfilController.findByName("test_profile_ephemeral");
		this.log.debug("" + perfil);
	}

	@Test
	public void findAll ()
	{
		this.log.debug("Inicia el test findAll");
		List<Perfil> perfiles = this.perfilController.findAll();
		for (Perfil perfil : perfiles)
		{
			this.log.debug("" + perfil + "\n");
		}
	}

	@Test
	public void modificar ()
	{
		this.log.debug("Inicia el test modificar");
		Perfil perfil = this.perfilController.findByName("test_profile_ephemeral");
		perfil.getModulosPerfil().clear();
		perfil.setDNombre(perfil.getDNombre() + "_modifyied");
		this.perfilController.modificar(perfil);
	}

	@Test
	public void eliminar ()
	{
		this.log.debug("Inicia el test eliminar");
		Perfil perfil = this.perfilController.findByName("test_profile_ephemeral_modifyied");
		this.perfilController.eliminar(perfil.getCId());
	}

}
