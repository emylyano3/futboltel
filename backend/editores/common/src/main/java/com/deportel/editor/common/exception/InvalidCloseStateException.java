package com.deportel.editor.common.exception;

public class InvalidCloseStateException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public InvalidCloseStateException()
	{
		super();
	}

	public InvalidCloseStateException(String message)
	{
		super(message);
	}
}
