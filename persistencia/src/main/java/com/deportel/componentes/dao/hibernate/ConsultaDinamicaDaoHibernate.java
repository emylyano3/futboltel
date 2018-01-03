package com.deportel.componentes.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.dao.ConsultaDinamicaDao;
import com.deportel.componentes.modelo.ConsultaDinamica;

/**
 * @author Emy
 */
public class ConsultaDinamicaDaoHibernate extends ComponentesGenericDaoHibernate<ConsultaDinamica, Integer> implements ConsultaDinamicaDao
{
	private static final Logger log = Logger.getLogger(ConsultaDinamicaDaoHibernate.class);

	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public ConsultaDinamicaDaoHibernate(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@SuppressWarnings("unchecked")
	public List<ConsultaDinamica> findAll () throws HibernateException
	{
		final List<ConsultaDinamica> result =
			getSession().createCriteria(ConsultaDinamica.class)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.list();
		return result;
	}

	public ConsultaDinamica findByName (String name) throws HibernateException
	{
		final ConsultaDinamica result = (ConsultaDinamica)
		getSession().createCriteria(ConsultaDinamica.class)
		.add(Restrictions.eq(ConsultaDinamica.FIELD_NAME, name))
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.uniqueResult();
		return result;
	}

	public ConsultaDinamica findByIdSolicitante (Integer idSolicitante) throws HibernateException
	{
		@SuppressWarnings("unchecked")
		final
		List<ConsultaDinamica> consultas = getSession().createCriteria(ConsultaDinamica.class)
		.add(Restrictions.eq("componenteSolicitante.CId", idSolicitante))
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
		if (!Utils.isNullOrEmpty(consultas))
		{
			if (consultas.size() > 1)
			{
				log.warn("ATENCION! Más de una consulta dinámica para el id de solicitante: " + idSolicitante + ". Se devuelve el primer resultado obtenido.");
			}
			return consultas.get(0);
		}
		return null;
	}

	/**
	 * @param entity
	 */
	@Override
	protected void onPreRemove(Session session, ConsultaDinamica entity)
	{
		new ParametroConsultaDaoHibernate(this.uniqueSession).removeRelated(session, entity.getCId());
	}
}
