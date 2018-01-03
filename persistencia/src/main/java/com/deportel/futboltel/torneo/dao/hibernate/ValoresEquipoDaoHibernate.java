package com.deportel.futboltel.torneo.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import org.hibernate.criterion.Restrictions;

import com.deportel.futboltel.torneo.dao.ValoresEquipoDao;
import com.deportel.futboltel.torneo.modelo.ValoresEquipo;

public class ValoresEquipoDaoHibernate extends TorneoGenericDaoHibernate<ValoresEquipo, Integer> implements ValoresEquipoDao
{
	public ValoresEquipo findByIdEquipo(Integer id)
	{
		return (ValoresEquipo) getSessionAndBeginTransaction().createCriteria(ValoresEquipo.class).add(Restrictions.eq("equipo.CId", id)).uniqueResult();
	}
}
