package com.deportel.administracion.dao;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import com.deportel.administracion.modelo.Usuario;

public interface UsuarioDao extends AdministracionGenericDao<Usuario, Integer>
{

	public Usuario findByName (String userName);

	public Usuario findByAlias(String alias);

	public List<Usuario> findAll();
}
