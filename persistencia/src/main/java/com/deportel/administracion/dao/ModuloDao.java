package com.deportel.administracion.dao;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import com.deportel.administracion.modelo.Modulo;

public interface ModuloDao extends AdministracionGenericDao<Modulo, Integer>
{
	public Modulo findByName(String name);

	public List<Modulo> findAll();
}
