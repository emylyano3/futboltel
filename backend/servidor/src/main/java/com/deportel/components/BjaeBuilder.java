package com.deportel.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.FuenteContenidoController;
import com.deportel.componentes.controller.TipoComponenteController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.TipoComponente;

/**
 * Clase encargada de construir el archivo binario que representa a un
 * conjunto de componentes que espera recibir la aplicacion mobile
 * como respuesta a su solicitud.
 *
 * @author Emy
 * @since 16/09/2011
 */
public class BjaeBuilder
{
	// ****************************************************************************
	// Constructor Singleton
	// ****************************************************************************

	public BjaeBuilder ()
	{
		super();
	}

	// ****************************************************************************
	// Atributos
	// ****************************************************************************

	private static final Logger				log							= Logger.getLogger(BjaeBuilder.class);

	private final TipoComponenteController	tipoComponenteController	= TipoComponenteController.getInstance(false);

	private final FuenteContenidoController	contenidoController			= FuenteContenidoController.getInstance(false);

	private final BjaeDataWriterFactory		bjaeDataWriterFactory		= new BjaeDataWriterFactory();

	// ****************************************************************************
	// Metodos publicos
	// ****************************************************************************

	/**
	 * Método encargado de armar el bjae partiendo de un conjunto de
	 * componentes del tipo {@link Componente}. Devuelve el bjae
	 * dentro de un {@link ByteArrayOutputStream}.
	 *
	 * @param components
	 *            Lista de {@link Componente}
	 * @return {@link ByteArrayOutputStream} con el bjae generado
	 */
	public ByteArrayOutputStream doBuild (List<Componente> components, int scw, int sch, boolean exportAll)
	{
		log.info("Armando el Bjae");
		ByteArrayOutputStream bjae = null;
		Map<Integer, List<Componente>> sortedComponents = groupComponentsByType(components);
		try
		{
			bjae = writeBjae(sortedComponents, scw, sch, exportAll);
			bjae.flush();
			return bjae;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				bjae.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Escribe el bjae en un {@link ByteArrayOutputStream}.
	 *
	 * @param groupedComponents
	 *            Los componentes que formaran al Bjae
	 * @param count
	 *            La cantidad de componentes
	 * @return {@link ByteArrayOutputStream} con el Bjae
	 * @throws IOException
	 */
	private ByteArrayOutputStream writeBjae (Map<Integer, List<Componente>> groupedComponents, int scw, int sch, boolean exportAll) throws IOException
	{
		log.info("Comineza la escritura del BJAE");
		ByteArrayOutputStream bjae = new ByteArrayOutputStream();
		List<TipoComponente> componentTypes = this.tipoComponenteController.findAll();
		List<Componente> componentsByType;
		Propiedad property;
		int codigoWww = this.contenidoController.findByName(WEB_SOURCE).getCFuenteContenido();
		int codigoUpd = this.contenidoController.findByName(UPDATED_SOURCE).getCFuenteContenido();
		// Recorro los componentes del tipo actual
		for (TipoComponente componentType : componentTypes)
		{
			componentsByType = groupedComponents.get(componentType.getCTipoComponente()) == null ? new ArrayList<Componente>() : groupedComponents.get(componentType.getCTipoComponente());
			log.debug("Se escribiran [" + componentsByType.size() + "] componentes del tipo de componente [" + componentType.getDNombre() + "]");
			// Escribo el tipo actual de componente
			bjae.write(new byte[] {(byte) componentType.getCTipoComponente()});
			// Escribo la cantidad de componentes del tipo actual
			bjae.write(new byte[] {(byte) componentsByType.size()});
			Map<Integer, Propiedad> propertiesToExport;
			for (Componente component : componentsByType)
			{
				// Busco que propiedades tengo que exportar
				propertiesToExport = getPropertiesToExport(component.getPropiedades(), codigoWww, exportAll);
				log.debug("  Se escribiran [" + propertiesToExport.size() + "] propiedades para el componente [" + componentType.getDNombre() + "]");

				// Escribo la cantidad de properties que se van a exportar
				bjae.write(new byte[] {(byte) propertiesToExport.size()} );
				Map<Integer, List<Propiedad>> groupedProperties = groupPropertiesByType(component.getPropiedades());
				for (Integer integer : groupedProperties.keySet())
				{
					property = filterProperties(groupedProperties.get(integer), scw, sch);
					//TODO Hacer que tome una property dummy en caso de que cuando filtre no devuelva nninguna
					if (!propertiesToExport.containsKey(property.getTipoPropiedad().getCTipoPropiedad()))
					{
						continue;
					}
					// Escribo el codigo de la property
					bjae.write(new byte[] {(byte) property.getTipoPropiedad().getCTipoPropiedad()});
					/*
					 * Escribo el source de la property. De la base saque WWW, pero la escribo UPD
					 * para que la app mobile sepa que es la actualizada
					 */
					bjae.write(new byte[] {(byte) codigoUpd} );
					this.bjaeDataWriterFactory.getWriter(property.getTipoPropiedad()).writeData(bjae, property);
				}
			}
		}
		bjae.flush();
		bjae.close();
		return bjae;
	}

	/**
	 *
	 * @param properties
	 * @param scw
	 * @param sch
	 * @return
	 */
	private Propiedad filterProperties (List<Propiedad> properties, int scw, int sch)
	{
		Propiedad candidate = null;
		for (Propiedad propiedad : properties)
		{
			if (propiedad.getNAltoPantalla() != 0 && propiedad.getNAnchoPantalla() != 0)
			{
				//Si alto y ancho es 0, significa que la property no depende de las dimensiones de la pantalla
				if (propiedad.getNAltoPantalla() <= sch && propiedad.getNAnchoPantalla() <= scw)
				{
					if (candidate != null)
					{
						if (getDimensionGap(propiedad, scw, sch) < getDimensionGap(candidate, scw, sch))
						{
							candidate = propiedad;
						}
					}
					else
					{
						candidate = propiedad;
					}
				}
			}
			else
			{
				if (candidate != null)
				{
					if (getDimensionGap(propiedad, scw, sch) < getDimensionGap(candidate, scw, sch))
					{
						candidate = propiedad;
					}
				}
				else
				{
					candidate = propiedad;
				}
			}
		}
		return candidate != null ? candidate : getDummyProperty(properties);
	}

	private Propiedad getDummyProperty (List<Propiedad> properties, String...attributes)
	{
		Propiedad dummy = new Propiedad();
		if (!Utils.isNullOrEmpty(properties))
		{
			Propiedad reference = properties.get(0);
			dummy.setTipoPropiedad(reference.getTipoPropiedad());
		}
		return dummy;
	}

	/**
	 *
	 * @param propiedad
	 * @param scw
	 * @param sch
	 * @return
	 */
	private int getDimensionGap (Propiedad propiedad, int scw, int sch)
	{
		int widthGap = scw - propiedad.getNAnchoPantalla() + 1;
		int heightGap = sch - propiedad.getNAltoPantalla() + 1;
		int converter = widthGap < 0 && heightGap < 0 ? -1 : 1;
		return converter * widthGap * heightGap;
	}

	/**
	 * Busca y almacena aquellas propiedades que deberan ser exportadas. Las propiedades que se exportan son
	 * aquellas que cumplen con lo siguiente:
	 * <ul>
	 * <li>Es una propiedad de exportacion obligatoria</li>
	 * <li>Es una propiedad cuya fuente de contenido es la indicada en el parametro sourceCode</li>
	 * </ul>
	 * Cuenta la cantidad de properties alojadas en el {@link Set} Estan condiciones serán pasadas por alto en
	 * caso de que el parametro exportAll sea verdadero, pues indica que todas las porpiedades deberan ser
	 * exportadas.
	 *
	 * @param properties
	 *            Las propiedades a chequear
	 * @param sourceCode
	 *            El codigo de fuente de contenido
	 * @param exportAll
	 *            Si se deben exportar todas las propiedades o no
	 * @return un Map con todas las propiedades a exportar.
	 */
	private Map<Integer, Propiedad> getPropertiesToExport (Set<Propiedad> properties, int sourceCode, boolean exportAll)
	{
		int propertyCode;
		Map<Integer, Propiedad> propertiesToExport = new HashMap<Integer, Propiedad>();
		for (Propiedad property : properties)
		{
			propertyCode = property.getTipoPropiedad().getCTipoPropiedad();
			if (property.getFuenteContenido().getCFuenteContenido() == sourceCode || isObligatoryProperty(propertyCode) || exportAll)
			{
				propertiesToExport.put(propertyCode, property);
			}
		}
		return propertiesToExport;
	}

	/**
	 *
	 * @param code
	 * @return
	 */
	private boolean isObligatoryProperty (int code)
	{
		if (obligatoryProperties.containsKey(code))
		{
			return true;
		}
		return false;
	}

	/**
	 * Toma una lista de componentes y los agrupa segú su tipo.
	 *
	 * @param components
	 *            Los componentes a agrupar
	 * @return {@link Collection} de {@link List} de
	 *         {@link Componente}. Cada lista dentro de la collection
	 *         posee componentes de un mismo tipo.
	 */
	private Map<Integer, List<Componente>> groupComponentsByType (List<Componente> components)
	{
		log.debug("Se ordenan los componentes por tipo");
		Map <Integer, List<Componente>> grouped = new HashMap<Integer, List<Componente>> ();
		Integer index;
		for (Componente component : components)
		{
			index = new Integer(component.getTipoComponente().getCTipoComponente());
			if (grouped.get(index) == null)
			{
				grouped.put(index, new ArrayList<Componente>());
			}
			grouped.get(index).add(component);
		}
		return grouped;
	}

	/**
	 * Toma una lista de componentes y los agrupa segú su tipo.
	 *
	 * @param properties
	 *            Los componentes a agrupar
	 * @return {@link Collection} de {@link List} de
	 *         {@link Componente}. Cada lista dentro de la collection
	 *         posee componentes de un mismo tipo.
	 */
	private Map<Integer, List<Propiedad>> groupPropertiesByType (Set<Propiedad> properties)
	{
		log.debug("Se ordenan las propiedades por tipo");
		Map <Integer, List<Propiedad>> grouped = new HashMap<Integer, List<Propiedad>> ();
		Integer index;
		for (Propiedad property : properties)
		{
			index = property.getTipoPropiedad().getCTipoPropiedad();
			if (grouped.get(index) == null)
			{
				grouped.put(index, new ArrayList<Propiedad>());
			}
			grouped.get(index).add(property);
		}
		return grouped;
	}

	// ****************************************************************************
	// Constantes
	// ****************************************************************************

	public static final String			STRING_DATA				= "STRING";
	public static final String			BYTE_DATA				= "BYTE";
	public static final String			HEXA_BYTE_DATA			= "HBYTE";
	public static final String			SHORT_DATA				= "SHORT";
	public static final String			HEXA_SHORT_DATA			= "HSHORT";
	public static final String			INT_DATA				= "INT";
	public static final String			HEXA_INT_DATA			= "HINT";
	public static final String			LONG_DATA				= "LONG";
	public static final String			HEXA_LONG_DATA			= "HLONG";
	public static final String			IMAGE_DATA				= "IMAGE";

	public static final String			WEB_SOURCE				= "WEB";
	public static final String			UPDATED_SOURCE			= "UPD";

	public static final byte			EMBEDDED_DATA			= 1;
	public static final byte			NOT_EMBEDDED_DATA 		= 0;

	public static Map<Integer, Object> obligatoryProperties = new HashMap<Integer, Object>()
	{
		private static final long	serialVersionUID	= 1L;
		{
			put(Integer.valueOf(0), null);  // ID
			put(Integer.valueOf(20), null); // TIME STAMP
		}
	};
}