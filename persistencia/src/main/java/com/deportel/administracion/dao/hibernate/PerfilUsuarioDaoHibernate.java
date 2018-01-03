package com.deportel.administracion.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.PerfilUsuarioDao;
import com.deportel.administracion.modelo.PerfilUsuario;

public class PerfilUsuarioDaoHibernate extends AdministracionGenericDaoHibernate<PerfilUsuario, Integer> implements PerfilUsuarioDao
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized PerfilUsuarioDaoHibernate getInstance ()
	{
		if (instance == null)
		{
			instance = new PerfilUsuarioDaoHibernate();
		}
		return instance;
	}

	private PerfilUsuarioDaoHibernate()
	{

	}

	private static PerfilUsuarioDaoHibernate	instance;

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final Logger						log	= Logger.getLogger(PerfilUsuarioDaoHibernate.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca las entidades que relacionan al usuario con los perfiles.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PerfilUsuario> findByIdUsuario (Integer userId)
	{
		this.log.debug("Inicia findByIdUsuario");
		return getSession().createCriteria(PerfilUsuario.class).add(Restrictions.eq("usuario.CId", userId)).list();
	}

	/**
	 * Busca las entidades que relacionan al usuario con los perfiles.
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilUsuario> findByIdPerfil (Integer idPerfil)
	{
		this.log.debug("Inicia findByIdPerfil");
		return getSession().createCriteria(PerfilUsuario.class).add(Restrictions.eq("perfil.CId", idPerfil)).list();
	}

	@Override
	protected void onPreCreated (PerfilUsuario entity)
	{

	}

	@Override
	protected void onCreated (PerfilUsuario entity)
	{

	}

	@Override
	protected void onUpdate (PerfilUsuario entity)
	{

	}

	@Override
	protected void onPreRemove (PerfilUsuario entity)
	{

	}

}
