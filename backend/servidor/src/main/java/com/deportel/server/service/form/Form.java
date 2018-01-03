package com.deportel.server.service.form;

import java.util.Iterator;
import java.util.Properties;

public class Form implements Iterable<Object>
{
	private Properties			fields;

	public Form()
	{
	}

	public Form(Properties parametters)
	{
		this.setFields(parametters);
	}

	@Override
	public Iterator<Object> iterator ()
	{
		return this.fields.keySet().iterator();
	}

	public void setFields (Properties fields)
	{
		this.fields = fields;
	}

	public Properties getFields ()
	{
		return this.fields;
	}

	public void setFields (String code, String name)
	{
		this.fields.put(code, name);
	}

	public String getField (String code)
	{
		return this.fields.getProperty(code);
	}

	public static interface Fields
	{
		public final static String	CELULAR	= "cel";
		public final static String	NAME	= "name";
		public final static String	EMAIL	= "email";
	}
}
