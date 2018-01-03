package com.deportel.componentes.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.deportel.common.Constants;
import com.deportel.componentes.dao.AccionComponenteDao;
import com.deportel.componentes.modelo.AccionComponente;

/**
 * @author Emy
 */
public class AccionComponenteDaoHibernate extends ComponentesGenericDaoHibernate<AccionComponente, Integer> implements AccionComponenteDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public AccionComponenteDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	@SuppressWarnings("unchecked")
	public AccionComponente findByCodigo (String codigo)
	{
		try
		{
			final List<AccionComponente> resultados =
				getSession().createCriteria(AccionComponente.class)
				.add(Restrictions.eq("CCodigo", codigo))
				.add(Restrictions.eq("MHabilitada", Constants.HABILITADO))
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
	public List<AccionComponente> findInCodes (Collection<String> codes)
	{
		try
		{
			return getSession().createCriteria(AccionComponente.class)
			.add(Restrictions.in("CCodigo", codes))
			.list();
		}
		catch (final Exception e)
		{
			return new ArrayList<AccionComponente>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccionComponente> findAll ()
	{
		try
		{
			return getSession().createCriteria(AccionComponente.class)
			.add(Restrictions.eq("MHabilitada", Constants.HABILITADO))
			.list();
		}
		catch (final Exception e)
		{
			return new ArrayList<AccionComponente>();
		}
	}
}
