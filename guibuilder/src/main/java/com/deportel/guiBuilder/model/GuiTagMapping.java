package com.deportel.guiBuilder.model;

import java.awt.Component;
import java.util.Vector;

/**
 * Clase que mapea el nombre de los tags utilzados en los XML
 * con las clases que implementaran dicho componente.
 * @author Emy
 * @since 19/01/2011
 */
public class GuiTagMapping
{
	private String			name;
	private Class <?>		clazz;
	private String			group;
	private String			description;
	private Vector <String>	attributes;

	public GuiTagMapping (String name, Class <?> clazz, String group, String description, Vector <String> attributes)
	{
		this.name = name;
		this.clazz = clazz;
		this.group = group;
		this.description = description;
		this.attributes = attributes;
	}

	public String getName ()
	{
		return this.name;
	}
	public void setName (String name)
	{
		this.name = name;
	}
	public Class <?> getClase ()
	{
		return this.clazz;
	}
	public void setClase (Class<Component> clase)
	{
		this.clazz = clase;
	}
	public String getDescription ()
	{
		return this.description;
	}
	public void setDescription (String description)
	{
		this.description = description;
	}
	public Vector<String> getAttributes ()
	{
		return this.attributes;
	}
	public void setAttributes (Vector<String> attributes)
	{
		this.attributes = attributes;
	}

	public void setGroup (String group)
	{
		this.group = group;
	}

	public String getGroup ()
	{
		return this.group;
	}
}
