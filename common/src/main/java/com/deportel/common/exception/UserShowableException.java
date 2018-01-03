package com.deportel.common.exception;


/**
 * @author Emy
 */
public class UserShowableException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public UserShowableException()
	{
	}

	/**
	 * @param errorCode
	 */
	public UserShowableException(String message)
	{
		super(message);
	}

}
