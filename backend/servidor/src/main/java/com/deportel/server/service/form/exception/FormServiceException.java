package com.deportel.server.service.form.exception;

import com.deportel.server.service.exception.ServerServiceException;

public class FormServiceException extends ServerServiceException
{
	private static final long	serialVersionUID	= 1L;

	public FormServiceException ()
	{
		super();
	}

	public FormServiceException (String message)
	{
		super(message);
	}
}
