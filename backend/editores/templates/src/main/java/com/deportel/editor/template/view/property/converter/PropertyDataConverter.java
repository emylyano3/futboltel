package com.deportel.editor.template.view.property.converter;

import com.deportel.componentes.modelo.Propiedad;

public abstract class PropertyDataConverter
{
	private static final long	serialVersionUID	= 1L;

	protected Propiedad			propiedad;

	public PropertyDataConverter(Propiedad propiedad)
	{
	}

	public abstract String doConversion (String value);
}
