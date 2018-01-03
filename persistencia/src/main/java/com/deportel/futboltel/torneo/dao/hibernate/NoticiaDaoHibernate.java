package com.deportel.futboltel.torneo.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.futboltel.torneo.dao.NoticiaDao;
import com.deportel.futboltel.torneo.modelo.Categoria;
import com.deportel.futboltel.torneo.modelo.Equipo;
import com.deportel.futboltel.torneo.modelo.Noticia;

public class NoticiaDaoHibernate extends TorneoGenericDaoHibernate<Noticia, Integer> implements NoticiaDao
{

	@Override
	@SuppressWarnings("unchecked")
	public List<Noticia> findAll ()
	{
		List<Noticia> resultados = getSessionAndBeginTransaction().createCriteria(Noticia.class).list();
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<Noticia> findByCategoria (Categoria categoria)
	{
		List<Noticia> resultados = getSessionAndBeginTransaction()
		.createCriteria(Noticia.class)
		.add(Restrictions.eq("categoria.CId", categoria.getCId()))
		.list();
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<Noticia> findByEquipo (Equipo equipo)
	{
		List<Noticia> resultados = getSessionAndBeginTransaction()
		.createCriteria(Noticia.class)
		.add(Restrictions.eq("equipo.CId", equipo.getCId()))
		.list();
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<Noticia> findByCategoriaEquipo (Categoria categoria, Equipo equipo)
	{
		List<Noticia> resultados = getSessionAndBeginTransaction()
		.createCriteria(Noticia.class)
		.add(Restrictions.eq("categoria.CId", categoria.getCId()))
		.add(Restrictions.eq("equipo.CId", equipo.getCId()))
		.list();
		return resultados;
	}
}
