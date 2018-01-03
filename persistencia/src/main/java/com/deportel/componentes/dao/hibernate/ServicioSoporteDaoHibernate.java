package com.deportel.componentes.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.ServicioSoporteDao;
import com.deportel.componentes.modelo.ServicioSoporte;

/**
 * @author Emy
 */
public class ServicioSoporteDaoHibernate extends ComponentesGenericDaoHibernate<ServicioSoporte, Integer> implements ServicioSoporteDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public ServicioSoporteDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioSoporte> findAll ()
	{
		try
		{
			final List<ServicioSoporte> preguntas = getSession().createCriteria(ServicioSoporte.class).list();
			return preguntas;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 *
	 * @return
	 */
	public ServicioSoporte findByName (String name)
	{
		try
		{
			final ServicioSoporte pregunta = (ServicioSoporte)
			getSession().createCriteria(ServicioSoporte.class)
			.add(Restrictions.eq(ServicioSoporte.FIELD_NAME, name))
			.uniqueResult();
			return pregunta;
		}
		catch (final Exception e)
		{
			return null;
		}
	}
}
