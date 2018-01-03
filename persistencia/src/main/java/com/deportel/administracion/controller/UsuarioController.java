package com.deportel.administracion.controller;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.administracion.dao.hibernate.UsuarioDaoHibernate;
import com.deportel.administracion.modelo.Usuario;
import com.deportel.common.Constants;

/**
 * @author Emy
 */
public class UsuarioController
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public synchronized static UsuarioController getInstance ()
	{
		if (instance == null)
		{
			instance = new UsuarioController();
		}
		return instance;
	}

	private UsuarioController()
	{

	}

	private static UsuarioController	instance;

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static Logger				log			= Logger.getLogger(UsuarioController.class);

	private final UsuarioDaoHibernate	usuarioDaoH	= UsuarioDaoHibernate.getInstance();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * 
	 */
	public Usuario crear (Usuario usuario)
	{
		log.info("Comienza el método crear");
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			usuario = this.usuarioDaoH.create(usuario);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return usuario;
		}
		tx.commit();
		return usuario;
	}

	public Usuario modificar (Usuario usuario)
	{
		log.info("Comienza el método modificar");
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			usuario = this.usuarioDaoH.update(usuario);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return usuario;
		}
		tx.commit();
		return usuario;
	}

	/**
	 * Borra logicamente al usuario, colocando su estado en N
	 * 
	 * @param id
	 *            usuario
	 * @since 01/02/2011
	 */
	public void deshabilitar (Integer idUsuario)
	{
		log.info("Comienza el método deshabilitar");
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			Usuario usuario = this.usuarioDaoH.findById(idUsuario);
			usuario.setMEstado(Constants.DESHABILITADO);
			this.usuarioDaoH.update(usuario);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
		}
		tx.commit();
	}

	/**
	 * Borra fisicamente de la base el usuario
	 * 
	 * @param id
	 *            usuario
	 * @since 01/02/2011
	 */
	public void eliminar (Integer idUsuario)
	{
		log.info("Comienza el método eliminar");
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			this.usuarioDaoH.remove(idUsuario);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
		}
		tx.commit();
	}

	/**
	 * Busca el usuario a traves del id recibido.
	 * 
	 * @param id
	 *            El id del usuario
	 * @return {@link Usuario} El usuario
	 * @throws EntityNotFoundException
	 */
	public Usuario findById (Integer id)
	{
		Usuario user = null;
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			user = this.usuarioDaoH.findById(id);
			if (user == null)
			{
				throw new EntityNotFoundException("Usuario no encontrado");
			}
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return user;
		}
		tx.commit();
		return user;
	}

	/**
	 * Busca el usuario usando como criterio de busqueda el alias del mismo.
	 * 
	 * @param name
	 *            El alias del usuario
	 * @return {@link Usuario} El usuario
	 */
	public Usuario findByAlias (String name)
	{
		Usuario user = null;
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			user = this.usuarioDaoH.findByAlias(name);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return user;
		}
		tx.commit();
		return user;
	}

	/**
	 * Busca el usuario usando como criterio de busqueda el alias del mismo.
	 * 
	 * @param password
	 *            La contraseña del usuario
	 * @return {@link Usuario} El usuario
	 */
	public Usuario findByPassword (String password)
	{
		Usuario user = null;
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			user = this.usuarioDaoH.findByPassword(password);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return user;
		}
		tx.commit();
		return user;
	}

	/**
	 * Obtiene todos los Usuarios dados de alta
	 * 
	 * @return {@link Usuario} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public List<Usuario> findAll ()
	{
		List<Usuario> resultados = null;
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			resultados = this.usuarioDaoH.findAll();
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return resultados;
		}
		tx.commit();
		return resultados;
	}

	/**
	 * Obtiene todos los Usuarios dados de alta con estado habilitado
	 * 
	 * @return {@link Usuario} List
	 * @throws EntityNotFoundException
	 */
	public List<Usuario> findAllEnabled ()
	{
		List<Usuario> resultados = null;
		Transaction tx = this.usuarioDaoH.getSession().beginTransaction();
		try
		{
			resultados = this.usuarioDaoH.findAllEnabled();
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return resultados;
		}
		tx.commit();
		return resultados;
	}

	public String generateNewPassword ()
	{
		UUID uuid = UUID.randomUUID();
		String sUuid = uuid.toString();
		String result = sUuid.substring(sUuid.length() - this.PASSWORD_MAX_CHARS, sUuid.length());
		return result;
	}

	private final int PASSWORD_MAX_CHARS = 12;
}
