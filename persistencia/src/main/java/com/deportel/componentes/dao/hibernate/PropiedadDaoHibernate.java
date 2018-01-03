package com.deportel.componentes.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.PropiedadDao;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;

/**
 * @author Emy
 */
public class PropiedadDaoHibernate extends ComponentesGenericDaoHibernate<Propiedad, Integer> implements PropiedadDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public PropiedadDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	/**
	 *
	 * @param idTipoPropiedad
	 * @param idComponente
	 * @return
	 */
	public Propiedad findByIdTipoPropiedadIdComponente (Integer idTipoPropiedad, Integer idComponente)
	{
		try
		{
			final Propiedad propiedad = (Propiedad) getSession().
			createCriteria(Propiedad.class).
			add(Restrictions.eq("componente.CId", idComponente)).
			add(Restrictions.eq("tipoPropiedad.CId", idTipoPropiedad)).
			uniqueResult();
			return propiedad;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Propiedad> propertiesFilteredByComponentTypeAndSource (Componente component, TipoPropiedad type, FuenteContenido source)
	{
		try
		{
			final List<Propiedad> result = getSession().createCriteria(Propiedad.class)
			.add(Restrictions.eq("componente.CId", component.getCId()))
			.add(Restrictions.eq("tipoPropiedad.CId", type.getCId()))
			.add(Restrictions.eq("fuenteContenido.CId", source.getCId()))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
			return result;
		}
		catch (final Exception e)
		{
			return new ArrayList<Propiedad>();
		}
	}

	/**
	 *
	 * @param idComponente
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Propiedad> findByIdComponente (Integer idComponente)
	{
		try
		{
			final List<Propiedad> propiedades = getSession().
			createCriteria(Propiedad.class).
			add(Restrictions.eq("componente.CId", idComponente)).
			list();
			return propiedades;
		}
		catch (final Exception e)
		{
			return null;
		}
	}
}
