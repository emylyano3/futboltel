package com.deportel.componentes.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.ParametroConsultaDao;
import com.deportel.componentes.modelo.ParametroConsulta;

/**
 * @author Emy
 */
public class ParametroConsultaDaoHibernate extends ComponentesGenericDaoHibernate<ParametroConsulta, Integer> implements ParametroConsultaDao
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	public ParametroConsultaDaoHibernate (boolean uniqueSession)
	{
		super(uniqueSession);
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger	log	= Logger.getLogger(ParametroConsultaDaoHibernate.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 *
	 * @param idConsulta
	 *            El codigo de la ConsultaDinamica
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ParametroConsulta> findByIdConsultaDinamica (Integer idConsulta)
	{
		final List<ParametroConsulta> parametros = getSession().createCriteria(ParametroConsulta.class).add(Restrictions.eq("consultaDinamica.CId", idConsulta)).list();
		return parametros;
	}

	/**
	 *
	 */
	protected void removeRelated (Session session, Integer idConsulta)
	{
		log.debug("Eliminando los parametros de la consulta dinamica con id [" + idConsulta + "]");
		final List<?> parametters = session.createCriteria(ParametroConsulta.class).add(Restrictions.eq("consultaDinamica.CId", idConsulta)).list();
		for (final Object name : parametters)
		{
			session.delete(name);
		}
	}
}
