package com.deportel.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.ComponenteDaoHibernate;
import com.deportel.futboltel.torneo.dao.hibernate.TorneoDaoHibernate;
import com.deportel.persistencia.utils.QueryParam;

public class GlobalConsultasController
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	public GlobalConsultasController(boolean uniqueSession)
	{
		this.torneoDao = new TorneoDaoHibernate();
		this.componenteDao = new ComponenteDaoHibernate(uniqueSession);
	}

	private static final Logger				log	= Logger.getLogger(GlobalConsultasController.class);

	private final TorneoDaoHibernate		torneoDao;
	private final ComponenteDaoHibernate	componenteDao;

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public List<?> executeCustomQuery (String query, List<QueryParam> params) throws HibernateException
	{
		log.info("Ejecutando la consulta \n" + query + " \nCon los siguientes parametros " + params);
		List<?> result = null;
		try
		{
			result = executeInTorneoSchema(query, params);
		}
		catch (final HibernateException e)
		{
			log.warn("Fallo la ejecucion de la consulta customizada en el esquema Torneo. Error causado por: " + e.getMessage());
			result = executeInComponentesSchema(query, params);
		}
		return result;
	}

	private List<?> executeInTorneoSchema (String query, List<QueryParam> params) throws HibernateException
	{
		log.debug("Ejecuanto la consulta customizada en el esquema torneo");
		return this.torneoDao.excuteCustomQuery(query, params);
	}

	private List<?> executeInComponentesSchema (String query, List<QueryParam> params) throws HibernateException
	{
		log.debug("Ejecuanto la consulta customizada en el esquema componente");
		final Transaction tx = this.componenteDao.getSession().beginTransaction();
		try
		{
			final List<?> result = this.componenteDao.excuteCustomQuery(query, params);;
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			throw new HibernateException(e);
		}
	}

}
