package com.deportel.editor.template.view.property.converter;

import java.util.List;

import com.deportel.componentes.controller.AccionComponenteController;
import com.deportel.componentes.modelo.AccionComponente;
import com.deportel.componentes.modelo.Propiedad;

public class ActionPropertyDataConverter extends PropertyDataConverter
{
	private static final long	serialVersionUID	= 1L;

	public ActionPropertyDataConverter(Propiedad propiedad)
	{
		super(propiedad);
	}

	@Override
	public String doConversion (String value)
	{
		List<AccionComponente> acciones = AccionComponenteController.getInstance(true).findAll();
		StringBuilder sb = new StringBuilder();
		String[] values = value.split(",");
		for (AccionComponente accion : acciones)
		{
			for (String theValue : values)
			{
				if (accion.getDNombre().equals(theValue))
				{
					sb.append(accion.getCCodigo()).append(",");
					break;
				}
			}
		}
		String result = sb.toString();
		result = result.length() > 1 ? result.substring(0, result.length() - 1) : result;
		return result;
	}

}
