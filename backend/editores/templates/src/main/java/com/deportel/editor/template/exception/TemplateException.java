package com.deportel.editor.template.exception;

public class TemplateException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public TemplateException()
	{
	}

	public TemplateException (String message)
	{
		super(message);
	}

	public TemplateException (String message, Throwable e)
	{
		super(message, e);
	}
}
