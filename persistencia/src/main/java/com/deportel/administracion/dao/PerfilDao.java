package com.deportel.administracion.dao;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import com.deportel.administracion.modelo.Perfil;

public interface PerfilDao extends AdministracionGenericDao<Perfil, Integer>
{
	public List<Perfil> findByPerfilId (Integer perfilId, int startIndex, int count);

	public Perfil findByName(String name);

	public List<Perfil> findAll();
}
