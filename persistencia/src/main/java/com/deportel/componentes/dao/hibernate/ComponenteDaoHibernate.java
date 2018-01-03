package com.deportel.componentes.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.deportel.common.Constants;
import com.deportel.componentes.dao.ComponenteDao;
import com.deportel.componentes.modelo.Componente;

/**
 * @author Emy
 */
public class ComponenteDaoHibernate extends ComponentesGenericDaoHibernate<Componente, Integer> implements ComponenteDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public ComponenteDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Encuentra un componente a través de su codigo de componente.
	 * Recordar que el codigo de componente es un codigo de "negocio"
	 * mientras que el id es una clave subrogada.
	 *
	 * @param codigo
	 *            El codigo del componente
	 * @return El componente encontrado
	 */
	@SuppressWarnings("unchecked")
	public Componente findByCodigo (String codigo)
	{
		try
		{
			final List<Componente> resultados =
				getSession().createCriteria(Componente.class)
				.add(Restrictions.eq("CComponente", codigo))
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

	/**
	 * Busca todos los componentes que son de un determinado tipo.
	 *
	 * @param idTipoComponente
	 *            El id del tipo de componente a buscar
	 * @return Los componentes que pertenecen al tipo solicitado
	 */
	@SuppressWarnings("unchecked")
	public List<Componente> findByIdTipo (Integer idTipoComponente)
	{
		try
		{
			final List<Componente> result =
				getSession().createCriteria(Componente.class)
				.add(Restrictions.eq("tipoComponente.CId", idTipoComponente))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
			return result;
		}
		catch (final Exception e)
		{
			return new ArrayList<Componente>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Componente> findAll ()
	{
		try
		{
			return getSession().createCriteria(Componente.class)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
		}
		catch (final Exception e)
		{
			return new ArrayList<Componente>();
		}
	}

	/**
	 * Busca los componentes filtrando por id de tema e id de tipo de componente. Por el tema de los
	 * resultados repetidos, ver:<br/>
	 * <a href="http://stackoverflow.com/questions/1995080/hibernate-criteria-returns-children
	 * -multiple-times-with-fetchtype-eager">Criteria resultados repetidos</a>
	 *
	 * @param idTema
	 *            El id del tema
	 * @param idTipoComponente
	 *            El id de tipo de componente
	 * @return Lista de componentes que cumplen con el filtro
	 */
	@SuppressWarnings("unchecked")
	public List<Componente> findByIdTemaAndIdTipo (Integer idTema, Integer idTipoComponente, boolean tipoContenido)
	{
		try
		{
			Criteria criteria =
				getSession().createCriteria(Componente.class)
				.add(Restrictions.eq("tema.CId", idTema))
				.add(Restrictions.eq("tipoComponente.CId", idTipoComponente));
			if (tipoContenido)
			{
				criteria = criteria.add(Restrictions.eq("MContenido", Constants.HABILITADO));
			}
			final List<Componente> result = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			return result;
		}
		catch (final Exception e)
		{
			return new ArrayList<Componente>();
		}
	}
}
