package com.deportel.administracion.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.ModuloDao;
import com.deportel.administracion.modelo.Modulo;
import com.deportel.common.Constants;

public class ModuloDaoHibernate extends AdministracionGenericDaoHibernate<Modulo, Integer> implements ModuloDao
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized ModuloDaoHibernate getInstance()
	{
		if (instance == null)
		{
			instance = new ModuloDaoHibernate();
		}
		return instance;
	}

	private ModuloDaoHibernate ()
	{

	}

	private static ModuloDaoHibernate	instance;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca si existe el nombre del modulo pasado por parametro
	 * 
	 * @param nombre
	 * @return Los datos del modulo
	 * @author Emy
	 * @since 01/02/2011
	 */
	@Override
	public Modulo findByName(String name)
	{
		Modulo modulo = (Modulo) this.getSession().createCriteria(Modulo.class)
		.add(Restrictions.eq(Modulo.FIELD_NAME, name))
		.add(Restrictions.eq(Modulo.FIELD_STATE, Constants.HABILITADO))
		.uniqueResult();
		return modulo;
	}

	/**
	 * Obtiene todos los modulos dados de alta
	 * 
	 * @return {@link Modulo} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Modulo> findAll()
	{
		List<Modulo> resultados =
			this.getSession().createCriteria(Modulo.class)
			.add(Restrictions.eq(Modulo.FIELD_STATE, Constants.HABILITADO))
			.list();
		return resultados;
	}

	@Override
	public void onPreCreated(Modulo modulo)
	{
		modulo.setMEstado(Constants.HABILITADO);
	}

	@Override
	protected void onCreated (Modulo entity)
	{

	}

	@Override
	protected void onUpdate (Modulo entity)
	{

	}

	@Override
	protected void onPreRemove (Modulo entity)
	{

	}

}
