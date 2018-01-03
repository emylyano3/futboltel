package com.deportel.editor.template.validation;

import com.deportel.componentes.modelo.TipoPropiedad;

public class PropertyValidationFactory
{
	public static PropertyValidationStrategy getStrategy (TipoPropiedad tipoPropiedad)
	{
		if (TipoPropiedad.TAG_ACTION.equals(tipoPropiedad.getDTagXml()))
		{
			return new ActionPropertyValidation();
		}
		else if (TipoPropiedad.TAG_ACTION_PARAMS.equals(tipoPropiedad.getDTagXml()))
		{
			return new ActionParamsPropertyValidation();
		}
		else
		{
			return new GenericTextPropertyValidation();
		}
	}
}
