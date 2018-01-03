package com.deportel.componentes.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.AccionComponenteDaoHibernate;
import com.deportel.componentes.modelo.AccionComponente;

/**
 * @author Emy
 */
public class AccionComponenteController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private AccionComponenteController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	public static synchronized AccionComponenteController getInstance (boolean uniqueSession)
	{
		return new AccionComponenteController(uniqueSession);
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger					log	= Logger.getLogger(AccionComponenteController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public AccionComponente findByCodigo (String codigo)
	{
		log.debug("Buscando todos los componentes");
		final AccionComponenteDaoHibernate dao = new AccionComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final AccionComponente result = dao.findByCodigo(codigo);
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	public List<AccionComponente> findInCodes (Collection<String> codes)
	{
		log.debug("Buscando todas las acciones");
		final AccionComponenteDaoHibernate dao = new AccionComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<AccionComponente> result = dao.findInCodes(codes);
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}
	public List<AccionComponente> findAll ()
	{
		log.debug("Buscando todas las acciones");
		final AccionComponenteDaoHibernate dao = new AccionComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<AccionComponente> result = dao.findAll();
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getAsNames (List<AccionComponente> acciones)
	{
		final List<String> names = new ArrayList<String>(acciones.size());
		for (final AccionComponente accionAplicacion : acciones)
		{
			names.add(accionAplicacion.getDNombre());
		}
		return names;
	}

	public List<String> getAsCodes (List<AccionComponente> acciones)
	{
		final List<String> codes = new ArrayList<String>(acciones.size());
		for (final AccionComponente accionAplicacion : acciones)
		{
			codes.add(accionAplicacion.getCCodigo());
		}
		return codes;
	}
}
