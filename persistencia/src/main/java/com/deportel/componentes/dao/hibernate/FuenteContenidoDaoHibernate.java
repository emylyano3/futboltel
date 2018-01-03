package com.deportel.componentes.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.FuenteContenidoDao;
import com.deportel.componentes.modelo.FuenteContenido;

/**
 * @author Emy
 */
public class FuenteContenidoDaoHibernate extends ComponentesGenericDaoHibernate<FuenteContenido, Integer> implements FuenteContenidoDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public FuenteContenidoDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Encuentra una fuente de contenido a través de su codigo.
	 * Recordar que el codigo de fuente de contenido es un codigo de
	 * "negocio" mientras que el id es una clave subrogada.
	 *
	 * @param codigo
	 *            El codigo de la fuente de contenido
	 * @return La fuente de contenido encontrada
	 */
	@SuppressWarnings("unchecked")
	public FuenteContenido findByCodigo (Integer codigo)
	{
		try
		{
			final List<FuenteContenido> resultados = getSession()
			.createCriteria(FuenteContenido.class)
			.add(Restrictions.eq(FuenteContenido.FIELD_SOURCE, codigo))
			.list();
			return resultados.get(0);
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Busca una fuente de contenido a traves del nombre de la misma.
	 *
	 * @param name
	 *            El nombre de la fuente de contenido
	 * @return {@link FuenteContenido}
	 */
	public FuenteContenido findByName (String name)
	{
		try
		{
			final FuenteContenido fc = (FuenteContenido) getSession()
			.createCriteria(FuenteContenido.class)
			.add(Restrictions.like(FuenteContenido.FIELD_NAME,  name))
			.uniqueResult();
			return fc;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Busca una fuente de contenido a traves de tag XML que la representa.
	 *
	 * @param name
	 *            El nombre de la fuente de contenido
	 * @return {@link FuenteContenido}
	 */
	public FuenteContenido findByXmlTag (String tag)
	{
		try
		{
			final FuenteContenido fc = (FuenteContenido) getSession()
			.createCriteria(FuenteContenido.class)
			.add(Restrictions.like(FuenteContenido.FIELD_XML_CHAR,  tag))
			.uniqueResult();
			return fc;
		}
		catch (final Exception e)
		{
			return null;
		}
	}
}
