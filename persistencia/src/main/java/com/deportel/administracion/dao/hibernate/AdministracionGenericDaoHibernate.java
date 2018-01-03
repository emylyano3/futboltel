package com.deportel.administracion.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.deportel.administracion.dao.AdministracionGenericDao;
import com.deportel.administracion.utils.AdministracionSessionFactoryUtil;

/**
 * E, es la clase persistente para la que se implementará el DAO PK, define el tipo del identificador de la
 * clase persistente. El identificador debe ser Serializable
 */
public abstract class AdministracionGenericDaoHibernate<E, PK extends Serializable> implements AdministracionGenericDao<E, PK>
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final Logger			log				= Logger.getLogger(this.getClass());

	protected static SessionFactory	sessionFactory	= AdministracionSessionFactoryUtil.getInstance().getSessionFactory();

	public Class<?>					domainClass		= getDomainClass();

	private final Class<E>			entityClass;

	private static Session			session;

	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	/**
	 * Se utiliza Java reflection para encontrar la clase del argumento genérico E y almacenarla en la
	 * propiedad entityClass
	 */
	@SuppressWarnings("unchecked")
	public AdministracionGenericDaoHibernate()
	{
		this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if (session == null)
		{
			session = sessionFactory.openSession();
		}
	}

	public Class<E> getPersistentClass ()
	{
		return this.entityClass;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public boolean exists (PK id)
	{
		return this.getSession()
		.createCriteria(this.entityClass)
		.add(Restrictions.idEq(id))
		.setProjection(Projections.id())
		.uniqueResult() != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E findById (PK id)
	{
		E entity = (E) this.getSession().get(this.entityClass, id);
		if (entity == null)
		{
			return null;
		}
		return entity;
	}

	@Override
	public E create (E entity)
	{
		try
		{
			onPreCreated(entity);
			this.getSession().save(entity);
			onCreated(entity);
			return entity;
		}
		catch (HibernateException e)
		{
			this.log.error("Error en el create de la entity del tipo: " + entity.getClass() + " " + e.getLocalizedMessage());
			throw e;
		}
	}

	@Override
	public void remove (PK id)
	{
		if (!exists(id))
		{
			throw new NoSuchElementException();
		}
		E entity = findById(id);
		onPreRemove(entity);
		this.getSession().delete(entity);
	}

	@Override
	public E update (E entity) throws HibernateException
	{
		try
		{
			this.getSession().update(entity);
			onUpdate(entity);
			return entity;
		}
		catch (HibernateException e)
		{
			this.log.error("Error realizando el update de la entidad del tipo: " + entity.getClass() + " " + e.getLocalizedMessage());
			return null;
		}
	}

	// *********************************************************************************************************************
	// Metodos Protected
	// *********************************************************************************************************************

	/**
	 * @param entity
	 */
	protected abstract void onPreCreated (E entity);

	/**
	 * @param entity
	 */
	protected abstract void onCreated (E entity);

	/**
	 * @param entity
	 */
	protected abstract void onUpdate (E entity);

	/**
	 * @param entity
	 */
	protected abstract void onPreRemove (E entity);

	/**
	 * Method to return the class of the domain object
	 */
	protected Class<?> getDomainClass ()
	{
		if (this.domainClass == null)
		{
			ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
			this.domainClass = (Class<?>) thisType.getActualTypeArguments()[0];
		}
		return this.domainClass;
	}

	public Session getSession ()
	{
		return session;
	}

}
