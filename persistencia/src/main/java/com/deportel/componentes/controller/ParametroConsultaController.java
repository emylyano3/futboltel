package com.deportel.componentes.controller;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.ParametroConsultaDaoHibernate;
import com.deportel.componentes.modelo.ParametroConsulta;

/**
 * @author Emy
 */
public class ParametroConsultaController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private ParametroConsultaController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static ParametroConsultaController instance;

	public static ParametroConsultaController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new ParametroConsultaController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger					log	= Logger.getLogger(ParametroConsulta.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public ParametroConsulta create (ParametroConsulta parametro)
	{
		log.debug("Creando una ConsultaDinamica");
		final ParametroConsultaDaoHibernate dao = new ParametroConsultaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final ParametroConsulta pc = dao.create(parametro);
			tx.commit();
			return pc;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public void clear ()
	{
		final ParametroConsultaDaoHibernate dao = new ParametroConsultaDaoHibernate(this.uniqueSession);
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


