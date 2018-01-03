package com.deportel.futboltel.torneo.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.common.Constants;
import com.deportel.futboltel.torneo.dao.EquipoDao;
import com.deportel.futboltel.torneo.modelo.Equipo;

public class EquipoDaoHibernate extends TorneoGenericDaoHibernate<Equipo, Integer> implements EquipoDao
{

	@Override
	@SuppressWarnings("unchecked")
	public List<Equipo> findAll ()
	{
		List<Equipo> resultados = getSessionAndBeginTransaction()
		.createCriteria(Equipo.class)
		.add(Restrictions.eq(Equipo.FIELD_STATE, Constants.HABILITADO))
		.list();
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<Equipo> findByIdCategoria (Integer id)
	{
		List<Equipo> resultados = getSessionAndBeginTransaction()
		.createCriteria(Equipo.class)
		.add(Restrictions.eq("categoria.CId", id))
		.list();
		return resultados;
	}
}
