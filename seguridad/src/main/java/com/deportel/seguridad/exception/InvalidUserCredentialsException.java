package com.deportel.seguridad.exception;

/**
 * @author EMY
 */
public class InvalidUserCredentialsException  extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public InvalidUserCredentialsException(String message)
	{
		super(message);
	}
}
