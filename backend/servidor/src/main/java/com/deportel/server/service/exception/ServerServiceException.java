package com.deportel.server.service.exception;

import com.deportel.server.web.ResourceManager;

public class ServerServiceException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	public ServerServiceException()
	{
		super();
	}

	public ServerServiceException(String errorCode)
	{
		super(ResourceManager.getInstance().getText(errorCode));
	}

}
