package com.deportel.componentes.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.common.Constants;
import com.deportel.componentes.dao.TemaDao;
import com.deportel.componentes.modelo.Tema;

/**
 * @author Emy
 */
public class TemaDaoHibernate extends ComponentesGenericDaoHibernate<Tema, Integer> implements TemaDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public TemaDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	/**
	 * Busca si existe el nombre de usuario pasado por parametro
	 *
	 * @param themeName
	 * @return El tema buscado
	 * @since 16/09/2011
	 */
	public Tema findByName (String themeName)
	{
		try
		{
			final Tema tema = (Tema) getSession()
			.createCriteria(Tema.class)
			.add(Restrictions.like(Tema.FIELD_NAME, themeName))
			.add(Restrictions.eq(Tema.FIELD_STATE, Constants.HABILITADO))
			.uniqueResult();
			return tema;
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
	@SuppressWarnings("unchecked")
	public List<Tema> findAll ()
	{
		try
		{
			final List<Tema> temas = getSession().createCriteria(Tema.class).list();
			return temas;
		}
		catch (final Exception e)
		{
			return null;
		}
	}
}
