package com.deportel.componentes.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.deportel.componentes.dao.hibernate.TipoPropiedadDaoHibernate;
import com.deportel.componentes.modelo.TipoPropiedad;

/**
 * @author Emy
 */
public class TipoPropiedadController extends ComponentesController
{
	// *********************************************************************************************************************
	// Constructor
	// *********************************************************************************************************************

	private TipoPropiedadController(boolean uniqueSession)
	{
		super(uniqueSession);
	}

	private static TipoPropiedadController instance;

	public synchronized static TipoPropiedadController getInstance (boolean uniqueSession)
	{
		if (instance == null)
		{
			instance = new TipoPropiedadController(uniqueSession);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final Logger				log	= Logger.getLogger(TipoPropiedadController.class);

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * Busca un tipo de propiedad a través del codigo.
	 *
	 * @param codigo
	 *            El codigo del tipo de propiedad.
	 * @return El tipo de propiedad encontrada o <tt>null</tt> en
	 *         caso de no encontrar ninguna.
	 */
	public TipoPropiedad findByCodigo (Integer codigo)
	{
		log.debug("Buscando la fuente de contenido con codigo: [" + codigo +"]");
		final TipoPropiedadDaoHibernate dao = new TipoPropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoPropiedad tp = dao.findByCodigo(codigo);
			tx.commit();
			return tp;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	/**
	 * Busca un tipo de propiedad a traves del nombre de la misma.
	 *
	 * @param name
	 *            El nombre del tipo de propiedad
	 * @return {@link TipoPropiedad}
	 */
	public TipoPropiedad findByName (String name)
	{
		final TipoPropiedadDaoHibernate dao = new TipoPropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoPropiedad tp = dao.findByName(name);
			tx.commit();
			return tp;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	/**
	 * Busca un tipo de propiedad a traves de su tag XML.
	 *
	 * @param tagXml
	 *            El tagXml del tipo de propiedad
	 * @return {@link TipoPropiedad}
	 */
	public TipoPropiedad findByXmlTag (String tagXml)
	{
		final TipoPropiedadDaoHibernate dao = new TipoPropiedadDaoHibernate(this.uniqueSession);
		final Transaction tx = dao.getSession().beginTransaction();
		try
		{
			final TipoPropiedad tp = dao.findByXmlTag(tagXml);
			tx.commit();
			return tp;
		}
		catch (final Exception e)
		{
			tx.rollback();
			return null;
		}
	}

	/**
	 * Realiza un flush de la sesion
	 */
	public void clear ()
	{
		final TipoPropiedadDaoHibernate dao = new TipoPropiedadDaoHibernate(this.uniqueSession);
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

	//	/**
	//	 * Analiza el tipo de datos de la property y devuelve <tt>true</tt> si la misma debe tener un valor
	//	 * positivo
	//	 *
	//	 * @param tipoPropiedad
	//	 *            El tipo de propiedad que se quiere saber debe tener un valor numerico posiivo
	//	 * @return <tt>true</tt> si la misma debe tener un valor positivo, <tt>false</tt> en caso contrario
	//	 */
	//	public Boolean mustBePositive (TipoPropiedad tipoPropiedad)
	//	{
	//		return POSITIVE_PROP_TYPES.containsKey(tipoPropiedad.getDTagXml());
	//	}
	//
	//	public static final Map<String, String> POSITIVE_PROP_TYPES = new HashMap<String, String>()
	//	{
	//		private static final long	serialVersionUID	= 1L;
	//		{
	//			put(TipoPropiedad.TAG_HEIGHT, TipoPropiedad.TAG_HEIGHT);
	//			put(TipoPropiedad.TAG_WIDTH, TipoPropiedad.TAG_WIDTH);
	//			put(TipoPropiedad.TAG_X_POS, TipoPropiedad.TAG_X_POS);
	//			put(TipoPropiedad.TAG_Y_POS, TipoPropiedad.TAG_Y_POS);
	//			put(TipoPropiedad.TAG_UPDATE_TIME, TipoPropiedad.TAG_UPDATE_TIME);
	//			put(TipoPropiedad.TAG_TEXT_LIMIT, TipoPropiedad.TAG_TEXT_LIMIT);
	//			put(TipoPropiedad.TAG_SUBTYPE, TipoPropiedad.TAG_SUBTYPE);
	//			put(TipoPropiedad.TAG_FONT_TYPE, TipoPropiedad.TAG_FONT_TYPE);
	//			put(TipoPropiedad.TAG_TEXT_ALIGN, TipoPropiedad.TAG_TEXT_ALIGN);
	//			put(TipoPropiedad.TAG_MAR_X, TipoPropiedad.TAG_MAR_X);
	//			put(TipoPropiedad.TAG_MAR_Y, TipoPropiedad.TAG_MAR_Y);
	//			put(TipoPropiedad.TAG_CHILDS, TipoPropiedad.TAG_CHILDS);
	//			put(TipoPropiedad.TAG_ANCHOR, TipoPropiedad.TAG_ANCHOR);
	//			put(TipoPropiedad.TAG_TIME_STAMP, TipoPropiedad.TAG_TIME_STAMP);
	//		}
	//	};

	public static Map<String, TipoPropiedad>	NOT_REGULAR_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put("Fuente de imagen",null);
		}
	};

	public static Map<String, TipoPropiedad>	TAGS_EDITABLE_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_BG_COLOR, null);
			put(TipoPropiedad.TAG_FG_COLOR, null);
			put(TipoPropiedad.TAG_AL_COLOR, null);
			put(TipoPropiedad.TAG_X_POS, null);
			put(TipoPropiedad.TAG_Y_POS, null);
			put(TipoPropiedad.TAG_WIDTH, null);
			put(TipoPropiedad.TAG_HEIGHT, null);
			put(TipoPropiedad.TAG_TEXT, null);
			put(TipoPropiedad.TAG_MAR_X, null);
			put(TipoPropiedad.TAG_MAR_Y, null);
			put(TipoPropiedad.TAG_ACTION, null);
			put(TipoPropiedad.TAG_ACTION_PARAMS, null);
			put(TipoPropiedad.TAG_IMAGE_SOURCE, null);
			put(TipoPropiedad.TAG_TEXT_LIMIT, null);
			put(TipoPropiedad.TAG_TEXT_ALIGN, null);
		}
	};

	public static Map<String, TipoPropiedad>	TAGS_COLOR_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_BG_COLOR, null);
			put(TipoPropiedad.TAG_FG_COLOR, null);
			put(TipoPropiedad.TAG_AL_COLOR, null);
		}
	};

	public static Map<String, TipoPropiedad>	TAGS_TEXT_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_TEXT, null);
		}
	};

	public static Map<String, TipoPropiedad>	TAGS_ACTION_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_ACTION, null);
		}
	};

	public static Map<String, TipoPropiedad>	TAGS_IMAGE_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_IMAGE_SOURCE, null);
		}
	};

	public static Map<String, TipoPropiedad>	TAGS_ALIGN_PROPERTIES	= new HashMap<String, TipoPropiedad>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_TEXT_ALIGN, null);
			put(TipoPropiedad.TAG_ANCHOR, null);
		}
	};

	public static Map<String, IntPropertyTypeValues> INT_PROPERTY_TYPE_VALUES = new HashMap<String, IntPropertyTypeValues>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(TipoPropiedad.TAG_WIDTH, new IntPropertyTypeValues(0, 300));
			put(TipoPropiedad.TAG_HEIGHT, new IntPropertyTypeValues(0, 300));
			put(TipoPropiedad.TAG_X_POS, new IntPropertyTypeValues(0, 100));
			put(TipoPropiedad.TAG_Y_POS, new IntPropertyTypeValues(0, 100));
			put(TipoPropiedad.TAG_MAR_X, new IntPropertyTypeValues(0, 50));
			put(TipoPropiedad.TAG_MAR_Y, new IntPropertyTypeValues(0, 50));
		}
	};

}

