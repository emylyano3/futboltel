package com.deportel.componentes.controller;

import java.util.List;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.ServicioSoporteDaoHibernate;
import com.deportel.componentes.modelo.ServicioSoporte;

/**
 * @author Emy
 */
public class ServicioSoporteController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private ServicioSoporteController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static ServicioSoporteController instance;

	public synchronized static ServicioSoporteController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new ServicioSoporteController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger					log	= Logger.getLogger(ServicioSoporteController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public ServicioSoporte findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Obteniendo la entidad de servicio de soporte por id");
		final ServicioSoporteDaoHibernate dao = new ServicioSoporteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final ServicioSoporte s = dao.find(id);
			tx.commit();
			return s;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public List<ServicioSoporte> findAll ()
	{
		log.debug("Obteniendo todos los temas");
		final ServicioSoporteDaoHibernate dao = new ServicioSoporteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<ServicioSoporte> preguntas = dao.findAll();
			tx.commit();
			return preguntas;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public ServicioSoporte findByName (String name)
	{
		log.debug("Obteniendo el Servicio de soporte con nombre: " + name);
		final ServicioSoporteDaoHibernate dao = new ServicioSoporteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final ServicioSoporte pregunta = dao.findByName(name);
			tx.commit();
			return pregunta;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public ServicioSoporte create (ServicioSoporte suscripcion)
	{
		log.debug("Creando la entidad de ServicioSoporte");
		final ServicioSoporteDaoHibernate dao = new ServicioSoporteDaoHibernate(this.uniqueSession);
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
		log.debug("Creando la entidad de ServicioSoporte");
		final ServicioSoporteDaoHibernate dao = new ServicioSoporteDaoHibernate(this.uniqueSession);
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
		final ServicioSoporteDaoHibernate dao = new ServicioSoporteDaoHibernate(this.uniqueSession);
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
