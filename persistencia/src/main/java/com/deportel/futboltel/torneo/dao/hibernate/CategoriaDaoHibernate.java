package com.deportel.futboltel.torneo.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.common.Constants;
import com.deportel.futboltel.torneo.dao.CategoriaDao;
import com.deportel.futboltel.torneo.modelo.Categoria;

public class CategoriaDaoHibernate extends TorneoGenericDaoHibernate<Categoria, Integer> implements CategoriaDao
{

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findAll()
	{
		List<Categoria> resultados = getSessionAndBeginTransaction()
		.createCriteria(Categoria.class)
		.add(Restrictions.eq(Categoria.FIELD_STATE, Constants.HABILITADO))
		.list();
		return resultados;
	}

}
