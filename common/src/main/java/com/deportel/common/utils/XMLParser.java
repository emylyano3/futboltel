package com.deportel.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.deportel.common.Constants;

/**
 * Clase estática que presta funcionalidad de parseo de XML
 *
 * @author Emy
 * @since 02/01/2010
 */
public class XMLParser implements Constants
{
	private static Logger		log						= Logger.getLogger(XMLParser.class);

	public static final String	JAXP_SCHEMA_SOURCE		= "http://java.sun.com/xml/jaxp/properties/schemaSource";
	public static final String	JAXP_SCHEMA_LANGUAGE	= "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	public static final String	W3C_XML_SCHEMA			= "http://www.w3.org/2001/XMLSchema";
	public static final String	SPECIFIC_SCHEMA			= "http://svn3.xp-dev.com/svn/Futboltel/trunk/Implementacion/fulboltel/guibuilder/src/main/resources/gui.xsd";

	/**
	 * Parsea el XML y devuelve los valores que corresponden con el tag y
	 * atributo enviados por parametro.<br/>
	 * <b>Ejemplo:</b> Quiero los valores del atributo "name" que se encuentran
	 * en cada uno de los tags "person". Esto devuelve "Maria, Jose, Pedro".
	 *
	 * @param file
	 *            El archivo XML
	 * @param tagName
	 *            El nombre del tag
	 * @param attName
	 *            El nombre del atributo
	 * @return {@link Vector} de {@link String} conteniendo todos los valores
	 *         encontrados
	 * @throws ParserConfigurationException
	 * @since 02/01/2010
	 */
	public static Vector<String> getAttributeValues(File file, String tagName, String attName)
	{
		Utils.validateParameters(file, tagName, attName);
		attName = attName.toLowerCase();
		tagName = tagName.toLowerCase();
		final Vector<String> vec = new Vector<String>();
		try
		{
			log.debug("Parsing " + file.getName());
			String attValue = null;
			NodeList nList = null;
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document doc = db.parse(file);
			if (tagName == null)
			{
				nList = doc.getChildNodes();
			}
			else
			{
				nList = doc.getElementsByTagName(tagName);
			}

			for (int i = 0; i < nList.getLength(); i++)
			{
				final Node node = nList.item(i);
				Node current = null;
				NamedNodeMap nMap;

				if (tagName == null)
				{
					nMap = doc.getAttributes();
				}
				else
				{
					nMap = node.getAttributes();
				}
				for (int j = 0; j < nMap.getLength(); j++)
				{
					current = nMap.item(j);
					if (current.getNodeName().equalsIgnoreCase(attName))
					{
						attValue = current.getNodeValue();
						vec.add(attValue);
					}
					else
					{
						continue;
					}
				}
			}
		}
		catch (final IOException e)
		{
			log.error("Error reading file: " + file.getName());
			e.printStackTrace();
		}
		catch (final ParserConfigurationException e)
		{
			log.error("Error parsing file: " + file.getName());
			e.printStackTrace();
		}
		catch (final SAXException e)
		{
			log.error("Error Parsing file: " + file.getName());
			e.printStackTrace();
		}
		catch (final NullPointerException e)
		{
			log.debug("Error parsing file " + file);
			e.printStackTrace();
		}
		log.debug("Parsing file " + file.getName() + " ended");
		return vec;
	}

/**
	 * Parsea el XML y genera un {@link LinkedHashMap}, donde se usa como keys a los
	 * tags del XML y los values son los atributos de los tags que tengan el nombre
	 * pasadao por parametro.<br/>
	 * Es decir que agrupa todos los atributos bajo un mismo tagname.<p/>
	 * <b>Ejemplo:</b> Si tengo un XML de la forma: <br/>
	 * '<'persona name="Pepe"'>' <br/>
	 * '<'persona name="Juan"'>' <br/>
	 * '<'empleado name="Jose"'>' <br/>
	 * Devuelve algo de la forma: <br/>
	 * persona=Pepe, Juan; empleado=Jose
	 *
	 * @param file El archivo XML
	 * @param attributeName El nombre del atributo
	 * @return LinkedHashMap con la forma <String, Vector <String>>
	 * @since 02/01/2010
	 */
	public static LinkedHashMap<String, Vector<String>> getTagNamesByAttribute(InputStream file, String attributeName)
	{
		final Node firstNode = initializeXMLParser(file);
		final LinkedHashMap<String, Vector<String>> mapComponentTagNames = new LinkedHashMap<String, Vector<String>>();

		final NodeList items = firstNode.getChildNodes();
		for (int i = 0; i < items.getLength(); i++)
		{
			final Node node = items.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				processNodeTagNamesByAttribute(node, mapComponentTagNames, attributeName);
			}
		}
		return mapComponentTagNames;
	}

	/**
	 * Inicializa el parser devolviendo el nodo que representa al XML.
	 *
	 * @param xmlFile
	 *            El archivo XML
	 * @return a {@link Node} with all the xml nodes
	 * @since 02/01/2010
	 */
	private static Node initializeXMLParser(InputStream xmlFile)
	{
		log.debug("Initializing XML parser");
		final Node firstNode = getDocument(xmlFile).getFirstChild();
		return firstNode;
	}

	/**
	 * Recibe un archivo XML lo parsea y devuelve un documento en memoria
	 * normalizado que representa a ese archvio.
	 *
	 * @param xmlFile
	 *            Archivo XML
	 * @return un documento en memoria normalizado que representa al XML enviado
	 *         por parametro
	 * @since 22/01/2011
	 */
	public static Document getDocument(InputStream xmlFile)
	{
		log.debug("Initializing XML parser");
		try
		{
			final DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
			// docfactory.setValidating(true);
			// docfactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA );
			// docfactory.setAttribute(JAXP_SCHEMA_SOURCE, SPECIFIC_SCHEMA);
			final DocumentBuilder docBuilder = docfactory.newDocumentBuilder();
			final InputSource source = new InputSource(xmlFile);
			final Document doc = docBuilder.parse(source);
			doc.getDocumentElement().normalize();
			return doc;
		}
		catch (final ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (final SAXException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converts a Node object into a String.
	 *
	 * @param node
	 *            the target Node.
	 * @return String - a String form of the specified Node.
	 * @since 02/01/2010
	 */
	public static String nodeToString(Node node)
	{
		Utils.validateParameters(node);
		final StringWriter sw = new StringWriter();
		try
		{
			final Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, Constants.YES);
			t.transform(new DOMSource(node), new StreamResult(sw));
		}
		catch (final TransformerException te)
		{
			log.error("nodeToString Transformer Exception");
			te.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * Process a Node and all it's child nodes to retrieve all component's
	 * specified attribute.
	 *
	 * @param node
	 *            the target Node.
	 * @param mapTagNames
	 *            the map with all the current attributes.
	 * @param attributeName
	 *            the attribute to find.
	 * @since 02/01/2010
	 */
	private static void processNodeTagNamesByAttribute(Node node, LinkedHashMap<String, Vector<String>> mapTagNames, String attributeName)
	{
		log.debug("Processing node");
		if (node.hasChildNodes())
		{
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++)
			{
				final Node childNode = childNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					processNodeTagNamesByAttribute(childNode, mapTagNames, attributeName);
				}
			}
		}
		// Process node.
		final NamedNodeMap nodeAttributes = node.getAttributes();
		if (nodeAttributes != null)
		{
			final Node attributeNode = nodeAttributes.getNamedItem(attributeName);
			if (attributeNode != null)
			{
				Vector<String> componentIds;
				// Get node name.
				final String tagName = node.getNodeName();
				// Check if this tag name it's present.
				if (mapTagNames.containsKey(tagName))
				{
					componentIds = mapTagNames.get(tagName);
				}
				else
				{
					componentIds = new Vector<String>();
				}
				// Add the component id to the Vector.
				componentIds.add(attributeNode.getNodeValue());
				// Add the Vector to the Map.
				mapTagNames.put(tagName, componentIds);
			}
		}
	}
}
