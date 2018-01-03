package com.deportel.editor.template.view.property.converter;

import java.util.List;

import com.deportel.componentes.controller.AlineacionComponenteController;
import com.deportel.componentes.modelo.AlineacionComponente;
import com.deportel.componentes.modelo.Propiedad;

public class AlignPropertyDataConverter extends PropertyDataConverter
{
	private static final long	serialVersionUID	= 1L;

	public AlignPropertyDataConverter(Propiedad propiedad)
	{
		super(propiedad);
	}

	@Override
	public String doConversion (String value)
	{
		List<AlineacionComponente> aligns = AlineacionComponenteController.getInstance(true).findAll();
		String[] values = value.split(",");
		StringBuilder sb = new StringBuilder();
		for (AlineacionComponente align : aligns)
		{
			for (String theValue : values)
			{
				if (align.getDNombre().equals(theValue))
				{
					sb.append(align.getCCodigo()).append(",");
					break;
				}
			}
		}
		String result = sb.toString();
		result = result.length() > 1 ? result.substring(0, result.length() - 1) : result;
		return result;
	}

}
