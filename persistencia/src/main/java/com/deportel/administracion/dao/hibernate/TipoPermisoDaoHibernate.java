package com.deportel.administracion.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.TipoPermisoDao;
import com.deportel.administracion.modelo.TipoPermiso;

public class TipoPermisoDaoHibernate extends AdministracionGenericDaoHibernate<TipoPermiso, Integer> implements TipoPermisoDao
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized TipoPermisoDaoHibernate getInstance()
	{
		if (instance == null)
		{
			instance = new TipoPermisoDaoHibernate();
		}
		return instance;
	}

	private TipoPermisoDaoHibernate	()
	{

	}

	private static TipoPermisoDaoHibernate	instance;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca si existe el nombre de tipo de permiso pasado por parametro
	 * 
	 * @param nombre del tipo de permiso
	 * @return Los datos del tipo de permiso
	 * @author Emy
	 * @since 01/02/2011
	 */

	@Override
	public TipoPermiso findByName(String name)
	{
		TipoPermiso tipoPermiso = (TipoPermiso) this.getSession()
		.createCriteria(TipoPermiso.class)
		.add(Restrictions.eq(TipoPermiso.FIELD_NAME, name))
		.uniqueResult();

		return tipoPermiso;
	}

	/**
	 * Obtiene todos los tipo de permiso dados de alta
	 * 
	 * @return {@link TipoPermiso} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoPermiso> findAll()
	{
		List<TipoPermiso> resultados = this.getSession().createCriteria(TipoPermiso.class).list();

		return resultados;
	}

	@Override
	protected void onPreCreated (TipoPermiso entity)
	{

	}

	@Override
	protected void onCreated (TipoPermiso entity)
	{

	}

	@Override
	protected void onUpdate (TipoPermiso entity)
	{

	}

	@Override
	protected void onPreRemove (TipoPermiso entity)
	{

	}

}
