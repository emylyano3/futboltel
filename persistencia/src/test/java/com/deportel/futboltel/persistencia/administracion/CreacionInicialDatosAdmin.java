package com.deportel.futboltel.persistencia.administracion;

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
import com.deportel.administracion.controller.UsuarioController;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.TipoPermiso;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;
import com.deportel.common.Constants;

public class CreacionInicialDatosAdmin
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
		JUnitCore.runClasses(CreacionInicialDatosAdmin.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(CreacionInicialDatosAdmin.class);
	}

	private static final Logger			log						= LoggerFactory.getLogger(CreacionInicialDatosAdmin.class);

	private final TipoPermisoController	tipoPermisoController	= TipoPermisoController.getInstance();

	private final ModuloController		moduloController		= ModuloController.getInstance();

	private final PerfilController		perfilController		= PerfilController.getInstance();

	private final UsuarioController		usuarioController		= UsuarioController.getInstance();

	private static TipoPermiso			tipoPermiso;

	private static Modulo				modulo;

	private static Perfil				perfil;

	@Test
	public void tipoPermisoCreate ()
	{
		log.debug("Creando tipo permiso");
		tipoPermiso = new TipoPermiso();
		tipoPermiso.setDNombre("ejecucion");
		tipoPermiso.setDDescripcion("Permiso de ejecución sobre un módulo del sistema");
		tipoPermiso = this.tipoPermisoController.crear(tipoPermiso);
		log.debug("Tipo Permiso creado: " + tipoPermiso);
	}

	@Test
	public void moduloAdminCreate ()
	{
		log.debug("Creando modulo");
		modulo = new Modulo();
		modulo.setDNombre("editor_admin_futboltel");
		modulo.setDDescripcion("Módulo de administración de la aplicación Futboltel");
		modulo.setMEstado(Constants.HABILITADO);
		modulo = this.moduloController.crear(modulo);
		log.debug("Modulo creado: " + modulo);
	}

	@Test
	public void moduloContenidoCreate ()
	{
		log.debug("Creando modulo");
		modulo = new Modulo();
		modulo.setDNombre("editor_contenido_futboltel");
		modulo.setDDescripcion("Módulo de edición de contenido de la aplicación Futboltel");
		modulo.setMEstado(Constants.HABILITADO);
		modulo = this.moduloController.crear(modulo);
		log.debug("Modulo creado: " + modulo);
	}

	@Test
	public void perfilCreate ()
	{
		log.debug("Creando perfil");
		perfil = new Perfil();
		perfil.setDNombre("administrador");
		perfil.setDDescripcion("Perfil con permiso de ejecución para todos los modulos del sistema Futboltel");
		perfil.setMEstado(Constants.HABILITADO);
		ModuloPerfil moduloPerfil = new ModuloPerfil();
		moduloPerfil.setTipoPermiso(tipoPermiso);
		moduloPerfil.setModulo(modulo);
		perfil.getModulosPerfil().add(moduloPerfil);
		perfil = this.perfilController.crear(perfil);
		log.debug("Perfil creado: " + perfil);
	}

	@Test
	public void userCreate ()
	{
		log.debug("Creando usuario");
		Usuario user = new Usuario();
		user.setDNombre("Emiliano");
		user.setDAlias("emylyano3");
		user.setDApellido("Schiano di Cola");
		user.setDPassword("MANDA03");
		user.setMEstado(Constants.HABILITADO);
		user.getPerfiles().add(perfil);
		user = this.usuarioController.crear(user);
		log.debug("Usuario creado: " + user);
	}

}
