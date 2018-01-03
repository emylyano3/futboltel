package com.deportel.componentes.controller;

import java.util.List;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.deportel.common.exception.UserShowableException;
import com.deportel.componentes.dao.hibernate.ComponenteDaoHibernate;
import com.deportel.componentes.dao.hibernate.ConsultaDinamicaDaoHibernate;
import com.deportel.componentes.dao.hibernate.ParametroConsultaDaoHibernate;
import com.deportel.componentes.dao.hibernate.PropiedadDaoHibernate;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.ConsultaDinamica;
import com.deportel.componentes.modelo.ParametroConsulta;
import com.deportel.componentes.modelo.Propiedad;

/**
 * @author Emy
 */
public class ConsultaDinamicaController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private ConsultaDinamicaController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static ConsultaDinamicaController instance;

	public static synchronized ConsultaDinamicaController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new ConsultaDinamicaController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger					log						= Logger.getLogger(ConsultaDinamicaController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public ConsultaDinamica create (ConsultaDinamica consulta)
	{
		log.debug("Creando una ConsultaDinamica");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			consulta = dao.create(consulta);
			tx.commit();
			return consulta;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return consulta;
		}
	}

	/**
	 * Crea la consulta dinamica junto con todas las entidades que su creacion implica, como ser el componente
	 * de respuesta y los parametros. En caso de haber una falla durante la creacion de alguna de las
	 * entidades, se realiza un rollback de todas las acciones y ninguna sera persistida.
	 *
	 * @param dynaQuery
	 *            {@link ConsultaDinamica}
	 * @param queryParams
	 *            Una lista de {@link ParametroConsulta}
	 * @return {@link ConsultaDinamica}
	 * @throws UserShowableException
	 */
	public ConsultaDinamica createSafe (ConsultaDinamica dynaQuery) throws UserShowableException
	{
		log.debug("Creando una ConsultaDinamica");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			createResponseComponent(dynaQuery);
			dynaQuery = dao.create(dynaQuery);
			createDynaQueryParams(dynaQuery);
			tx.commit();
			return dynaQuery;
		}
		catch (final HibernateException e)
		{
			tx.rollback();
			e.printStackTrace();
			throw new UserShowableException("Ha ocurrido un error intentando crear la consulta de contenido. Cheqee si todos los atributos obligatorios fueron seleccionados.");
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			e.printStackTrace();
			throw new UserShowableException("Ha ocurrido un error intentando crear la consulta de contenido. Cheqee si todos los atributos obligatorios fueron seleccionados.");
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return dynaQuery;
		}
	}

	/**
	 *
	 * @param dynaQuery
	 * @throws InstanceNotFoundException
	 */
	private void createResponseComponent (ConsultaDinamica dynaQuery) throws InstanceNotFoundException
	{
		try
		{
			Componente componente = dynaQuery.getComponenteRespuesta();
			final ComponenteDaoHibernate componenteDao = new ComponenteDaoHibernate(this.uniqueSession);
			if (componente != null && componente.getCId() != null)
			{
				try
				{
					//El componente tiene id, debe existir en la base. Si es asi, lo actualizo
					componenteDao.update(componente);
				}
				catch (final HibernateException e)
				{
					log.error("El componente tenia un id, pero no se encontro en la base");
					throw e;
				}
			}
			else
			{
				componente = componenteDao.create(componente);
			}
			createComponentProperties(componente);
		}
		catch (final HibernateException e)
		{
			log.error("Error creando el componente de respuesta");
		}
	}

	/**
	 * Recorre las propiedades del componente. Si la propiedad ya existe la actualiza y si no la crea.
	 *
	 * @throws InstanceNotFoundException
	 */
	private void createComponentProperties (Componente componente) throws InstanceNotFoundException
	{
		final PropiedadDaoHibernate propiedadDao = new PropiedadDaoHibernate(this.uniqueSession);
		for (Propiedad propiedad : componente.getPropiedades())
		{
			if (propiedad.getCId() != null)
			{
				propiedad = propiedadDao.update(propiedad);
			}
			else
			{
				propiedad = propiedadDao.create(propiedad);
			}
		}
	}

	/**
	 * @throws InstanceNotFoundException
	 */
	private void createDynaQueryParams  (ConsultaDinamica dynaQuery) throws InstanceNotFoundException
	{
		final ParametroConsultaDaoHibernate parametroConsultaDao = new ParametroConsultaDaoHibernate(this.uniqueSession);
		for (ParametroConsulta parametroConsulta : dynaQuery.getParametrosConsulta())
		{
			parametroConsulta = parametroConsultaDao.create(parametroConsulta);
		}
	}

	/**
	 * @throws InstanceNotFoundException
	 */
	private void updateDynaQueryParams (ConsultaDinamica dynaQuery) throws InstanceNotFoundException
	{
		final ParametroConsultaDaoHibernate parametroConsultaDao = new ParametroConsultaDaoHibernate(this.uniqueSession);
		eliminarParametros(dynaQuery);
		for (ParametroConsulta parametroConsulta : dynaQuery.getParametrosConsulta())
		{
			parametroConsulta = parametroConsultaDao.create(parametroConsulta);
		}
	}

	public void eliminarParametros (ConsultaDinamica dynaQuery) throws InstanceNotFoundException
	{
		final ParametroConsultaDaoHibernate parametroConsultaDao = new ParametroConsultaDaoHibernate(this.uniqueSession);
		final List<ParametroConsulta> parametters = parametroConsultaDao.findByIdConsultaDinamica(dynaQuery.getCId());
		for (final ParametroConsulta parametroConsulta : parametters)
		{
			parametroConsultaDao.remove(parametroConsulta.getCId());
		}
	}

	/**
	 * Actualiza todos los datos de la consulta dinamica, tanto los datos propios como los datos de aquellas
	 * entidades a las que hace referencia tales como {@link ParametroConsulta} y {@link Componente}.
	 *
	 * @param targetDynaQuery
	 *            La consulta dinamica que se va a actualizar
	 * @param dynaQuery
	 *            La consulta dinamica de donde se van a tomar los datos
	 * @return targetDynaQuery actualizada.
	 * @throws UserShowableException
	 */
	public ConsultaDinamica updateSafe (ConsultaDinamica dynaQuery) throws UserShowableException
	{
		log.debug("Modificando una entidad de consulta dinamica");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			updateDynaQueryParams(dynaQuery);
			dynaQuery = dao.update(dynaQuery);
			tx.commit();
			return dynaQuery;
		}
		catch (final HibernateException e)
		{
			tx.rollback();
			e.printStackTrace();
			throw new UserShowableException("Ha ocurrido un error intentando crear la consulta de contenido. Cheqee si todos los atributos obligatorios fueron seleccionados.");
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			e.printStackTrace();
			throw new UserShowableException("No se pudo alctualizar alguno de los parametros de la consulta.");
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return dynaQuery;
		}
	}

	/**
	 *
	 * @param consultaDinamica
	 * @return
	 */
	public void update (ConsultaDinamica consultaDinamica)
	{
		log.debug("Modificando una entidad de consulta dinamica");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.update(consultaDinamica);
			tx.commit();
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param id
	 * @throws InstanceNotFoundException
	 */
	public void remove (Integer id) throws InstanceNotFoundException
	{
		log.debug("Eliminando una entidad de consulta dinamica");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.remove(id);
			tx.commit();
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			e.printStackTrace();
			throw new InstanceNotFoundException("No se encontro la instancia de [" + ConsultaDinamica.class + "] bajo el id [" + id + "]");
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
		}
	}

	public List<ConsultaDinamica> findAll ()
	{
		log.debug("Buscando todas las consultas");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<ConsultaDinamica> result = dao.findAll();
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	public ConsultaDinamica findByName (String name)
	{
		log.debug("Buscando la consulta por el nombre: " + name);
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final ConsultaDinamica result = dao.findByName(name);
			tx.commit();
			return result;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	public ConsultaDinamica findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Obteniendo una consulta dinamica a traves del id");
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final ConsultaDinamica cd = dao.find(id);
			tx.commit();
			return cd;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public ConsultaDinamica findByIdSolicitante (Integer idSolicitante) throws InstanceNotFoundException
	{
		log.debug("Obteniendo una consulta dinamica a traves del id de componente solicitante: " + idSolicitante);
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final ConsultaDinamica cd = dao.findByIdSolicitante(idSolicitante);
			if (cd == null)
			{
				throw new InstanceNotFoundException("No se encontro la instancia de [" + ConsultaDinamica.class + "] bajo el id de solicitante [" + idSolicitante + "]");
			}
			tx.commit();
			return cd;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	public void clear ()
	{
		final ConsultaDinamicaDaoHibernate dao = new ConsultaDinamicaDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.clear();
			tx.commit();
		}
		catch (final Exception e)
		{
			tx.rollback();
		}
	}
}
