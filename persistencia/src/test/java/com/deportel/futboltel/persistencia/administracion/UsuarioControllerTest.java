package com.deportel.futboltel.persistencia.administracion;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deportel.administracion.controller.PerfilController;
import com.deportel.administracion.controller.UsuarioController;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;
import com.deportel.common.Constants;

public class UsuarioControllerTest
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
		JUnitCore.runClasses(UsuarioControllerTest.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(UsuarioControllerTest.class);
	}

	private static final Logger		log					= LoggerFactory.getLogger(UsuarioControllerTest.class);

	private final PerfilController	perfilController	= PerfilController.getInstance();

	private final UsuarioController	usuarioController	= UsuarioController.getInstance();

	@Test @Ignore
	public void create ()
	{
		log.debug("Inicia el test create");
		Usuario user = new Usuario();
		user.setDNombre("Emiliano");
		user.setDAlias("emylyano3");
		user.setDApellido("Schiano di Cola");
		user.setDPassword("MANDA03");
		user.setMEstado(Constants.HABILITADO);
		user = this.usuarioController.crear(user);
		log.debug("" + user);
	}

	@Test
	public void create2 ()
	{
		log.debug("Inicia el test create");
		Usuario user = new Usuario();
		user.setDNombre("test_user_ephemeral");
		user.setDAlias("test_user_ephemeral");
		user.setDApellido("test_user_ephemeral");
		user.setDPassword("test_user_ephemeral");
		user.setMEstado(Constants.HABILITADO);

		Perfil profile = this.perfilController.findAll().get(0);
		user.getPerfiles().add(profile);

		user = this.usuarioController.crear(user);
		log.debug("" + user);
	}

	@Test
	public void findAll ()
	{
		log.debug("Inicia el test findAll");
		List<Usuario> users = this.usuarioController.findAll();
		for (Usuario usuario : users)
		{
			log.debug("" + usuario);
		}
	}

	@Test
	public void generatePassword () throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		log.info("Password: " + this.usuarioController.generateNewPassword());
	}
}
