package com.deportel.common.exception;

public class PropertiesNotFoundException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	public PropertiesNotFoundException ()
	{
		super();
	}

	public PropertiesNotFoundException (String message)
	{
		super(message);
	}

}
