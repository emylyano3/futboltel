package com.deportel.editor.template.view.property.converter;

import com.deportel.common.utils.Converter;
import com.deportel.componentes.modelo.Propiedad;

public class ColorPropertyDataConverter extends PropertyDataConverter
{
	private static final long	serialVersionUID	= 1L;

	public ColorPropertyDataConverter(Propiedad propiedad)
	{
		super(propiedad);
	}

	@Override
	public String doConversion (String value)
	{
		int color = Converter.stringToInt(value);
		value = Integer.toHexString(color);
		return value.substring(2, value.length());
	}

}
