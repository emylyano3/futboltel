package com.deportel.editor.template.controller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.deportel.common.Constants;
import com.deportel.common.exception.UserShowableException;
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
import com.deportel.editor.template.main.TemplateEditor;
import com.deportel.editor.template.model.ProjectTexts;


public class ComponentsImporter
{
	private static final Logger				log							= Logger.getLogger(ComponentsImporter.class);

	private final TipoComponenteController	tipoComponenteController	= TipoComponenteController.getInstance(true);

	private final TipoPropiedadController	tipoPropiedadController		= TipoPropiedadController.getInstance(true);

	private final ComponenteController		componenteController		= ComponenteController.getInstance(true);

	private final PropiedadController		propiedadController			= PropiedadController.getInstance(true);

	private final FuenteContenidoController	fuenteContenidoController	= FuenteContenidoController.getInstance(true);

	private final Map<String, List<String>>	result						= new HashMap<String, List<String>>();

	private String							contentMark					= Constants.DESHABILITADO;

	public void importComponentsDescriptor (File componentsDescriptorFile, boolean contentComponents) throws UserShowableException
	{
		DocumentBuilder db;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			db = dbf.newDocumentBuilder();
			Document doc;
			doc = db.parse(componentsDescriptorFile);
			doc.getDocumentElement().normalize();
			String nombreTema = doc.getDocumentElement().getAttribute(TipoPropiedad.TAG_NAME);
			nombreTema = nombreTema.substring(1, nombreTema.length());
			Tema tema = obtainTheme(nombreTema, true);
			updateContentMark(contentComponents);
			processNode(doc.getDocumentElement(), null, tema, true);
		}
		catch (ParserConfigurationException e)
		{
			throw new UserShowableException(TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_BAD_FORMED_FILE));
		}
		catch (SAXException e)
		{
			throw new UserShowableException(TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_BAD_FORMED_FILE));
		}
		catch (IOException e)
		{
			throw new UserShowableException(TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_FILE_NOT_FOUND));
		}
	}

	public Map<String, List<String>> processComponentsDescriptor (File componentsDescriptorFile) throws UserShowableException
	{
		DocumentBuilder db;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			this.result.clear();
			db = dbf.newDocumentBuilder();
			Document doc;
			doc = db.parse(componentsDescriptorFile);
			doc.getDocumentElement().normalize();
			String nombreTema = doc.getDocumentElement().getAttribute(TipoPropiedad.TAG_NAME);
			nombreTema = nombreTema.substring(1, nombreTema.length());
			Tema tema = obtainTheme(nombreTema, false);
			processNode(doc.getDocumentElement(), null, tema, false);
			return this.result;
		}
		catch (ParserConfigurationException e)
		{
			throw new UserShowableException(TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_BAD_FORMED_FILE));
		}
		catch (SAXException e)
		{
			throw new UserShowableException(TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_BAD_FORMED_FILE));
		}
		catch (IOException e)
		{
			throw new UserShowableException(TemplateEditor.getTexts().get(ProjectTexts.TI_ERROR_FILE_NOT_FOUND));
		}
	}

	private void updateContentMark(boolean contentComponents)
	{
		this.contentMark = contentComponents ? Constants.HABILITADO : Constants.DESHABILITADO;
	}

	private Tema obtainTheme(String nombreTema, boolean createIfNone)
	{
		Tema tema = TemaController.getInstance(true).findByName(nombreTema);
		if (createIfNone && tema == null)
		{
			tema = new Tema();
			tema.setDNombre(nombreTema);
			tema.setMEstado(Constants.HABILITADO);
			tema = TemaController.getInstance(true).create(tema);
		}
		return tema;
	}

	private void processNode (Node node, Componente father, Tema tema, boolean persist)
	{
		log.debug("Procesando el nodo: " + node.getNodeName());
		NamedNodeMap attributes = node.getAttributes();
		//Creo el componente representado por el nodo
		Componente componente = new Componente();
		componente.setTema(tema);
		componente.setMContenido(this.contentMark);
		componente.setMEstado(Constants.HABILITADO);
		componente.setTipoComponente(this.tipoComponenteController.findByXmlTag(node.getNodeName()));
		componente.setPadre(father);
		componente.setCComponente(getCodComponente(attributes));
		if (persist)
		{
			componente = createOrUpdateComponent(componente);
		}
		else
		{
			addComponentResultInfo(componente.getTipoComponente().getDNombre());
		}
		//Creo la propiedades del componente segun los atributos del nodo
		Node item;
		Propiedad propiedad;
		String value;
		createPropertyfather(componente, father, persist);
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
			if (persist)
			{
				propiedad = createOrUpdatePropiedad(propiedad);
			}
		}
		//Proceso los hijos del nodo
		NodeList childs = node.getChildNodes();
		for (int s = 0; s < childs.getLength(); s++)
		{
			Node child = childs.item(s);
			if (child.getNodeType() != Node.TEXT_NODE)
			{
				processNode(child, componente, tema, persist);
			}
		}
	}

	private void createPropertyfather (Componente componente, Componente father, boolean persist)
	{
		if (father != null)
		{
			Propiedad propiedad = new Propiedad();
			propiedad.setBinaryData(null);
			propiedad.setComponente(componente);
			propiedad.setNAltoPantalla(0);
			propiedad.setNAnchoPantalla(0);
			propiedad.setFuenteContenido(this.fuenteContenidoController.findByXmlTag(FuenteContenido.TAG_JAR));
			propiedad.setDRegularData(father.getCComponente());
			propiedad.setTipoPropiedad(this.tipoPropiedadController.findByXmlTag(TipoPropiedad.TAG_FATHER));
			if (persist)
			{
				propiedad = createOrUpdatePropiedad(propiedad);
			}
		}
	}

	private void addComponentResultInfo (String tipoComponente)
	{
		List<String> list = this.result.get(tipoComponente);
		if (list == null)
		{
			list = new ArrayList<String>();
			list.add(tipoComponente);
			this.result.put(tipoComponente, list);
		}
		else
		{
			list.add(tipoComponente);
		}
	}

	/**
	 *
	 * @param propiedad
	 * @return
	 */
	private Propiedad createOrUpdatePropiedad (Propiedad propiedad)
	{
		TipoPropiedad tipoPropiedad = propiedad.getTipoPropiedad();
		Componente componente = propiedad.getComponente();
		Propiedad aux = this.propiedadController.findByIdTipoPropiedadIdComponente(tipoPropiedad.getCId(), componente.getCId());
		if (aux != null)
		{
			log.debug("Actualizando la propiedad: " + propiedad);
			aux.setBinaryData(propiedad.getBinaryData());
			aux.setComponente(propiedad.getComponente());
			aux.setNAltoPantalla(propiedad.getNAltoPantalla());
			aux.setNAnchoPantalla(propiedad.getNAnchoPantalla());
			aux.setFuenteContenido(propiedad.getFuenteContenido());
			aux.setDRegularData(propiedad.getDRegularData());
			aux.setTipoPropiedad(propiedad.getTipoPropiedad());
			aux = this.propiedadController.update(aux);
			return aux;
		}
		else
		{
			log.debug("Creando la propiedad: " + propiedad);
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
		log.debug("Creando el componente: " + componente);
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
			this.componenteController.update(aux);
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
			if (TipoPropiedad.TAG_ID.equals(item.getNodeName()))
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
			String tag = nodeValue.substring(0, 1);
			return this.fuenteContenidoController.findByXmlTag(tag);
		}
		return null;
	}
}
