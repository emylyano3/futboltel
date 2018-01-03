package com.deportel.componentes.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.deportel.componentes.dao.ComponentesGenericDao;
import com.deportel.componentes.utils.ComponentesSessionFactoryUtil;
import com.deportel.persistencia.utils.ConsultaUtils;
import com.deportel.persistencia.utils.QueryParam;

/**
 * E, es la clase persistente para la que se implementará el DAO PK,
 * define el tipo del identificador de la clase persistente. El
 * identificador debe ser Serializable.
 *
 * @author Emy
 */
public abstract class ComponentesGenericDaoHibernate<E, PK extends Serializable> implements ComponentesGenericDao<E, PK>
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger	log			= Logger.getLogger(ComponentesGenericDaoHibernate.class);

	public Class<?>				domainClass	= getDomainClass();

	private final Class<E>			entityClass;

	protected boolean			uniqueSession;

	protected static Session	staticSession;

	protected Session			session;

	// private final SessionFactory sessionFactory = ComponentesSessionFactoryUtil.getInstance().getSessionFactory();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Se utiliza Java reflection para encontrar la clase del
	 * argumento genérico E y almacenarla en la propiedad entityClass
	 */
	@SuppressWarnings("unchecked")
	public ComponentesGenericDaoHibernate(boolean uniqueSession)
	{
		this.uniqueSession = uniqueSession;
		this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Se utiliza Java reflection para encontrar la clase del
	 * argumento genérico E y almacenarla en la propiedad entityClass
	 */
	@SuppressWarnings("unchecked")
	public ComponentesGenericDaoHibernate()
	{
		this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 *
	 */
	@Override
	public E create (E entity)
	{
		try
		{
			getSession().save(entity);
			return entity;
		}
		catch (final HibernateException e)
		{
			log.error("Error en el create de la entity del tipo: " + entity.getClass() + " " + e.getLocalizedMessage());
			throw e;
		}
	}

	/**
	 *
	 */
	@Override
	public boolean exists (PK id)
	{
		try
		{
			final boolean exist = getSession().createCriteria(this.entityClass).add(Restrictions.idEq(id)).setProjection(Projections.id()).uniqueResult() != null;
			return exist;
		}
		catch (final HibernateException e)
		{
			return false;
		}
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void remove (PK id) throws InstanceNotFoundException
	{
		try
		{
			final E e = (E) getSession().get(this.entityClass, id);
			onPreRemove(getSession(), e);
			getSession().delete(e);
		}
		catch (final HibernateException e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	public E update (E entity)
	{
		try
		{
			getSession().update(entity);
			return entity;
		}
		catch (final HibernateException e)
		{
			log.error("Error realizando el update de la entidad del tipo: " + entity.getClass() + " causado por: " + e.getMessage());
			return entity;
		}
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E find (PK id) throws InstanceNotFoundException
	{
		final E entity = (E) getSession().get(this.entityClass, id);
		if (entity == null)
		{
			throw new InstanceNotFoundException("No se encontro lo instancia bajo el id [" + id + "]");
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
	 * Devuelve la sesion actual. Si no existe una, la crea,
	 * @return
	 */
	public Session getSession ()
	{
		if (this.uniqueSession)
		{
			if (staticSession == null)
			{
				staticSession = createStaticSession();
			}
			return staticSession;
		}
		if (this.session == null)
		{
			this.session = this.getHibernateSession().openSession();
		}
		return this.session;
	}


	private synchronized Session createStaticSession()
	{
		return this.getHibernateSession().openSession();
	}

	/**
	 * @param entity
	 */
	protected void onPreRemove(Session session, E entity)
	{

	}

	/**
	 * @return
	 */
	private SessionFactory getHibernateSession ()
	{
		try
		{
			return ComponentesSessionFactoryUtil.getInstance().getSessionFactory();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void clear ()
	{
		getSession().clear();
	}

	/**
	 *
	 * @param queryString
	 * @param params
	 * @return
	 */
	public List<?> excuteCustomQuery (String queryString, List<QueryParam> params) throws HibernateException
	{
		log.debug("Ejecutando query customizada: " + queryString);
		final Query query = getSession().createQuery(queryString);
		ConsultaUtils.setQueryParams(query, params);
		final List<?> result = query.list();
		return result;
	}
}
