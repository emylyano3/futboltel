package com.deportel.componentes.controller;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.FuenteContenidoDaoHibernate;
import com.deportel.componentes.modelo.FuenteContenido;

/**
 * @author Emy
 */
public class FuenteContenidoController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private FuenteContenidoController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static FuenteContenidoController instance;

	public static synchronized FuenteContenidoController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new FuenteContenidoController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger					log	= Logger.getLogger(FuenteContenidoController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca una fuente de contenido a través del codigo de.
	 *
	 * @param codigo
	 *            El codigo de la fuente de contenido
	 * @return La fuente de contenido encontrada o <tt>null</tt> en
	 *         caso de no encontrar ninguna.
	 */
	public FuenteContenido findByCodigoFuenteContenido (Integer codigo)
	{
		log.debug("Buscando la fuente de contenido con codigo: [" + codigo +"]");
		final FuenteContenidoDaoHibernate dao = new FuenteContenidoDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final FuenteContenido fc = dao.findByCodigo(codigo);
			tx.commit();
			return fc;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Busca una fuente de contenido a traves del nombre de la misma
	 *
	 * @param name
	 *            El nombre de la fuente de contenido
	 * @return {@link FuenteContenido}
	 */
	public FuenteContenido findByName (String name)
	{
		final FuenteContenidoDaoHibernate dao = new FuenteContenidoDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final FuenteContenido fc = dao.findByName(name);
			tx.commit();
			return fc;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
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
		final FuenteContenidoDaoHibernate dao = new FuenteContenidoDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final FuenteContenido fuenteContenido = dao.findByXmlTag(tag);
			tx.commit();
			return fuenteContenido;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}
}
