package com.deportel.administracion.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.administracion.dao.hibernate.TipoPermisoDaoHibernate;
import com.deportel.administracion.modelo.TipoPermiso;

public class TipoPermisoController
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public synchronized static TipoPermisoController getInstance ()
	{
		if (instance == null)
		{
			instance = new TipoPermisoController();
		}
		return instance;
	}

	private TipoPermisoController()
	{

	}

	private static TipoPermisoController	instance;

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static Logger					log						= Logger.getLogger(TipoPermisoController.class);

	private final TipoPermisoDaoHibernate	tipoPermisoDaoHibernate	= TipoPermisoDaoHibernate.getInstance();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public TipoPermiso crear (TipoPermiso tipoPermiso)
	{
		log.info("Comienza el método crear");
		Transaction tx = this.tipoPermisoDaoHibernate.getSession().beginTransaction();
		try
		{
			tipoPermiso = this.tipoPermisoDaoHibernate.create(tipoPermiso);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return tipoPermiso;
	}

	public TipoPermiso modificar (TipoPermiso tipoPermiso)
	{
		log.info("Comienza el método modificar");
		Transaction tx = this.tipoPermisoDaoHibernate.getSession().beginTransaction();
		try
		{
			tipoPermiso = this.tipoPermisoDaoHibernate.update(tipoPermiso);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
		return tipoPermiso;
	}

	public void eliminar (int idTipoPermiso)
	{
		log.info("Comienza el método eliminar");
		Transaction tx = this.tipoPermisoDaoHibernate.getSession().beginTransaction();
		try
		{
			this.tipoPermisoDaoHibernate.remove(idTipoPermiso);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		tx.commit();
	}

	/**
	 * Busca el tipo de permiso usando como criterio de busqueda el nombre del mismo.@param name, El nombre
	 * del tipo de permiso
	 * 
	 * @return {@link TipoPermiso} El TipoPermiso
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public TipoPermiso findByName (String name) throws RuntimeException
	{
		TipoPermiso tipoPermiso = null;
		Transaction tx = this.tipoPermisoDaoHibernate.getSession().beginTransaction();
		try
		{
			tipoPermiso = this.tipoPermisoDaoHibernate.findByName(name);
		}
		catch (RuntimeException e)
		{
			tx.rollback();
			return tipoPermiso;
		}
		tx.commit();
		return tipoPermiso;
	}

	/**
	 * Obtiene todos los tipo de permiso dados de alta
	 * 
	 * @return {@link TipoPermiso} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	public List<TipoPermiso> findAll ()
	{
		List<TipoPermiso> resultados = null;
		Transaction tx = this.tipoPermisoDaoHibernate.getSession().beginTransaction();
		try
		{
			resultados = this.tipoPermisoDaoHibernate.findAll();
			if (resultados == null)
			{
				log.error("Tipo Permiso no encontrado");
				throw new EntityNotFoundException("Tipo Permiso no encontrado");
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
