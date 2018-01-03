package com.deportel.componentes.controller;

import java.util.List;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.common.Constants;
import com.deportel.componentes.dao.hibernate.TipoComponenteDaoHibernate;
import com.deportel.componentes.modelo.TipoComponente;

/**
 * @author Emy
 */
public class TipoComponenteController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private TipoComponenteController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static TipoComponenteController instance;

	public synchronized static TipoComponenteController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new TipoComponenteController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger					log	= Logger.getLogger(TipoComponenteController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca un tipo de componente a través del id del mismo.
	 *
	 * @param id
	 *            El id de tipo de componente
	 * @return El tipo componente encontrado o <tt>null</tt> en caso
	 *         de no encontrar ningun tipo de componente que cumpla
	 *         con el id enviado
	 * @throws InstanceNotFoundException
	 */
	public TipoComponente findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Buscando el componente con id: [" + id +"]");
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoComponente tc = dao.find(id);
			tx.commit();
			return tc;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public void remove (Integer id) throws InstanceNotFoundException
	{
		log.debug("Eliminando el componente con id: [" + id +"]");
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.remove(id);
			tx.commit();
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
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
		log.debug("Buscando el componente con nombre: [" + nombre +"]");
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoComponente tc = dao.findByNombre(nombre);
			tx.commit();
			return tc;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	/**
	 *
	 * @return
	 */
	public List<TipoComponente> findAll ()
	{
		log.debug("Buscando todos los tipo de componentes");
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<TipoComponente> result = dao.findAll();
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	/**
	 *
	 * @return
	 */
	public List<TipoComponente> findContentUsableComponents ()
	{
		log.debug("Buscando todos los tipo de componentes");
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<TipoComponente> componentTypes = dao.findAll();
			for (int i = componentTypes.size() - 1; i >=0; i--)
			{
				if (Constants.DESHABILITADO.equalsIgnoreCase(componentTypes.get(i).getMSirveContenido()))
				{
					componentTypes.remove(i);
				}
			}
			tx.commit();
			return componentTypes;
		}
		catch (final Exception e)
		{
			tx.rollback();
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
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoComponente result = dao.findByXmlTag(tag);
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public void clear ()
	{
		final TipoComponenteDaoHibernate dao = new TipoComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.clear();
			tx.commit();
		}
		catch (final Exception e)
		{
			tx.rollback();
		}
	}
}
