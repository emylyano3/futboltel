package com.deportel.server.service.content.exception;

import com.deportel.server.service.exception.ServerServiceException;

public class ContentServiceException extends ServerServiceException
{
	private static final long	serialVersionUID	= 1L;

	public ContentServiceException ()
	{
		super();
	}

	public ContentServiceException (String message)
	{
		super(message);
	}
}
