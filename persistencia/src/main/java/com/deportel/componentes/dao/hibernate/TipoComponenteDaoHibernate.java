package com.deportel.componentes.dao.hibernate;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.modelo.Usuario;
import com.deportel.componentes.dao.TipoComponenteDao;
import com.deportel.componentes.modelo.TipoComponente;

/**
 * @author Emy
 */
public class TipoComponenteDaoHibernate extends ComponentesGenericDaoHibernate<TipoComponente, Integer> implements TipoComponenteDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public TipoComponenteDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Obtiene todos los tipo de componentes dados de alta
	 *
	 * @return {@link Usuario} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	@SuppressWarnings("unchecked")
	public List<TipoComponente> findAll()
	{
		try
		{
			final List<TipoComponente> resultados = getSession().createCriteria(TipoComponente.class).list();
			return resultados;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Busca un tipo de componente a través del nombre del mismo.
	 *
	 * @param nombre
	 *            El nombre de tipo de componente
	 * @return El tipo componente encontrado o <tt>null</tt> en caso
	 *         de no encontrar ningun tipo de componente que cumpla
	 *         con el nombre enviado
	 */
	public TipoComponente findByNombre (String nombre)
	{
		try
		{
			final TipoComponente tipoComponente = (TipoComponente) getSession()
			.createCriteria(TipoComponente.class)
			.add(Restrictions.eq(TipoComponente.FIELD_NAME, nombre))
			.uniqueResult();
			return tipoComponente;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Busca un tipo de componente a través del tag XML que lo representa.
	 *
	 * @param tag
	 *            El tag xml que lo representa
	 * @return El tipo componente encontrado o <tt>null</tt> en caso
	 *         de no encontrar ningun tipo de componente que cumpla
	 *         con el tag recibido
	 */
	public TipoComponente findByXmlTag (String tag)
	{
		try
		{
			final TipoComponente tipoComponente = (TipoComponente) getSession()
			.createCriteria(TipoComponente.class)
			.add(Restrictions.eq(TipoComponente.FIELD_XML_TAG, tag))
			.uniqueResult();
			return tipoComponente;
		}
		catch (final Exception e)
		{
			return null;
		}
	}
}
