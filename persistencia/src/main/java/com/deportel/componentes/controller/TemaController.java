package com.deportel.componentes.controller;

import java.util.List;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.TemaDaoHibernate;
import com.deportel.componentes.modelo.Tema;

/**
 * @author Emy
 */
public class TemaController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private TemaController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static TemaController instance;

	public synchronized static TemaController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new TemaController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger		log	= Logger.getLogger(TemaController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Tema create (Tema tema)
	{
		log.debug("Se crea un nuevo tema: " + tema);
		final TemaDaoHibernate dao = new TemaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			tema = dao.create(tema);
			tx.commit();
			return tema;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public Tema findByName (String name)
	{
		log.debug("Obteniendo el tema segun el nombre");
		final TemaDaoHibernate dao = new TemaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Tema tema = dao.findByName(name);
			tx.commit();
			return tema;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public Tema findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Obteniendo el tema segun el id");
		final TemaDaoHibernate dao = new TemaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Tema tema = dao.find(id);
			tx.commit();
			return tema;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public List<Tema> findAll ()
	{
		log.debug("Obteniendo todos los temas");
		final TemaDaoHibernate dao = new TemaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<Tema> temas = dao.findAll();
			tx.commit();
			return temas;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public void clear ()
	{
		final TemaDaoHibernate dao = new TemaDaoHibernate(this.uniqueSession);
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
