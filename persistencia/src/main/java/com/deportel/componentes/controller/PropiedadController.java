package com.deportel.componentes.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.PropiedadDaoHibernate;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoPropiedad;

/**
 * @author Emy
 */
public class PropiedadController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private PropiedadController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static PropiedadController instance;

	public synchronized static PropiedadController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new PropiedadController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger			log	= Logger.getLogger(PropiedadController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	public Propiedad findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Buscando la propiedad con id [" + id + "]");
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Propiedad propiedad = dao.find(id);
			tx.commit();
			return propiedad;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	public Propiedad create (Propiedad propiedad)
	{
		log.debug("Creando una propiedad");
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			propiedad = dao.create(propiedad);
			tx.commit();
			return propiedad;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	public void remove (Integer id) throws InstanceNotFoundException
	{
		log.debug("Creando la entidad de Suscripcion");
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			dao.remove(id);
			tx.commit();
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
		}
	}

	public Propiedad update (Propiedad propiedad)
	{
		log.debug("Actualizando la propiedad: " + propiedad);
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			propiedad = dao.update(propiedad);
			tx.commit();
			return propiedad;
		}
		catch (final RuntimeException e)
		{
			tx.rollback();
			throw e;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return propiedad;
		}
	}

	public Set<Propiedad> filterPropertiesBySource (Set<Propiedad> properties, FuenteContenido source)
	{
		final Set<Propiedad> propFiltradas = new HashSet<Propiedad>(properties.size());
		for (final Propiedad propiedad : properties)
		{
			if (source.equals(propiedad.getFuenteContenido()))
			{
				propFiltradas.add(propiedad);
			}
		}
		return propFiltradas;
	}

	public List<Propiedad> propertiesFilteredByComponentTypeAndSource (Componente component, TipoPropiedad type, FuenteContenido source)
	{
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<Propiedad> propiedad = dao.propertiesFilteredByComponentTypeAndSource(component, type, source);
			tx.commit();
			return propiedad;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return null;
		}
	}

	public void removeProperties (List<Propiedad> properties)
	{
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			for (final Propiedad property : properties)
			{
				if (property.getCId() != null)
				{
					dao.remove(property.getCId());
				}
			}
			tx.commit();
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
		}
	}

	public void persistProperties (Collection<Propiedad> properties)
	{
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			for (final Propiedad property : properties)
			{
				if (property.getCId() == null)
				{
					dao.create(property);
				}
				else
				{
					dao.update(property);
				}
			}
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
	 * @param idTipoPropiedad
	 * @param idComponente
	 * @return
	 */
	public Propiedad findByIdTipoPropiedadIdComponente (Integer idTipoPropiedad, Integer idComponente)
	{
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Propiedad propiedad = dao.findByIdTipoPropiedadIdComponente(idTipoPropiedad, idComponente);
			tx.commit();
			return propiedad;
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
		final PropiedadDaoHibernate dao = new PropiedadDaoHibernate(this.uniqueSession);
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
