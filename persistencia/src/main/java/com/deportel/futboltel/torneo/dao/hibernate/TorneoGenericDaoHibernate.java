package com.deportel.futboltel.torneo.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.deportel.futboltel.torneo.dao.TorneoGenericDao;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;
import com.deportel.persistencia.utils.ConsultaUtils;
import com.deportel.persistencia.utils.QueryParam;

/**
 * E, es la clase persistente para la que se implementará el DAO PK, define el tipo del
 * identificador de la clase persistente. El identificador debe ser Serializable
 */
public class TorneoGenericDaoHibernate<E, PK extends Serializable> implements TorneoGenericDao<E, PK>
{
	private TorneoSessionFactoryUtil	sessionFactory;
	private final Class<E>				entityClass;
	private final Logger				log			= Logger.getLogger(this.getClass());

	@SuppressWarnings("rawtypes")
	public Class						domainClass	= getDomainClass();

	/**
	 * Se utiliza Java reflection para encontrar la clase del argumento genérico E y almacenarla en
	 * la propiedad entityClass
	 */
	@SuppressWarnings("unchecked")
	public TorneoGenericDaoHibernate()
	{
		this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<E> getPersistentClass()
	{
		return this.entityClass;
	}

	/**
	 * Se sobreescribe este método para setear valores por default
	 */
	public void onPreCreated(E entity)
	{

	}

	public void onCreated(E entity)
	{

	}
	/**
	 * 
	 */
	@Override
	public E create (E entity)
	{
		try
		{
			Session session = this.getSessionAndBeginTransaction();
			onPreCreated(entity);
			session.save(entity);
			onCreated(entity);
			this.endTransactionAndCloseSession(session);
			return entity;
		}
		catch (final HibernateException e)
		{
			this.log.error("Error en el create de la entity del tipo: " + entity.getClass() + e.getLocalizedMessage());
			throw e;
		}
	}

	/**
	 * 
	 */
	private void endTransactionAndCloseSession (Session session)
	{
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * 
	 */
	@Override
	public boolean exists (PK id)
	{
		return getSession().createCriteria(this.entityClass).add(Restrictions.idEq(id)).setProjection(Projections.id()).uniqueResult() != null;
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E find (PK id)
	{
		final Session session = this.getSessionAndBeginTransaction();
		final E entity = (E) session.get(this.entityClass, id);
		if (entity == null)
		{
			return null;
		}
		return entity;
	}

	/**
	 * Method to return the class of the domain object
	 */
	@SuppressWarnings("rawtypes")
	protected Class getDomainClass ()
	{
		if (this.domainClass == null)
		{
			final ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
			this.domainClass = (Class) thisType.getActualTypeArguments()[0];
		}
		return this.domainClass;
	}

	/**
	 * @return
	 */
	private SessionFactory getHibernateSession ()
	{
		try
		{
			return TorneoSessionFactoryUtil.getInstance().getSessionFactory();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return
	 */
	protected Session getSession ()
	{
		return this.sessionFactory.getCurrentSession();
	}

	/**
	 * @return
	 */
	public Session getSessionAndBeginTransaction()
	{
		Session session = this.openSession();
		session.beginTransaction();
		return session;
	}

	/**
	 * @return
	 */
	private Session openSession ()
	{
		return this.getHibernateSession().openSession();
	}

	/**
	 * 
	 */
	@Override
	public void remove (PK id)
	{
		if (!exists(id))
		{
			throw new NoSuchElementException();
		}
		final Session session = this.getSessionAndBeginTransaction();
		session.delete(find(id));
		this.endTransactionAndCloseSession(session);
	}

	/**
	 * Permite proporcionar la factoría a partir de la cual se obtendrá la sesión actual (a través
	 * del método getCurrentSession)
	 */
	public void setSessionFactory (TorneoSessionFactoryUtil sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	/*
	 * TODO: los campos que no recibe seteados la entity, los setea en null.
	 */
	@Override
	public E update (E entity)
	{
		try
		{
			final Session session = this.getSessionAndBeginTransaction();
			session.update(entity);
			this.endTransactionAndCloseSession(session);
			return entity;

		}
		catch (final HibernateException e)
		{
			this.log.error("Error realizando el update de la entidad del tipo: " + entity.getClass() + " " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<E> findByCriteria(Criterion... criterion)
	{
		Criteria crit = getSessionAndBeginTransaction().createCriteria(getPersistentClass());
		for (Criterion c : criterion)
		{
			crit.add(c);
		}
		return crit.list();
	}

	/**
	 * Find by criteria.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<E> findByCriteria(Map criterias)
	{
		Criteria criteria = getSessionAndBeginTransaction().createCriteria(getPersistentClass());
		criteria.add(Restrictions.allEq(criterias));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll ()
	{
		return getSessionAndBeginTransaction().createCriteria(getPersistentClass()).list();
	}

	/**
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	public List<?> excuteCustomQuery (String queryString, List<QueryParam> params) throws HibernateException
	{
		this.log.debug("Ejecutando query customizada: " + queryString);
		Session session = getSessionAndBeginTransaction();
		Query query = session.createQuery(queryString);
		ConsultaUtils.setQueryParams(query, params);
		List<?> result = query.list();
		session.close();
		return result;
	}
}
