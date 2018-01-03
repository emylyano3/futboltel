package com.deportel.administracion.dao.hibernate;

// Generated 10/01/2011 22:19:26 by Hibernate Tools 3.3.0.GA

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.PerfilDao;
import com.deportel.administracion.modelo.ModuloPerfil;
import com.deportel.administracion.modelo.Perfil;
import com.deportel.common.Constants;

public class PerfilDaoHibernate extends AdministracionGenericDaoHibernate<Perfil, Integer> implements PerfilDao
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static synchronized PerfilDaoHibernate getInstance ()
	{
		if (instance == null)
		{
			instance = new PerfilDaoHibernate();
		}
		return instance;
	}

	private static PerfilDaoHibernate	instance;

	private PerfilDaoHibernate()
	{
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final Logger					log					= Logger.getLogger(PerfilDaoHibernate.class);

	private final ModuloPerfilDaoHibernate	moduloPerfilDaoH	= ModuloPerfilDaoHibernate.getInstance();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	@SuppressWarnings("unchecked")
	public List<Perfil> findByPerfilId (Integer userId, int startIndex, int count)
	{
		return this.getSession()
		.createQuery("SELECT a FROM Perfil a WHERE a.id =:userId")
		.setParameter("userId", userId)
		.setFirstResult(startIndex)
		.setMaxResults(count)
		.list();
	}

	/**
	 * Busca si existe el perfil pasado como paramtro
	 *
	 * @param nombre
	 *            del perfil
	 * @return Los datos del perfil
	 * @author Emy
	 * @since 01/02/2011
	 */
	@Override
	public Perfil findByName (String name)
	{
		Perfil perfil = (Perfil) this.getSession()
		.createCriteria(Perfil.class)
		.add(Restrictions.like(Perfil.FIELD_NAME, name))
		.uniqueResult();
		return perfil;
	}

	/**
	 * Obtiene todos los perfiles dados de alta
	 *
	 * @return {@link Perfil} List
	 * @throws EntityNotFoundException
	 * @since 5/03/2011
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Perfil> findAll ()
	{
		List<Perfil> resultados = this.getSession()
		.createCriteria(Perfil.class)
		.add(Restrictions.eq(Perfil.FIELD_STATE, Constants.HABILITADO))
		.list();
		return resultados;
	}

	@Override
	protected void onPreCreated (Perfil perfil)
	{
	}

	@Override
	protected void onCreated (Perfil entity)
	{
		this.log.debug("Creando la asociacion perfil - modulo - tipo permiso");
		for (ModuloPerfil moduloPerfil : entity.getModulosPerfil())
		{
			moduloPerfil.setPerfil(entity);
			this.moduloPerfilDaoH.create(moduloPerfil);
		}
	}

	@Override
	protected void onUpdate (Perfil entity)
	{
		List<ModuloPerfil> modulosPerfilDB = this.moduloPerfilDaoH.findByIdPerfil(entity.getCId());
		int index;
		ModuloPerfil aux;
		for (ModuloPerfil moduloPerfil : entity.getModulosPerfil())
		{
			moduloPerfil.setPerfil(entity);
			if ((index = modulosPerfilDB.indexOf(moduloPerfil)) != -1)
			{
				aux = modulosPerfilDB.remove(index);
				moduloPerfil.setCId(aux.getCId());
				this.moduloPerfilDaoH.update(moduloPerfil);
			}
			else
			{
				this.moduloPerfilDaoH.create(moduloPerfil);
			}
		}
		for (ModuloPerfil moduloPerfil : modulosPerfilDB)
		{
			this.moduloPerfilDaoH.remove(moduloPerfil.getCId());
		}
	}

	@Override
	protected void onPreRemove (Perfil entity)
	{
		for (ModuloPerfil moduloPerfil : entity.getModulosPerfil())
		{
			this.moduloPerfilDaoH.remove(moduloPerfil.getCId());
		}
	}
}
