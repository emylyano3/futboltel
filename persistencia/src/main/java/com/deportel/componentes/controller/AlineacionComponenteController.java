package com.deportel.componentes.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.AlineacionComponenteDaoHibernate;
import com.deportel.componentes.modelo.AlineacionComponente;

/**
 * @author Emy
 */
public class AlineacionComponenteController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private AlineacionComponenteController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	public static synchronized AlineacionComponenteController getInstance (boolean uniqueSession)
	{
		return new AlineacionComponenteController(uniqueSession);
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger					log	= Logger.getLogger(AlineacionComponenteController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public AlineacionComponente findByCodigo (String codigo)
	{
		log.debug("Buscando todos los componentes");
		final AlineacionComponenteDaoHibernate dao = new AlineacionComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final AlineacionComponente result = dao.findByCodigo(codigo);
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

	public List<AlineacionComponente> findAll ()
	{
		log.debug("Buscando todas las acciones");
		final AlineacionComponenteDaoHibernate dao = new AlineacionComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<AlineacionComponente> result = dao.findAll();
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

	public List<String> getAsNames (List<AlineacionComponente> acciones)
	{
		final List<String> names = new ArrayList<String>(acciones.size());
		for (final AlineacionComponente accionAplicacion : acciones)
		{
			names.add(accionAplicacion.getDNombre());
		}
		return names;
	}

	public List<String> getAsCodes (List<AlineacionComponente> acciones)
	{
		final List<String> codes = new ArrayList<String>(acciones.size());
		for (final AlineacionComponente accionAplicacion : acciones)
		{
			codes.add(accionAplicacion.getCCodigo());
		}
		return codes;
	}
}
