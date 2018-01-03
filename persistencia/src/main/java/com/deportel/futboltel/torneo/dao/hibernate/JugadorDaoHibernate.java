package com.deportel.futboltel.torneo.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.futboltel.torneo.dao.JugadorDao;
import com.deportel.futboltel.torneo.modelo.Jugador;

public class JugadorDaoHibernate extends TorneoGenericDaoHibernate<Jugador, Integer> implements JugadorDao
{

	@SuppressWarnings("unchecked")
	public List<Jugador> findByIdEquipo (Integer id)
	{
		List<Jugador> resultados = getSessionAndBeginTransaction().createCriteria(Jugador.class).add(Restrictions.eq("equipo.CId", id)).list();
		return resultados;
	}
}
