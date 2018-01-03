package com.deportel.editor.contenido.exception;

import com.deportel.common.exception.UserShowableException;
import com.deportel.editor.contenido.main.ContenidoEditor;

public class ContenidoEditorException extends UserShowableException
{
	private static final long	serialVersionUID	= 1L;

	public ContenidoEditorException ()
	{
		super();
	}

	public ContenidoEditorException (String errorCode)
	{
		super(ContenidoEditor.getTexts().get(errorCode));
	}
}
