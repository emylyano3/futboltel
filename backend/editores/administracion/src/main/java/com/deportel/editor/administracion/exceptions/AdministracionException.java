package com.deportel.editor.administracion.exceptions;

import com.deportel.editor.administracion.main.AdministracionEditor;

public class AdministracionException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	public AdministracionException ()
	{
		super();
	}

	public AdministracionException (String errorCode)
	{
		super(AdministracionEditor.getTexts().get(errorCode));
	}

}
