package com.deportel.componentes.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.AlineacionComponenteDao;
import com.deportel.componentes.modelo.AlineacionComponente;

/**
 * @author Emy
 */
public class AlineacionComponenteDaoHibernate extends ComponentesGenericDaoHibernate<AlineacionComponente, Integer> implements AlineacionComponenteDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public AlineacionComponenteDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	@SuppressWarnings("unchecked")
	public AlineacionComponente findByCodigo (String codigo)
	{
		try
		{
			final List<AlineacionComponente> resultados =
				getSession().createCriteria(AlineacionComponente.class)
				.add(Restrictions.eq("CCodigo", codigo))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
			if (resultados == null || resultados.isEmpty())
			{
				return null;
			}
			return resultados.get(0);
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlineacionComponente> findAll ()
	{
		try
		{
			return getSession().createCriteria(AlineacionComponente.class)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
		}
		catch (final Exception e)
		{
			return new ArrayList<AlineacionComponente>();
		}
	}
}
