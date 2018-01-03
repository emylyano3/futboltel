package com.deportel.componentes.controller;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.ClienteDaoHibernate;
import com.deportel.componentes.modelo.Cliente;

/**
 * @author Emy
 */
public class ClienteController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private ClienteController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static ClienteController instance;

	public static synchronized ClienteController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new ClienteController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger				log	= Logger.getLogger(ClienteController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Cliente findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Obteniendo el cliente segun el id");
		final ClienteDaoHibernate dao = new ClienteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Cliente cliente = dao.find(id);
			tx.commit();
			return cliente;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public Cliente create (Cliente cliente)
	{
		log.debug("Creando la entidad de Cliente");
		final ClienteDaoHibernate dao = new ClienteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			cliente = dao.create(cliente);
			tx.commit();
			return cliente;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return cliente;
		}
	}

	public void remove (Integer id)
	{
		log.debug("Creando la entidad de Suscripcion");
		final ClienteDaoHibernate dao = new ClienteDaoHibernate(this.uniqueSession);
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
		final ClienteDaoHibernate dao = new ClienteDaoHibernate(this.uniqueSession);
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
