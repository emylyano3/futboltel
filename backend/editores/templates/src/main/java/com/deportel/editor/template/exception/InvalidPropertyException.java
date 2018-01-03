package com.deportel.editor.template.exception;

public class InvalidPropertyException extends TemplateException
{
	private static final long	serialVersionUID	= 1L;

	public InvalidPropertyException()
	{
	}

	public InvalidPropertyException (String message)
	{
		super(message);
	}

	public InvalidPropertyException (String message, Throwable e)
	{
		super(message, e);
	}

}
