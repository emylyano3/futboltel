package com.deportel.seguridad.exception;

/**
 * @author EMY
 */
public class InvalidUserAccessException  extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public InvalidUserAccessException(String message)
	{
		super(message);
	}
}
