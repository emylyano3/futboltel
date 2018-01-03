package com.deportel.componentes.controller;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.SuscripcionDaoHibernate;
import com.deportel.componentes.modelo.Suscripcion;

/**
 * @author Emy
 */
public class SuscripcionController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private SuscripcionController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static SuscripcionController instance;

	public synchronized static SuscripcionController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new SuscripcionController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger				log	= Logger.getLogger(SuscripcionController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Suscripcion findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Obteniendo la suscripcion por el id");
		final SuscripcionDaoHibernate dao = new SuscripcionDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Suscripcion s = dao.find(id);
			tx.commit();
			return s;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public Suscripcion create (Suscripcion suscripcion)
	{
		log.debug("Creando la entidad de Suscripcion");
		final SuscripcionDaoHibernate dao = new SuscripcionDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			suscripcion = dao.create(suscripcion);
			tx.commit();
			return suscripcion;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return suscripcion;
		}
	}

	public void remove (Integer id)
	{
		log.debug("Creando la entidad de Suscripcion");
		final SuscripcionDaoHibernate dao = new SuscripcionDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.remove(id);
			tx.commit();
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
		}
	}

	public void clear ()
	{
		final SuscripcionDaoHibernate dao = new SuscripcionDaoHibernate(this.uniqueSession);
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
