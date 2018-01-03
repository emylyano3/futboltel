package com.deportel.persistencia.utils;

/**
 * @author Emy
 */
public class QueryParam
{
	private String	name;
	private Object	value;
	private String	type;
	private Integer	index;

	public QueryParam()
	{

	}

	public QueryParam(String name, Object value, String type)
	{
		this.name = name;
		this.value = value;
		this.type = type;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue (Object value)
	{
		this.value = value;
	}
	/**
	 * @return the value
	 */
	public Object getValue ()
	{
		return this.value;
	}
	/**
	 * @param name the name to set
	 */
	public void setName (String name)
	{
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName ()
	{
		return this.name;
	}
	/**
	 * @param type the type to set
	 */
	public void setType (String type)
	{
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public String getType ()
	{
		return this.type;
	}

	public void setIndex (Integer index)
	{
		this.index = index;
	}

	public Integer getIndex ()
	{
		return this.index;
	}

	@Override
	public String toString ()
	{
		return "[name=" + this.name + ", value=" + this.value + "]";
	}


}
