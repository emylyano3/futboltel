package com.deportel.componentes.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.TipoPropiedadDao;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.TipoPropiedad;

/**
 * @author Emy
 */
public class TipoPropiedadDaoHibernate extends ComponentesGenericDaoHibernate<TipoPropiedad, Integer> implements TipoPropiedadDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public TipoPropiedadDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Encuentra un tipo de propiedad a través de su codigo.
	 * Recordar que el codigo de tipo de porpiedad es un codigo de
	 * "negocio" mientras que el id es una clave subrogada.
	 *
	 * @param codigo
	 *            El codigo del tipo de propiedad
	 * @return El tipo de propiedad encontrado
	 */
	@SuppressWarnings("unchecked")
	public TipoPropiedad findByCodigo (Integer codigo)
	{
		try
		{
			final List<TipoPropiedad> resultados = getSession().createCriteria(TipoPropiedad.class).add(Restrictions.eq("CTipoPropiedad", codigo)).list();
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
	public TipoPropiedad findByName (String name)
	{
		try
		{
			final TipoPropiedad tipoPropiedad = (TipoPropiedad) getSession()
			.createCriteria(TipoPropiedad.class)
			.add(Restrictions.like(TipoPropiedad.FIELD_NAME,  name))
			.uniqueResult();
			return tipoPropiedad;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/**
	 * Busca una fuente de contenido a traves del tag XML que la representa.
	 *
	 * @param tag
	 *            El nombre de la fuente de contenido
	 * @return {@link FuenteContenido}
	 */
	public TipoPropiedad findByXmlTag (String tag)
	{
		try
		{
			final TipoPropiedad tipoPropiedad = (TipoPropiedad) getSession()
			.createCriteria(TipoPropiedad.class)
			.add(Restrictions.like(TipoPropiedad.FIELD_XML_TAG,  tag))
			.uniqueResult();
			return tipoPropiedad;
		}
		catch (final Exception e)
		{
			return null;
		}
	}
}
