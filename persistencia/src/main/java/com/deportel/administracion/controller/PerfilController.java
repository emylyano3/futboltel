package com.deportel.administracion.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.administracion.dao.hibernate.PerfilDaoHibernate;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.common.Constants;

/**
 * @author Emy
 */
public class PerfilController
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public synchronized static PerfilController getInstance ()
	{
		if (instance == null)
		{
			instance = new PerfilController();
		}
		return instance;
	}

	private static PerfilController	instance;

	private PerfilController()
	{

	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static Logger				log			= Logger.getLogger(PerfilController.class);

	private final PerfilDaoHibernate	perfilDaoH	= PerfilDaoHibernate.getInstance();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Perfil crear (Perfil perfil)
	{
		log.info("Comienza el método crear");
		Transaction tx = this.perfilDaoH.getSession().beginTransaction();
		try
		{
			perfil = this.perfilDaoH.create(perfil);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return perfil;
	}

	public Perfil modificar (Perfil perfil)
	{
		log.info("Comienza el método modificar");
		Transaction tx = this.perfilDaoH.getSession().beginTransaction();
		try
		{
			perfil = this.perfilDaoH.update(perfil);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return perfil;
	}

	public void deshabilitar (int idPerfil)
	{
		log.info("Comienza el método deshabilitar");
		Transaction tx = this.perfilDaoH.getSession().beginTransaction();
		try
		{
			Perfil perfil = this.perfilDaoH.findById(idPerfil);
			perfil.setMEstado(Constants.DESHABILITADO);
			this.perfilDaoH.update(perfil);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
	}

	public void eliminar (int idPerfil)
	{
		log.info("Comienza el método eliminar");
		Transaction tx = this.perfilDaoH.getSession().beginTransaction();
		try
		{
			this.perfilDaoH.remove(idPerfil);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
	}

	/**
	 * Busca el perfil usando como criterio de busqueda el nombre del mismo.
	 * 
	 * @param name
	 *            El nombre del Perfil
	 * @return {@link Perfil} El Perfil
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public Perfil findByName (String name)
	{
		Perfil profile = null;
		Transaction tx = this.perfilDaoH.getSession().beginTransaction();
		try
		{
			profile = this.perfilDaoH.findByName(name);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return profile;
	}

	/**
	 * Obtiene todos los perfiles dados de alta
	 * 
	 * @return {@link Perfil} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public List<Perfil> findAll ()
	{
		List<Perfil> resultados = null;
		Transaction tx = this.perfilDaoH.getSession().beginTransaction();
		try
		{
			resultados = this.perfilDaoH.findAll();
			if (resultados == null)
			{
				throw new EntityNotFoundException("Perfiles no encontrados");
			}
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return resultados;
	}
}
