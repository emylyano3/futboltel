package com.deportel.administracion.dao;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import com.deportel.administracion.modelo.PerfilUsuario;

public interface PerfilUsuarioDao extends AdministracionGenericDao<PerfilUsuario, Integer>
{
	public List<PerfilUsuario> findByIdUsuario(Integer userId);
}
