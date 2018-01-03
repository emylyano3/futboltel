package com.deportel.editor.template.view.property.converter;

import com.deportel.componentes.modelo.Propiedad;

public class NoConvertionPropertyDataConverter extends PropertyDataConverter
{
	private static final long	serialVersionUID	= 1L;

	public NoConvertionPropertyDataConverter(Propiedad propiedad)
	{
		super(propiedad);
	}

	/**
	 * No realiza ninguna conversion, devuelve el valor recibido sin ninguna transformacion
	 */
	@Override
	public String doConversion (String value)
	{
		return value;
	}

}
