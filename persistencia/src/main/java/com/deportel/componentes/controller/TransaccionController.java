package com.deportel.componentes.controller;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.TransaccionDaoHibernate;
import com.deportel.componentes.modelo.Transaccion;

/**
 * @author Emy
 */
public class TransaccionController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private TransaccionController (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static TransaccionController instance;

	public synchronized static TransaccionController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new TransaccionController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger				log	= Logger.getLogger(TransaccionController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Transaccion findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Obteniendo la Transaccion segun el id");
		final TransaccionDaoHibernate dao = new TransaccionDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Transaccion t = dao.find(id);
			tx.commit();
			return t;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw new InstanceNotFoundException("No se encontro la instancia de [" + Transaccion.class + "] bajo el id [" + id + "]");
		}
	}

	public Transaccion create (Transaccion transaccion)
	{
		log.debug("Creando la entidad de transaccion");
		final TransaccionDaoHibernate dao = new TransaccionDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			transaccion = dao.create(transaccion);
			tx.commit();
			return transaccion;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return transaccion;
		}
	}

	public void clear ()
	{
		final TransaccionDaoHibernate dao = new TransaccionDaoHibernate(this.uniqueSession);
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
