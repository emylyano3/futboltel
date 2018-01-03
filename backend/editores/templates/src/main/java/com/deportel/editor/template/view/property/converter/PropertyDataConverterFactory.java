package com.deportel.editor.template.view.property.converter;

import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.Propiedad;

public class PropertyDataConverterFactory
{
	private static final long	serialVersionUID	= 1L;

	public static PropertyDataConverter getConverter (Propiedad propiedad)
	{
		String propTypeXmlTag = propiedad.getTipoPropiedad().getDTagXml();
		if (TipoPropiedadController.TAGS_ACTION_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return new ActionPropertyDataConverter(propiedad);
		}
		if (TipoPropiedadController.TAGS_ALIGN_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return new AlignPropertyDataConverter(propiedad);
		}
		if (TipoPropiedadController.TAGS_COLOR_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return new ColorPropertyDataConverter(propiedad);
		}
		return new NoConvertionPropertyDataConverter(propiedad);
	}
}
