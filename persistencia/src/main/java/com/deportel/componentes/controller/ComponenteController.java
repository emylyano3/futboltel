package com.deportel.componentes.controller;

import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.ComponenteDaoHibernate;
import com.deportel.componentes.dao.hibernate.TipoComponenteDaoHibernate;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.modelo.TipoComponente;

/**
 * @author Emy
 */
public class ComponenteController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private ComponenteController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static ComponenteController instance;

	public static synchronized ComponenteController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new ComponenteController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private final static Logger					log							= Logger.getLogger(ComponenteController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca un componente a través del id del mismo
	 *
	 * @param id
	 *            El id del componente
	 * @return El componente encontrado o <tt>null</tt> en caso de no
	 *         encontrar ningun material que cumpla con el id enviado
	 * @throws InstanceNotFoundException
	 */
	public Componente findById (Integer id) throws InstanceNotFoundException
	{
		log.debug("Buscando el componente con id: [" + id +"]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Componente result = dao.find(id);
			tx.commit();
			return result;
		}
		catch (final InstanceNotFoundException e)
		{
			tx.rollback();
			throw e;
		}
	}

	/**
	 * Persiste un nuevo componente a partir de la etidad recibida.
	 *
	 * @param componente
	 *            {@link Componente} a persistir
	 * @return {@link Componente} con el id generado en la
	 *         persistencia.
	 */
	public Componente create (Componente componente)
	{
		log.debug("Creando la entidad de componente");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			componente = dao.create(componente);
			tx.commit();
			return componente;
		}
		catch (final Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return componente;
		}
	}

	/**
	 * Busca un componente a través del codigo de componente del mismo.
	 *
	 * @param codigo
	 *            El codigo del componente
	 * @return El componente encontrado o <tt>null</tt> en caso de no
	 *         encontrar ningun material que cumpla con el codigo enviado
	 */
	public Componente findByCodigo (String codigo)
	{
		log.debug("Buscando el componente con id: [" + codigo + "]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final Componente result = dao.findByCodigo(codigo);
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

	/**
	 * Busca todos los componentes que son de un determinado tipo.
	 * Realiza la busqueda a traves del id de tipo de componente.
	 *
	 * @param tipoComponente
	 *            El tipo de componentes a buscar
	 * @return Los componentes que pertenecen al tipo solicitado
	 */
	public List<Componente> findByIdTipoComponente (Integer tipoComponente)
	{
		log.debug("Buscando los componentes del tipo: [" + tipoComponente +"]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<Componente> result = dao.findByIdTipo(tipoComponente);
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

	/**
	 * Busca todos los componentes que son de un determinado tipo.
	 * Realiza la busqueda a traves del nombre de tipo de componente.
	 *
	 * @param tipoComponente
	 *            El tipo de componentes a buscar
	 * @return Los componentes que pertenecen al tipo solicitado
	 */
	public List<Componente> findByNombreTipoComponente (String nombretipoComponente)
	{
		log.debug("Buscando los componentes del tipo: [" + nombretipoComponente +"]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoComponenteDaoHibernate tipoComponenteDaoHibernate = new TipoComponenteDaoHibernate(this.uniqueSession);
			final TipoComponente tipoComponente = tipoComponenteDaoHibernate.findByNombre(nombretipoComponente);
			final List<Componente> result = dao.findByIdTipo(tipoComponente.getCid());
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

	/**
	 *
	 * @param idTema
	 * @param idTipoComponente
	 * @return
	 */
	public List<Componente> findByIdTemaAndIdTipoComponente (Tema tema, TipoComponente tipoComponente)
	{
		log.debug("Buscando los componentes del tema con id: [" + tema.getCId() + "] del tipo: [" + tipoComponente.getCid() +"]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<Componente> result = dao.findByIdTemaAndIdTipo(tema.getCId(), tipoComponente.getCid(), false);
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

	/**
	 *
	 * @param idTema
	 * @param idTipoComponente
	 * @param tipoContendio
	 * @return
	 */
	public List<Componente> findByIdTemaAndIdTipoComponente (Tema tema, TipoComponente tipoComponente, boolean tipoContendio)
	{
		log.debug("Buscando los componentes del tema: [" + tema + "] del tipo: [" + tipoComponente +"]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<Componente> result = dao.findByIdTemaAndIdTipo(tema.getCId(), tipoComponente.getCid(), tipoContendio);
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

	/**
	 * Busca todos los componentes que son de un determinado tipo.
	 * Realiza la busqueda a traves del nombre de tipo de componente.
	 *
	 * @param tipoComponente
	 *            El tipo de componentes a buscar
	 * @return Los componentes que pertenecen al tipo solicitado
	 */
	public List<Componente> findByNomNombreTipoComponente (String nombretipoComponente)
	{
		log.debug("Buscando los componentes del tipo: [" + nombretipoComponente +"]");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoComponenteDaoHibernate tipoComponenteDaoHibernate = new TipoComponenteDaoHibernate(this.uniqueSession);
			final TipoComponente tipoComponente = tipoComponenteDaoHibernate.findByNombre(nombretipoComponente);
			final List<Componente> result = dao.findByIdTipo(tipoComponente.getCid());
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

	public List<Componente> findAll ()
	{
		log.debug("Buscando todos los componentes");
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final List<Componente> result = dao.findAll();
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

	public Componente update (Componente componente)
	{
		log.debug("Actualizando el componente: " + componente);
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			componente = dao.update(componente);
			tx.commit();
			return componente;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			return componente;
		}
	}

	/**
	 * Recorre los componentes recibidos, y toma de ellos solo la property del tipo especificada por el
	 * parametro nombreTipoPropiedad, agregandola a la lista de retorno<br/>
	 * <b>Ejemplo:</b> Se quiere la propiedad "Name" de los componentes. Se devuelve entonces una lista del
	 * estilo: 'Nombre componente 1','Nombre componente 2','Nombre componente N'...
	 *
	 * @param componentes
	 *            Los componentes que se van a filtrar
	 * @param nombreTipoPropiedad
	 *            El nombre de la propiedad que se devolvera de cada componente
	 * @return Una lista con la propiedad seleccionada de cada componente que cumpla con el filtro de tipo de
	 *         componente y tema
	 */
	public List<Propiedad> getComponentsPropertyFiltered (List<Componente> componentes, String nombreTipoPropiedad)
	{
		final List<Propiedad> result = new ArrayList<Propiedad>();
		Propiedad propiedad;
		for (final Componente componente : componentes)
		{
			propiedad = componente.getPropiedadByTagXml(nombreTipoPropiedad);
			if (propiedad != null)
			{
				result.add(propiedad);
			}
		}
		return result;
	}

	/**
	 * Recibe una lista de componentes y deja solo aquellos que tengan al menos una de sus propiedad con la
	 * fuente de contenido especificada.
	 *
	 * @param components
	 *            La lista de componentes a filtrar
	 * @param source
	 *            La fuente de contenido por la que se va a filtrar
	 * @return La lista de componentes filtrada segun la fuente de contenido especificada
	 */
	public List<Componente> getEditableComponents (List<Componente> components)
	{
		final List<Componente> result = new ArrayList<Componente>(components.size());
		for (final Componente componente : components)
		{
			for (final Propiedad propiedad : componente.getPropiedades())
			{
				if (FuenteContenido.TAG_WEB.equals(propiedad.getFuenteContenido().getDCaracterXml()))
				{
					if (TipoPropiedadController.TAGS_EDITABLE_PROPERTIES.containsKey(propiedad.getTipoPropiedad().getDTagXml()))
					{
						result.add(componente);
						break;
					}
				}
			}
		}
		return result;
	}

	public List<Componente> filterComponentsByContentFlag (List<Componente> components, String flag)
	{
		final List<Componente> result = new ArrayList<Componente>(components.size());
		for (final Componente componente : components)
		{
			if (flag.equals(componente.getMContenido()))
			{
				result.add(componente);
			}
		}
		return result;
	}

	/**
	 *
	 * @param componente
	 * @return
	 */
	public Componente clone (Componente componente)
	{
		try
		{
			return componente.clone();
		}
		catch (final CloneNotSupportedException e)
		{
			return null;
		}
	}

	public void clear ()
	{
		final ComponenteDaoHibernate dao = new ComponenteDaoHibernate(this.uniqueSession);
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
