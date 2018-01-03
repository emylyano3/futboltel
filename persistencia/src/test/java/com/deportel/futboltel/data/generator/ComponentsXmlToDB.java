package com.deportel.futboltel.data.generator;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.deportel.common.Constants;
import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.ComponenteController;
import com.deportel.componentes.controller.FuenteContenidoController;
import com.deportel.componentes.controller.PropiedadController;
import com.deportel.componentes.controller.TemaController;
import com.deportel.componentes.controller.TipoComponenteController;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.Componente;
import com.deportel.componentes.modelo.FuenteContenido;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.componentes.modelo.Tema;
import com.deportel.componentes.modelo.TipoPropiedad;
import com.deportel.futboltel.torneo.utils.TorneoSessionFactoryUtil;

public class ComponentsXmlToDB
{
	@BeforeClass
	public static void initClass ()
	{
		TorneoSessionFactoryUtil.getInstance();
	}

	public static void main (String[] args)
	{
		JUnitCore.runClasses(ComponentsXmlToDB.class);
	}

	public static JUnit4TestAdapter suite ()
	{
		return new JUnit4TestAdapter(ComponentsXmlToDB.class);
	}

	private final Logger					log							= Logger.getLogger(this.getClass());

	private final TemaController			temaController				= TemaController.getInstance(true);

	private final TipoComponenteController	tipoComponenteController	= TipoComponenteController.getInstance(true);

	private final TipoPropiedadController	tipoPropiedadController		= TipoPropiedadController.getInstance(true);

	private final ComponenteController		componenteController		= ComponenteController.getInstance(true);

	private final PropiedadController		propiedadController			= PropiedadController.getInstance(true);

	private final FuenteContenidoController	fuenteContenidoController	= FuenteContenidoController.getInstance(true);

	@Test
	public void dumpXML2DB () throws FileNotFoundException
	{
		try
		{
			final File file = new File("C:/Workspaces/ProyectoFinalMobileWorkspace/futboltel/data/futboltel.xml");
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			processNode(doc.getDocumentElement(), null);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	private final Tema	tema = this.temaController.findByName("Futboltel");

	private void processNode (Node node, Componente father)
	{
		this.log.debug("Procesando el nodo: " + node.getNodeName());
		final NamedNodeMap attributes = node.getAttributes();
		//Creo el componente representado por el nodo
		Componente componente = new Componente();
		componente.setTema(this.tema);
		componente.setMContenido(Constants.DESHABILITADO);
		componente.setMEstado(Constants.HABILITADO);
		componente.setTipoComponente(this.tipoComponenteController.findByXmlTag(node.getNodeName()));
		componente.setPadre(father);
		componente.setCComponente(getCodComponente(attributes));
		componente = createOrUpdateComponent(componente);
		//Creo la propiedades del componente segun los atributos del nodo
		Node item;
		Propiedad propiedad;
		String value;
		for (int i = 0; i < attributes.getLength(); i++)
		{
			item = attributes.item(i);
			propiedad = new Propiedad();
			propiedad.setBinaryData(null);
			propiedad.setComponente(componente);
			propiedad.setNAltoPantalla(0);
			propiedad.setNAnchoPantalla(0);
			value = item.getNodeValue();
			propiedad.setFuenteContenido(getFuenteContenido(value));
			propiedad.setDRegularData(getRegularData(value));
			propiedad.setTipoPropiedad(getTipoPropiedad(item.getNodeName()));
			propiedad = createOrUpdatePropiedad(propiedad);
		}
		//Proceso los hijos del nodo
		final NodeList childs = node.getChildNodes();
		for (int s = 0; s < childs.getLength(); s++)
		{
			final Node child = childs.item(s);
			if (child.getNodeType() != Node.TEXT_NODE)
			{
				processNode(child, componente);
			}
		}
	}

	/**
	 *
	 * @param propiedad
	 * @return
	 */
	private Propiedad createOrUpdatePropiedad (Propiedad propiedad)
	{
		Propiedad aux = this.propiedadController.findByIdTipoPropiedadIdComponente(propiedad.getTipoPropiedad().getCId(), propiedad.getComponente().getCId());
		if (aux != null)
		{
			aux.setBinaryData(propiedad.getBinaryData());
			aux.setComponente(propiedad.getComponente());
			aux.setNAltoPantalla(propiedad.getNAltoPantalla());
			aux.setNAnchoPantalla(propiedad.getNAnchoPantalla());
			aux.setFuenteContenido(propiedad.getFuenteContenido());
			aux.setDRegularData(getRegularData(propiedad.getDRegularData()));
			aux.setTipoPropiedad(propiedad.getTipoPropiedad());
			aux = this.propiedadController.update(aux);
			return aux;
		}
		else
		{
			propiedad = this.propiedadController.create(propiedad);
			return propiedad;
		}
	}

	/**
	 *
	 * @param componente
	 * @return
	 */
	private Componente createOrUpdateComponent (Componente componente)
	{
		String componentCode = componente.getCComponente();
		componentCode = fillZeros(componentCode, 7);
		Componente aux = this.componenteController.findByCodigo(componentCode);
		if (aux != null)
		{
			aux.setTema(componente.getTema());
			aux.setMContenido(componente.getMContenido());
			aux.setMEstado(componente.getMEstado());
			aux.setTipoComponente(componente.getTipoComponente());
			aux.setPadre(componente.getPadre());
			aux.setCComponente(componentCode);
			aux = this.componenteController.update(aux);
			return aux;
		}
		else
		{
			componente = this.componenteController.create(componente);
			return componente;
		}
	}

	/**
	 *
	 * @param attributes
	 * @return
	 */
	private String getCodComponente (NamedNodeMap attributes)
	{
		Node item;
		String value;
		for (int i = 0; i < attributes.getLength(); i++)
		{
			item = attributes.item(i);
			if ("id".equals(item.getNodeName()))
			{
				value = item.getNodeValue();
				return value.substring(1, value.length());
			}
		}
		return "";
	}

	/**
	 *
	 * @param componentCode
	 * @param q
	 * @return
	 */
	private String fillZeros (String componentCode, int q)
	{
		while (componentCode.length() < q)
		{
			componentCode = "0" + componentCode;
		}
		return componentCode;
	}

	/**
	 *
	 * @param nodeName
	 * @return
	 */
	private TipoPropiedad getTipoPropiedad (String nodeName)
	{
		if (!Utils.isNullOrEmpty(nodeName))
		{
			return this.tipoPropiedadController.findByXmlTag(nodeName);
		}
		return null;
	}

	/**
	 *
	 * @param nodeValue
	 * @return
	 */
	private String getRegularData (String nodeValue)
	{
		if (!Utils.isNullOrEmpty(nodeValue))
		{
			return nodeValue.substring(1, nodeValue.length());
		}
		return null;
	}

	/**
	 *
	 * @param nodeValue
	 * @return
	 */
	private FuenteContenido getFuenteContenido (String nodeValue)
	{
		if (!Utils.isNullOrEmpty(nodeValue))
		{
			final String tag = nodeValue.substring(0, 1);
			return this.fuenteContenidoController.findByXmlTag(tag);
		}
		return null;
	}
}

