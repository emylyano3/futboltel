package com.deportel.futboltel.persistencia.administracion;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.administracion.controller.ModuloController;
import com.deportel.administracion.controller.PerfilController;
import com.deportel.administracion.controller.TipoPermisoController;
import com.deportel.administracion.controller.UsuarioController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;
import com.deportel.common.Constants;

@RunWith(MockitoJUnitRunner.class)
public class AdministracionIntegralTest
{
	@Mock
	UsuarioController	controller;

	Usuario				validUser	= new Usuario("Josep", "Jose", "Lopez", "chufachufa", Constants.HABILITADO);

	Usuario				invalidUser	= new Usuario("Josep", "Jose", "Lopez", "null", Constants.DESHABILITADO);

	@BeforeClass
	public static void initClass ()
	{
		AdministracionSessionFactoryUtil.getInstance().getSessionFactory();
	}

	@Before
	public void init ()
	{
		this.log.debug("Stubbing del mock de usuario controller");
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(AdministracionIntegralTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(AdministracionIntegralTest.class);
	}

	private final Logger	log	= LoggerFactory.getLogger(AdministracionIntegralTest.class);

	@Test
	public void createPermissionType ()
	{
		TipoPermiso type = new TipoPermiso();
		type.setDNombre("test_permission");
		type.setDDescripcion("Permiso creado con finalidad de pruebas");
		type = TipoPermisoController.getInstance().crear(type);
		this.log.debug("" + type);
	}

	@Test
	public void createModule ()
	{
		Modulo module = new Modulo();
		module.setDNombre("test_module");
		module.setDDescripcion("Modulo creado con finalidad de pruebas");
		module.setMEstado(Constants.HABILITADO);
		ModuloController.getInstance().crear(module);
	}

	@Test
	public void createProfile ()
	{
		Perfil perfil = new Perfil();
		perfil.setDNombre("test_profile");
		perfil.setDDescripcion("Perfil creado con finalidad de pruebas");
		perfil.setMEstado(Constants.HABILITADO);

		TipoPermiso tipoPermiso = TipoPermisoController.getInstance().findByName("test_permission");
		Modulo modulo = ModuloController.getInstance().findByName("test_module");

		ModuloPerfil moduloPerfil = new ModuloPerfil();
		moduloPerfil.setTipoPermiso(tipoPermiso);
		moduloPerfil.setModulo(modulo);

		perfil.getModulosPerfil().add(moduloPerfil);

		PerfilController.getInstance().crear(perfil);
	}

	@Test
	public void createUser ()
	{
		Perfil perfil = PerfilController.getInstance().findByName("test_profile");
		Usuario user = new Usuario();
		user.setDAlias("test_user");
		user.setDNombre("test_user");
		user.setDApellido("test_user");
		user.setDPassword("test_user");
		user.setMEstado(Constants.HABILITADO);
		user.getPerfiles().add(perfil);

		UsuarioController.getInstance().crear(user);
	}

	@Test
	@Ignore
	public void updateModule ()
	{
		Modulo module = ModuloController.getInstance().findByName("futboltel admin");
		module.setDNombre("editor_admin_futboltel");
		ModuloController.getInstance().modificar(module);
	}

}
