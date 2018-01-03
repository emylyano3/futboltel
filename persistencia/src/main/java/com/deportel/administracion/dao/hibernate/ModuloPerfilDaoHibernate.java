package com.deportel.administracion.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.ModuloPerfilDao;
import com.deportel.administracion.modelo.ModuloPerfil;

public class ModuloPerfilDaoHibernate extends AdministracionGenericDaoHibernate<ModuloPerfil, Integer> implements ModuloPerfilDao
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized ModuloPerfilDaoHibernate getInstance ()
	{
		if (instance == null)
		{
			instance = new ModuloPerfilDaoHibernate();
		}
		return instance;
	}

	private ModuloPerfilDaoHibernate()
	{

	}

	private static ModuloPerfilDaoHibernate	instance;

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@SuppressWarnings("unchecked")
	public List<ModuloPerfil> findByIdPerfil (Integer perfilId)
	{
		return getSession().createCriteria(ModuloPerfil.class).add(Restrictions.eq("perfil.CId", perfilId)).list();
	}

	@SuppressWarnings("unchecked")
	public List<ModuloPerfil> findByIdModulo (Integer idModulo)
	{
		return getSession().createCriteria(ModuloPerfil.class).add(Restrictions.eq("modulo.CId", idModulo)).list();
	}

	@SuppressWarnings("unchecked")
	public List<ModuloPerfil> findByIdTipoPermiso (Integer idTipoPermiso)
	{
		return getSession().createCriteria(ModuloPerfil.class).add(Restrictions.eq("tipoPermiso.CId", idTipoPermiso)).list();
	}

	@Override
	protected void onPreCreated (ModuloPerfil entity)
	{

	}

	@Override
	protected void onCreated (ModuloPerfil entity)
	{

	}

	@Override
	protected void onUpdate (ModuloPerfil entity)
	{

	}

	@Override
	protected void onPreRemove (ModuloPerfil entity)
	{

	}
}
